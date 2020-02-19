package com.yhsmy.entity.mall;

import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2020/1/5 18:25
 **/
@Data
public class Content implements Serializable {
    private static final long serialVersionUID = 7353279517417866140L;

    private String contentId;

    @NotBlank(message = "内容分类ID必填!",groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 30, max = 60, message = "内容分类ID控制在60个字符内!")
    private String conentCateId;

    private String userId;

    /**
     * 标题
     */
    @NotBlank(message = "标题必填!",groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 90, message = "标题控制在90个字符内!")
    private String title;

    /**
     * 副标题
     */
    @NotBlank(message = "副标题必填!",groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 10, max = 120, message = "副标题控制在120个字符内!")
    private String subTitle;

    /**
     * 备注
     */
    private String description;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * 图片地址
     */
    @NotBlank(message = "图片地址必填!",groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 10, max = 255, message = "图片地址控制在255个字符内!")
    private String picUrl;

    /**
     * 内容
     */
    @NotEmpty(message = "内容必填!",groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 200, message = "内容控制在200个字符内!")
    private String content;

    /**
     * 同一个contentCateId的排序
     */
    private int contentSort;

    /**
     * 0=删除 1=正常
     */
    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyer;

    private LocalDateTime modifyTime;

}
