package com.yhsmy.entity.sys;

import com.yhsmy.IConstant;
import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2019/11/9 22:47
 **/
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -2436037756816854897L;

    private String id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 10, message = "用户名控制在10个字符内！")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 60, message = "密码控制在2-60个字符内！")
    private String password;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 20, message = "真实姓名控制在2-20个字符内！")
    private String realName;

    /**
     * 微信扫码登录时的openId
     */
    @Size(min = 0, max = 60, message = "微信openId控制在0-60个字符内！")
    private String openId;

    /**
     * 所在部门
     */
    @NotBlank(message = "部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 20, max = 60, message = "部门控制在20-60个字符内！")
    private String departId;

    /**
     * 拥有角色
     */
    @NotBlank(message = "角色不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 20, max = 60, message = "角色控制在20-60个字符内！")
    private String roleId;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 20, message = "手机号控制在20个字符内！")
    @Pattern (regexp = IConstant.MOBILE,message = "手机号格式不正确!")
    private String mobile;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 5, max = 50, message = "邮箱控制在5-50个字符内！")
    @Pattern (regexp = IConstant.EMAIL,message = "邮箱格式不正确!")
    private String email;

    /**
     * 头像地址
     */
    @Size(min = 0, max = 150, message = "头像地址控制在150个字符内！")
    private String photo;

    /**
     * 用户类型 0=普通用户(默认) 1=管理员
     */
    private int ctype;

    /**
     * 记录操作状态 0=删除 1=正常 2=冻结
     */
    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;
}
