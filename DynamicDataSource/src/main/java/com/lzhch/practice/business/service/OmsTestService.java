package com.lzhch.practice.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzhch.practice.business.entity.OmsTest;

/**
 * (OmsTest)表服务接口
 *
 * @author lzhch
 * @since 2023-12-05 17:50:11
 */
public interface OmsTestService extends IService<OmsTest> {

    OmsTest selectById(Long id);

}

