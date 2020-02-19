package com.yhsmy.entity.mall;

import com.yhsmy.IConstant;
import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2019/12/17 21:57
 **/
@Data
public class Item implements Serializable {

    /**
     * 商品ID
     */
    private String itemId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 商品标题
     */
    @NotBlank(message = "商品标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 220, message = "商品标题控制在220个字符内！")
    private String title;

    /**
     * 商品卖点
     */
    @NotBlank(message = "商品卖点不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 2, max = 220, message = "商品卖点控制在220个字符内！")
    private String sellPoint;

    /**
     * 商品价格以元、角、分形式展现(原价)
     */
    @NotBlank(message = "商品价格不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Pattern (regexp = IConstant.POSITIVE_NUMBER, message = "商品价格格式不正确")
    private String price;

    /**
     * 商品折扣价以元、角、分形式展现,折扣价不能大于原价
     */
    @NotBlank(message = "商品价格不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Pattern (regexp = IConstant.POSITIVE_NUMBER, message = "商品价格格式不正确")
    private String disPrice;

    /**
     * 商品库存
     */
    @Min (value = 1, message = "商品库存的最小值为1", groups = {AddGroup.class, UpdateGroup.class})
    private int itemNum;

    /**
     * 库存余量
     */
    @Min (value = 0, message = "商品库存的最小值为0", groups = {AddGroup.class, UpdateGroup.class})
    private int leftItemNum;

    /**
     * 展示的商品图片
     */
    @Size(min = 0, max = 150, message = "商品图片控制在150个字符内！")
    private String itemImg;

    /**
     * 商品分类
     */
    private String categoryId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 审批备注
     */
    private String remark;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;
}
