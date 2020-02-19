package com.yhsmy.service.mall;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.mall.Category;
import com.yhsmy.entity.vo.sys.User;

import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/17 22:38
 **/
public interface CategoryServiceI {

    public DataGrid getListData (QueryParams params);

    public Map<String, Object> getForm (String id);

    public Json formSubmit (Category category, User user);

    public Json delete (String id, User user);

    /**
     * 组合分类的父子关系
     *
     * @param params
     * @return List
     */
    public List<Category> getCategoryRelation (QueryParams params);

}
