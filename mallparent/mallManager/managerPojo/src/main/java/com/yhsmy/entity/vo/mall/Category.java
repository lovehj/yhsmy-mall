package com.yhsmy.entity.vo.mall;

import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/17 22:00
 **/
@Data
public class Category extends com.yhsmy.entity.mall.Category {

    private String cateParentName;

    private String name;

    public String getName(){
        return super.getCateName ();
    }

    public String getId() {
        return super.getCateId ();
    }

    private List<Category> children = new ArrayList<> (1);

    public String getCreateTimeStr(){
        return DateTimeUtil.localDateTimeToStr (super.getCreateTime ());
    }

    public String getModifyTimeStr() {
        return DateTimeUtil.localDateTimeToStr (super.getModifyTime ());
    }

}
