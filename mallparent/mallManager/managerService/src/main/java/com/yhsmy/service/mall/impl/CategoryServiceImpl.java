package com.yhsmy.service.mall.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.mall.Category;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.mall.CategoryMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.mall.CategoryServiceI;
import com.yhsmy.utils.FastJsonUtil;
import com.yhsmy.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auth 李正义
 * @date 2019/12/17 22:39
 **/
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryServiceI {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Override
    public DataGrid getListData (QueryParams params) {
        return new DataGrid (PageHelper.startPage (params.getPageNo (), params.getPageSize ()).getTotal (),
                categoryMapper.findCategoryList (params.getQueryBy (), params.getQueryText ()));
    }

    @Override
    public Map<String, Object> getForm (String id) {
        Category category = null;
        if (StringUtils.isNotBlank (id)) {
            category = categoryMapper.findCategoryById (id);
            if (category == null) {
                throw new ServiceException ("商品分类未找到！");
            }
            if (StringUtils.isNotBlank (category.getCatePId ())) {
                Category pcate = categoryMapper.findCategoryById (category.getCatePId ());
                if (pcate == null || StringUtils.isEmpty (pcate.getCateName ())) {
                    throw new ServiceException ("商品分类的父父类未找到!");
                }
                category.setCateParentName (pcate.getCateName ());
            }
        } else {
            category = new Category ();
            category.setCateId ("");
            category.setCateName ("");
            category.setCatePId ("");
            category.setCateParentName ("");
            category.setRemark ("");
        }

        Map<String, Object> result = new HashMap<> (1);
        result.put ("category", category);
        result.put ("categoryList", FastJsonUtil.listToJSONArrayString (this.getCategoryRelation (new QueryParams (-1, ""))));
        return result;
    }


    @Transactional(readOnly = false)
    public Json formSubmit (Category category, User user) {
        String cname = user.getRealName (), cId = category.getCateId ();
        LocalDateTime now = LocalDateTime.now ();
        if (StringUtils.isNotBlank (cId)) {
            UpdateMap updateMap = new UpdateMap ("smy_category");
            updateMap.addField ("cateName", category.getCateName ());
            if (StringUtils.isNotBlank (category.getCatePId ())) {
                updateMap.addField ("catePId", category.getCatePId ());
            }
            updateMap.addField ("cateSort", this.getCategorySort (cId));
            if (StringUtils.isNotBlank (category.getRemark ())) {
                updateMap.addField ("remark", category.getRemark ());
            }
            updateMap.addField ("modifyor", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("cateId", cId);
            if (mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            String sortId = StringUtils.isNotBlank (category.getCatePId ()) ? category.getCatePId () : "";
            category.setCateId (UUIDUtil.generateUUID ());
            category.setCateSort (this.getCategorySort (sortId));
            category.setState (NormalEnum.NORMAL.getKey ());
            category.setCreator (cname);
            category.setModifyor (cname);
            category.setCreateTime (now);
            category.setModifyTime (now);
            categoryMapper.addCategory (category);
        }
        return Json.ok ();
    }

    /**
     * 删除商品分类时冻结该分类下的所有商品
     *
     * @param id
     * @param user
     * @return
     */
    @Transactional(readOnly = false)
    public Json delete (String id, User user) {
        String cname = user.getRealName ();
        LocalDateTime now = LocalDateTime.now ();
        UpdateMap delMap = new UpdateMap ("smy_category");
        delMap.addField ("state", NormalEnum.DELETE.getKey ());
        delMap.addField ("modifyor", cname);
        delMap.addField ("modifyTime", now);
        delMap.addWhere ("cateId", id);
        if (mybatisMapper.update (delMap) <= 0) {
            return Json.fail ();
        }

        UpdateMap itemDelMap = new UpdateMap ("smy_item");
        delMap.addField ("state", NormalEnum.FREEZE.getKey ());
        itemDelMap.addField ("modifyor", cname);
        itemDelMap.addField ("modifyTime", now);
        itemDelMap.addWhere ("cateId", id);
        if (mybatisMapper.update (itemDelMap) <= 0) {
            throw new ServiceException ("商品更新失败!");
        }
        return Json.ok ();
    }

    @Override
    public List<Category> getCategoryRelation (QueryParams params) {
        List<Category> categoryList = categoryMapper.findCategoryList (params.getQueryBy (), params.getQueryText ());
        if (!categoryList.isEmpty ()) {
            return categoryList.stream () //
                    .filter (c -> StringUtils.isBlank (c.getCatePId ()))
                    .map (c -> combCategoryRelation (c)) //
                    .collect (Collectors.toList ());
        }
        return new ArrayList<> (1);
    }

    /**
     * 使用递归封装每个分类的子节点
     *
     * @param c
     * @return
     */
    private Category combCategoryRelation (Category c) {
        List<Category> categoryList = c.getChildren ();
        if (categoryList.size () > 0) {
            // 继续查询子节点
            for (Category child : categoryList) {
                List<Category> childList = categoryMapper.findCategoryList (2, child.getCateId ());
                if (!childList.isEmpty () && childList.size () > 0) {
                    child.setChildren (childList);
                    for (Category _child : childList) {
                        this.combCategoryRelation (_child);
                    }
                }
            }
        }
        return c;
    }

    /**
     * 获取排序值
     *
     * @param id
     * @return
     */
    synchronized private int getCategorySort (String id) {
        int maxSort = categoryMapper.maxCategory (id);
        if (maxSort == 0) {
            return 1000; // 说明是第一条数据,它的值应该是1000
        }

        // id有可能是父商品的ID
        List<Category> categoryList = categoryMapper.findCategoryList (2, id);
        if (categoryList != null && categoryList.size () > 0) {
            // 说明当前ID有子节点,取最后一个子节点的值+1
            return maxSort + 1;
        }
        return maxSort + 1000;
    }
}
