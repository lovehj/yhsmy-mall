package com.yhsmy.entity.vo.mall;

import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.enums.NormalEnum;
import lombok.Data;

/**
 * @auth 李正义
 * @date 2020/1/5 18:40
 **/
@Data
public class Content extends com.yhsmy.entity.mall.Content {

    private String contentCateName;

    private String contentPicId;

    public String getStartKey () {
        return ApproveEnum.CONTENT.getKey ();
    }

    public String getStateStr () {
        if (super.getState () == 1) {
            return "待审";
        }
        return NormalEnum.getValueByKey (super.getState ());
    }

}
