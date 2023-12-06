package com.lzhch.practice.business.controller;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzhch.practice.business.entity.OmsTest;
import com.lzhch.practice.business.service.OmsTestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public String selectOne(@PathVariable Long id) {
        String master = JSON.toJSONString(this.omsTestService.getById(id));
        log.info("master :{}", master);

        String slave = JSON.toJSONString(this.omsTestService.selectById(id));
        log.info("slave :{}", slave);

        String master1 = JSON.toJSONString(this.omsTestService.getById(id));
        log.info("master1 :{}", master1);
        return master + "\n" + slave + "\n" + master1;
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
     * 修改数据
     *
     * @param omsTest 实体对象
     * @return 修改结果
     */
    @PutMapping
    public String update(@RequestBody OmsTest omsTest) {
        return JSON.toJSONString(this.omsTestService.updateById(omsTest));
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

