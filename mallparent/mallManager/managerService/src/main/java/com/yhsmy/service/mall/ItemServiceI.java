package com.yhsmy.service.mall;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.mall.Item;
import com.yhsmy.entity.vo.sys.User;

import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/17 22:38
 **/
public interface ItemServiceI {

    public DataGrid getListData (int state, QueryParams params);

    public Map<String, Object> getForm (String id);

    public Json formSubmit (Item item, User user);

    /**
     * @param ctype 0=删除 1=下架商品
     * @param id
     * @param user
     * @return
     */
    public Json delete (int ctype, String id, User user);

}
