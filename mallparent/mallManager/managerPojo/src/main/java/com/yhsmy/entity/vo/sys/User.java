package com.yhsmy.entity.vo.sys;

import com.yhsmy.enums.NormalEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth 李正义
 * @date 2019/11/9 22:50
 **/
@Data
public class User extends com.yhsmy.entity.sys.User {
    private static final long serialVersionUID = 3415041868492418437L;

    private int loginType;

    private String rememberMe;

    private String roleName;

    private String departName;

    private String fileLibId;

    public boolean isRememberMe () {
        if (StringUtils.isEmpty (this.getRememberMe ()) ||
                !"on".equalsIgnoreCase (this.getRememberMe ())) {
            return false;
        }
        return true;
    }

    /**
     * 根据ctype字段，判断用户是否为超级用户
     *
     * @return
     */
    public boolean isAdmin () {
        if (super.getCtype () == 1) {
            return true;
        }
        return false;
    }

    public String getStateStr () {
        return NormalEnum.getValueByKey (this.getState ());
    }
}
