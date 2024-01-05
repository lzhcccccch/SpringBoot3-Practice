package com.lzhch.practice.business.controller;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzhch.practice.business.entity.OmsTest;
import com.lzhch.practice.business.mapper.OmsTestMapper;
import com.lzhch.practice.business.service.OmsTestService;
import com.lzhch.practice.dynamic.annotation.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (OmsTest)表控制层
 *
 * @author lzhch
 * @since 2023-12-05 17:50:11
 */

@Slf4j
@RestController
@RequestMapping("omsTest")
public class OmsTestController {
    /**
     * 服务对象
     */
    @Resource
    private OmsTestService omsTestService;
    @Resource
    private OmsTestMapper omsTestMapper;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param omsTest 查询实体
     * @return 所有数据
     */
    @GetMapping
    public String selectAll(Page<OmsTest> page, OmsTest omsTest) {
        return JSON.toJSONString(this.omsTestService.page(page, new QueryWrapper<>(omsTest)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    // @Transactional(rollbackFor = Exception.class)
    // @Transactional(rollbackFor = Exception.class, transactionManager="masterTransactionManager")
    // @MultiDataSourceTransactional(transactionManagers={"masterTransactionManager"})
    public String selectOne(@PathVariable Long id) {
        log.info("=======Controller Thread :{}", Thread.currentThread().getName());
        OmsTest omsTest = this.omsTestService.getById(id);
        String master = JSON.toJSONString(omsTest);
        log.info("master :{}", master);

        String slave = JSON.toJSONString(this.omsTestService.selectById(id));
        log.info("slave :{}", slave);

        log.info("=======Controller final Thread :{}", Thread.currentThread().getName());
        String master1 = JSON.toJSONString(this.omsTestService.getById(id));
        log.info("master1 :{}", master1);
        return master + "\n" + slave + "\n" + master1;
    }

    /**
     * 修改数据
     *
     * @param omsTest 实体对象
     * @return 修改结果
     */
    @PutMapping(value = "update")
    // @Transactional(rollbackFor = Exception.class)
    // @Transactional(rollbackFor = Exception.class, transactionManager = "masterTransactionManager")
    // @MultiDataSourceTransactional(transactionManagers={"masterTransactionManager"})
    // @DSTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @DataSource
    public String update(@RequestBody OmsTest omsTest) {
        // 正常流程如下, 需要将事务的传播机制设置为 Propagation.REQUIRES_NEW
        // 1. 只在 Controller 层添加 @Transactional(rollbackFor = Exception.class), 无法切换从库数据源
        // 2. 在 Controller 层 和 service 上 添加 @Transactional(rollbackFor = Exception.class), 无法切换从库数据源
        // 3. 在 Controller 层添加 @Transactional(rollbackFor = Exception.class), 在 service 上添加 @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW), 可以切换数据源



        // 正常流程不测试, 只测试抛异常情况, 在selfUpdateById方法中抛出异常

        this.omsTestMapper.selfUpdateById(omsTest);
        this.omsTestService.selfUpdateById(omsTest);
        // int a = 1/0;

        return "success";
    }

    /**
     * 使用 JTA 的方式修改数据
     */
    @PutMapping(value = "updateByJTA")
    @Transactional(rollbackFor = Exception.class)
    @DataSource
    public String updateByJTA(@RequestBody OmsTest omsTest) {
        // 集成 JTA 可以实现事务回滚, 但是必须手写 sql, 不能使用 mybatis-plus 的方法
        this.omsTestMapper.selfUpdateById(omsTest);
        // JTA 的形式可以允许事务的嵌套, 只测试了默认的事务传播机制 Propagation.REQUIRED
        this.omsTestService.selfUpdateById(omsTest);
        // int a = 1/0;

        return "success";
    }

    /**
     * 新增数据
     *
     * @param omsTest 实体对象
     * @return 新增结果
     */
    @PostMapping
    public String insert(@RequestBody OmsTest omsTest) {
        return JSON.toJSONString(this.omsTestService.save(omsTest));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public String delete(@RequestParam("idList") List<Long> idList) {
        return JSON.toJSONString(this.omsTestService.removeByIds(idList));
    }

}

