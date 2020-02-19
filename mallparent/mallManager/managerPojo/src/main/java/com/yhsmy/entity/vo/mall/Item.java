package com.yhsmy.entity.vo.mall;

import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;

/**
 * @auth 李正义
 * @date 2019/12/17 22:00
 **/
@Data
public class Item extends com.yhsmy.entity.mall.Item {

    private String cateName;

    private String itemImgId;

    public String getCreateTimeStr() {
        return DateTimeUtil.localDateTimeToStr (super.getCreateTime ());
    }

    public String getModifyTimeStr() {
        return DateTimeUtil.localDateTimeToStr (super.getModifyTime ());
    }

    /**
     * 获取状态
     *
     * @return
     */
    public String getStateStr () {
        if(super.getState () == NormalEnum.NORMAL.getKey ()) {
            return "待审";
        }
        return NormalEnum.getValueByKey (super.getState ());
    }

    /**
     * 获取已卖商品数量
     * @return
     */
    public int getSold() {
        return super.getItemNum () - super.getLeftItemNum ();
    }

    public String getStartKey() {
        return ApproveEnum.ITEMAUDIT.getKey ();
    }

}
