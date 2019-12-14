package com.yhsmy.entity.vo.sys;

import com.yhsmy.utils.DateTimeUtil;
import lombok.Data;

/**
 * @auth 李正义
 * @date 2019/11/9 22:51
 **/
@Data
public class Role extends com.yhsmy.entity.sys.Role {
    private static final long serialVersionUID = -6009429074661810402L;

    private String menuIds;

    public String getCreateTimeStr() {
        return DateTimeUtil.localDateTimeToStr (this.getCreateTime ());
    }

    public String getModifyTimeStr() {
        return DateTimeUtil.localDateTimeToStr (this.getModifyTime ());
    }

}
