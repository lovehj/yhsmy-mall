package com.yhsmy.entity.mall;

import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品表
 *
 * @auth 李正义
 * @date 2019/12/17 21:50
 **/
@Data
public class Category implements Serializable {
    private static final long serialVersionUID = 7701316913630467106L;

    /**
     * 商品分类ID
     */
    private String cateId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 1, max = 100, message = "商品名称控制在100个字符内！")
    private String cateName;

    /**
     * 上级目录
     */
    private String catePId;

    /**
     * 排序字段,表示同级目录的展现顺序
     */
    private int cateSort;

    /**
     * 商品备注
     */
    @Size(min = 0, max = 200, message = "商品备注控制在200个字符内！")
    private String remark;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;

}
