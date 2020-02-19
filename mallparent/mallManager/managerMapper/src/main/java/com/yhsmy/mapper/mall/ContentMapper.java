package com.yhsmy.mapper.mall;

import com.yhsmy.entity.vo.mall.Content;
import com.yhsmy.entity.vo.mall.ContentCate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2020/1/5 18:41
 **/
public interface ContentMapper {

    /**
     * 保存内容
     *
     * @param content
     */
    public void addContent (Content content);

    /**
     * 根据条件查询
     * @param  userId
     * @param state 0=忽略 1=待审 10=审批中 11=审批通过 12=审批退回
     * @param queryBy   0=忽略 1=内容分类ID 2=标题 3=副标题 4=备注 5=内容
     * @param queryText
     * @param leftContentCate true=左连接内容分类表 false不连接
     * @return
     */
    public List<Content> findContentList (@Param ("userId") String userId, @Param ("state") int state, @Param("queryBy") int queryBy, @Param("queryText") String queryText, @Param ("leftContentCate") boolean leftContentCate);


    /**
     * 根据ID查询
     *
     * @param contentId
     * @return
     */
    public Content findContentById (@Param("contentId") String contentId);

    /**
     * 根据内容分类ID查询当前内容分类的最大值
     *
     * @param conentCateId
     * @return
     */
    public int findMaxContentSort (@Param("conentCateId") String conentCateId);

    /**
     * 保存内容分类
     *
     * @param contentCate
     */
    public void addContentCate (ContentCate contentCate);

    /**
     * @param queryBy   0=忽略 1=广告位名称 2=上级分类ID
     * @param queryText
     * @return
     */
    public List<ContentCate> findContentCateList (@Param("queryBy") int queryBy, @Param("queryText") String queryText);

    /**
     * 根据内容分类ID查询
     *
     * @param cateId
     * @return
     */
    public ContentCate findContentCateById (@Param("cateId") String cateId);

}
