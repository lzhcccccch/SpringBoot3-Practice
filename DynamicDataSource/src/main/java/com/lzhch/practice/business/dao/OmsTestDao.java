package com.lzhch.practice.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzhch.practice.business.entity.OmsTest;
import org.apache.ibatis.annotations.Mapper;

/**
 * (OmsTest)表数据库访问层
 *
 * @author lzhch
 * @since 2023-12-05 17:50:11
 */

@Mapper
public interface OmsTestDao extends BaseMapper<OmsTest> {

}

