package com.lzhch.practice.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzhch.practice.business.dao.OmsTestDao;
import com.lzhch.practice.business.entity.OmsTest;
import com.lzhch.practice.business.service.OmsTestService;
import com.lzhch.practice.dynamic.config.DataSourceType;
import com.lzhch.practice.dynamic.annotation.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (OmsTest)表服务实现类
 *
 * @author lzhch
 * @since 2023-12-05 17:50:11
 */

@Slf4j
@Service("omsTestService")
public class OmsTestServiceImpl extends ServiceImpl<OmsTestDao, OmsTest> implements OmsTestService {

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public OmsTest selectById(Long id) {
        log.info("=======Serivce Thread :{}", Thread.currentThread().getName());
        return baseMapper.selectById(id);
    }

}

