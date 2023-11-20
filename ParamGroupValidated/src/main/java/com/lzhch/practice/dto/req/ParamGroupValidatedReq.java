package com.lzhch.practice.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lzhch.practice.validatedtype.CreateParamValidated;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 参数分组校验入参
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/11/20 15:36
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamGroupValidatedReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     * 内部定义接口和统一定义接口任选其一即可
     */
    @NotNull(message = "用户id不能为空", groups = CreateParamValidated.class)
    // @NotNull(message = "用户id不能为空") // Service 层不进行分组校验
    // @NotNull(message = "用户id不能为空", groups = ParamGroupValidatedReq.Save.class)
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20个字符")
    private String username;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /**
     * 性别
     */
    private String sex;

    /**
     * 邮箱
     */
    @NotBlank(message = "联系邮箱不能为空")
    @Email(message = "邮箱格式不对")
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    // @Future(message = "时间必须是将来时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 保存的时候校验分组
     */
    public interface Save {
    }

    /**
     * 更新的时候校验分组
     */
    public interface Update {
    }

}
