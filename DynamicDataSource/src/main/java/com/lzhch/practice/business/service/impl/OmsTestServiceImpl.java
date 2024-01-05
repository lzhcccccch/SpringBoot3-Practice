package com.lzhch.practice.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzhch.practice.business.entity.OmsTest;
import com.lzhch.practice.business.mapper.OmsTestMapper;
import com.lzhch.practice.business.service.OmsTestService;
import com.lzhch.practice.dynamic.annotation.DataSource;
import com.lzhch.practice.dynamic.config.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * (OmsTest)表服务实现类
 *
 * @author lzhch
 * @since 2023-12-05 17:50:11
 */

@Slf4j
@Service("omsTestService")
// @DataSource(value = DataSourceType.SLAVE)
// @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class OmsTestServiceImpl extends ServiceImpl<OmsTestMapper, OmsTest> implements OmsTestService {

    @Resource
    private OmsTestMapper omsTestMapper;

    @Override
    // @DataSource(value = DataSourceType.SLAVE)
    // @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    // @MultiDataSourceTransactional(transactionManagers={"slaveTransactionManager"})
    public OmsTest selectById(Long id) {
        log.info("=======Serivce Thread :{}", Thread.currentThread().getName());
        return baseMapper.selectById(id);
    }

    @Override
    // @DSTransactional(rollbackFor = Exception.class)
    // @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, transactionManager = "slaveTransactionManager")
    @Transactional(rollbackFor = Exception.class)
    // @MultiDataSourceTransactional(transactionManagers={"masterTransactionManager"})
    @DataSource(value = DataSourceType.SLAVE)
    public void selfUpdateById(OmsTest omsTest) {
        OmsTest entity = BeanUtil.copyProperties(omsTest, OmsTest.class);
        entity.setTestA(omsTest.getTestA() + "slave");
        // omsTestMapper.updateById(entity);
        omsTestMapper.selfUpdateById(entity);
        int a = 1/0;
    }

}

