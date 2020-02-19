package com.yhsmy.mapper.mall;

import com.yhsmy.entity.vo.mall.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/17 22:02
 **/
public interface CategoryMapper {

    /**
     * 保存商品分类
     *
     * @param category
     */
    public void addCategory (Category category);

    /**
     * 查询商品分类列表
     *
     * @param queryBy   0=忽略 1=商品名称 2=上级分类 3=只查一级节点
     * @param queryText
     * @return
     */
    public List<Category> findCategoryList (@Param("queryBy") int queryBy, @Param("queryText") String queryText);

    /**
     * 按条件统计商品总数
     *
     * @param parentId 商品上级分类ID null=忽略
     * @return
     */
    public int maxCategory (@Param("parentId") String parentId);

    /**
     * 根据ID查询商品分类
     *
     * @param id
     * @return
     */
    public Category findCategoryById (@Param("id") String id);


}
