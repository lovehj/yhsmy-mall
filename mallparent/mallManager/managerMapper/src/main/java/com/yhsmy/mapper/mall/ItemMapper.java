package com.yhsmy.mapper.mall;

import com.yhsmy.entity.vo.mall.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/17 22:06
 **/
public interface ItemMapper {

    /**
     * 保存商品
     *
     * @param item
     */
    public void addItem (Item item);

    /**
     * 按条件查询商品
     *
     * @param state     参考NormarlEnum
     * @param queryBy   1= 商品标题 2=商品卖点 3=商品分类ID
     * @param queryText
     * @return
     */
    public List<Item> findItemList (@Param("state") int state, @Param("queryBy") int queryBy, @Param("queryText") String queryText);

    /**
     * 根据商品ID查询
     *
     * @param id
     * @return
     */
    public Item findItemById (@Param("id") String id);

    /**
     * 根据流程实例ID查询商品
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    public Item findItemByProcessInstanceId (@Param("processInstanceId") String processInstanceId);
}
