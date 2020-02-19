package com.yhsmy.service.mall;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.mall.Content;
import com.yhsmy.entity.vo.mall.ContentCate;
import com.yhsmy.entity.vo.sys.User;

import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2020/1/5 19:56
 **/
public interface ContentServiceI {

    /**
     * 获取内容列表
     * @param state
     * @param queryParams
     * @return
     */
    public DataGrid getContentList (int state, QueryParams queryParams, User user);

    /**
     * 获取内容表单
     *
     * @param contentId
     * @return
     */
    public Map<String, Object> getContentForm (String contentId);

    /**
     * 保存内容
     *
     * @param content
     * @param user
     * @return
     */
    public Json contentFormSubmit (Content content, User user);

    /**
     * 删除内容
     *
     * @param contentId
     * @param user
     * @return
     */
    public Json contentDelete (String contentId, User user);

    /**
     * 内容分类列表
     *
     * @param queryParams
     * @return
     */
    public List<ContentCate> getContentCateList (QueryParams queryParams);

    /**
     * 内容分类表单
     *
     * @param contentCateId
     * @return
     */
    public Map<String, Object> getContentCateForm (String contentCateId);

    /**
     * 保存/更新内容分类
     *
     * @param contentCate
     * @param user
     * @return
     */
    public Json contentCateFormSubmit (ContentCate contentCate, User user);

    /**
     * 根据内容分类ID删除
     *
     * @param contentCateId
     * @param user
     * @return
     */
    public Json contentCateDelete (String contentCateId, User user);


}
