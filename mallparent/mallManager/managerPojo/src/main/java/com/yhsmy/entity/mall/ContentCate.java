package com.yhsmy.entity.mall;

import com.yhsmy.validator.group.AddGroup;
import com.yhsmy.validator.group.UpdateGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 广告位分类
 *
 * @auth 李正义
 * @date 2020/1/5 17:48
 **/
@Builder
@Data
public class ContentCate implements Serializable {
    private static final long serialVersionUID = 8890725977765184385L;

    private String cateId;

    @NotBlank(message = "广告位分类名称不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 1, max = 120, message = "广告位分类名称控制在120个字符内！")
    private String cateName;

    /**
     * 1=轮播图 2=顶部广告 3=侧边广告 4=底部广告 5=页面中心广告 6=页面中心备选1 7=页面中心备选2 8=页面中心备选3 9=页面中心备选4 10=其它位置
     */
    @Min(value = 1, message = "广告位分类位置最小的为轮播图", groups = {AddGroup.class, UpdateGroup.class})
    @Max(value = 10, message = "广告位分类位置最大的为其它位置", groups = {AddGroup.class, UpdateGroup.class})
    private int ctype;

    private String catePid;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyer;

    private LocalDateTime modifyTime;

    @Tolerate //防止编译时找不到默认的构造器
    public ContentCate() {

    }
}
