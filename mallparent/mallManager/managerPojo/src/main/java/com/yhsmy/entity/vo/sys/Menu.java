package com.yhsmy.entity.vo.sys;

import com.yhsmy.enums.MenuTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/18 17:54
 **/
@Data
public class Menu extends com.yhsmy.entity.sys.Menu {
    private static final long serialVersionUID = 1857635558740878705L;

    private String parentName;

    /**
     * 子菜单
     */
    private List<Menu> children = new ArrayList<> (1);

    public Menu() {
        super("","","","","",0,0,"",0,"",null,"",null);
        parentName = "";
    }

    public String getMenuTypeStr() {
        return MenuTypeEnum.getValueByKey (this.getCtype ());
    }

}
