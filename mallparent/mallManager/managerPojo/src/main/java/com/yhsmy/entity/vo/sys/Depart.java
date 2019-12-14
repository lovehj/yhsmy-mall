package com.yhsmy.entity.vo.sys;

import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/29 17:14
 **/
@Data
public class Depart extends com.yhsmy.entity.sys.Depart {
    private static final long serialVersionUID = -5769248386748272245L;

    private String pName;

    private List<Depart> children = new ArrayList<> ();

    public String getCreateTimeStr() {
        return DateTimeUtil.localDateTimeToStr (this.getCreateTime ());
    }

    public String getModifyTimeStr() {
        return DateTimeUtil.localDateTimeToStr (this.getModifyTime ());
    }
}
