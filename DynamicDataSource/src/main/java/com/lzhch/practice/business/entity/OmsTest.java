package com.lzhch.practice.business.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.util.Date;

/**
 * (OmsTest)表实体类
 *
 * @author lzhch
 * @since 2023-12-05 17:50:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OmsTest extends Model<OmsTest> {

    private Integer id;

    private String testA;

    private Integer testB;

    private String remark;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}

