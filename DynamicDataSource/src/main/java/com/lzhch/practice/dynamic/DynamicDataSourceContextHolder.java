package com.lzhch.practice.dynamic;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.NamedThreadLocal;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 动态数据源切换控制器
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/12/5 11:06
 */

public final class DynamicDataSourceContextHolder {

    /**
     * 为什么要用链表存储(准确的是栈)
     * <pre>
     * 为了支持嵌套切换，如ABC三个service都是不同的数据源
     * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
     * 传统的只设置当前线程的方式不能满足此业务需求，必须使用栈，后进先出。
     * </pre>
     */
    private static final ThreadLocal<Deque<String>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<>("dynamic-datasource") {
        @Override
        protected Deque<String> initialValue() {
            return new ArrayDeque<>();
        }
    };

    private DynamicDataSourceContextHolder() {
    }

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return LOOKUP_KEY_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     * <p>
     * 如非必要不要手动调用，调用后确保最终清除
     * </p>
     *
     * @param dataSource 数据源名称
     */
    public static void push(String dataSource) {
        String dataSourceStr = StrUtil.isEmpty(dataSource) ? "" : dataSource;
        LOOKUP_KEY_HOLDER.get().push(dataSourceStr);
    }

    /**
     * 清空当前线程数据源
     * <p>
     * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
     * clear() 会清空所有的数据源, 导致后续的 Service 调用都会使用默认的数据源
     * </p>
     */
    public static void poll() {
        Deque<String> deque = LOOKUP_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            clear();
        }
    }

    /**
     * 强制清空本地线程
     * <p>
     * 防止内存泄漏，如手动调用了push可调用此方法确保清除
     * </p>
     */
    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }

    /* copilot 对该类的一些解释
    这个类是一个Java的工具类，它被定义为final，这意味着这个类不能被继承。
    它的构造函数是私有的，这意味着你不能在类的外部创建这个类的实例。
    这是一种常见的设计模式，叫做单例模式，用于确保一个类只有一个实例。

    这个类中的主要变量是一个名为`LOOKUP_KEY_HOLDER`的`ThreadLocal`对象。
    `ThreadLocal`是Java中的一个类，它提供了线程局部变量。这些变量与线程的生命周期相同，每个线程都保持其自己的独立副本。

    `LOOKUP_KEY_HOLDER`被初始化为一个`Deque<String>`（双端队列）的线程局部变量。
    这是通过`ThreadLocal`的匿名子类实现的，该子类覆盖了`initialValue`方法来提供`ThreadLocal`变量的初始值，即一个新的`ArrayDeque`实例。

    在应用运行期间，每个线程都会有自己的`LOOKUP_KEY_HOLDER`副本，每个副本都是一个独立的`Deque<String>`实例。
    这意味着，尽管`DynamicDataSourceContextHolder`类本身只有一个实例，但`LOOKUP_KEY_HOLDER`变量在每个线程中都有一个独立的副本。

    `push`方法用于将数据源名称添加到当前线程的`LOOKUP_KEY_HOLDER`双端队列的顶部，
    `peek`方法用于查看当前线程的`LOOKUP_KEY_HOLDER`双端队列的顶部元素（即最近添加的数据源名称），
    而`poll`方法用于移除当前线程的`LOOKUP_KEY_HOLDER`双端队列的顶部元素。
    如果双端队列为空，`clear`方法会被调用，它会清除当前线程的`LOOKUP_KEY_HOLDER`变量。


    `DynamicDataSourceContextHolder`类，包括其静态变量和方法，都存储在Java虚拟机（JVM）的方法区中。
    方法区是JVM的一部分，用于存储已被加载的类信息、常量、静态变量以及即时编译器编译后的代码等数据。

    具体来说，`DynamicDataSourceContextHolder`类的定义，包括其方法的字节码，都存储在方法区。
    类的静态变量，如`LOOKUP_KEY_HOLDER`，也存储在方法区。

    然而，`LOOKUP_KEY_HOLDER`变量指向的`ThreadLocal<Deque<String>>`对象实例并不存储在方法区，而是存储在堆区。
    堆区是JVM的另一部分，用于存储所有的对象实例。每个线程的`ThreadLocal`变量副本存储在各自线程的线程栈中。

    至于方法的调用，例如`push`、`peek`、`poll`和`clear`，它们在被调用时会创建一个栈帧存储在调用线程的Java栈中。
    Java栈是用于存储局部变量、操作数栈、动态链接和方法出口等信息的区域。
     */

}
