package com.yhsmy.entity.vo.mall;

import com.yhsmy.enums.ContentCateTypeEnum;
import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2020/1/5 18:20
 **/
@Data
public class ContentCate extends com.yhsmy.entity.mall.ContentCate {
    private static final long serialVersionUID = -1686530735647851929L;

    private String catePname;

    private List<ContentCate> children = new ArrayList<> (1);

    public String getId() {
        return super.getCateId ();
    }

    public String getName() {
        return super.getCateName ();
    }

    public String getCtypeStr () {
        return ContentCateTypeEnum.getValueByKey (super.getCtype ());
    }

    public String getCreateTimeStr () {
        return DateTimeUtil.localDateTimeToStr (super.getCreateTime ());
    }

    public String getModifyTimeStr () {
        return DateTimeUtil.localDateTimeToStr (super.getModifyTime ());
    }
}
