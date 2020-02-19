package com.yhsmy.service.mall.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.file.FileLib;
import com.yhsmy.entity.vo.mall.Content;
import com.yhsmy.entity.vo.mall.ContentCate;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.ContentCateTypeEnum;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.file.FileLibMapper;
import com.yhsmy.mapper.mall.ContentMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.file.FileLibServiceI;
import com.yhsmy.service.mall.ContentServiceI;
import com.yhsmy.util.FastdfsClientUtil;
import com.yhsmy.utils.FastJsonUtil;
import com.yhsmy.utils.UUIDUtil;
import io.netty.util.internal.StringUtil;
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
 * @date 2020/1/5 20:04
 **/
@Service
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentServiceI {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Autowired
    private FileLibServiceI fileLibServiceI;

    @Autowired
    private FastdfsClientUtil fastdfsClientUtil;

    @Override
    public DataGrid getContentList (int state, QueryParams queryParams, User user) {
        String userId = user.getId ();
        if (user.getCtype () == 1) {
            userId = "";
        }
        Page<Content> page = PageHelper.startPage (queryParams.getPageNo (), queryParams.getPageSize ());
        List<Content> list = contentMapper.findContentList (userId, state, queryParams.getQueryBy (), queryParams.getQueryText (), true);
        return new DataGrid (page.getTotal (), list);
    }

    @Override
    public Map<String, Object> getContentForm (String contentId) {
        Content content = null;
        if (StringUtils.isNotEmpty (contentId)) {
            content = contentMapper.findContentById (contentId);
            if (content == null) {
                throw new PageNotFoundException ();
            }
            ContentCate cate = contentMapper.findContentCateById (content.getConentCateId ());
            if (cate == null) {
                throw new PageNotFoundException ("内容分类未找到!");
            }
            content.setContentCateName (cate.getCateName ());
        } else {
            content = new Content ();
            content.setContentId ("");
            content.setConentCateId ("");
            content.setTitle ("");
            content.setSubTitle ("");
            content.setLinkUrl ("");
            content.setContent ("");
            content.setPicUrl ("");
            content.setDescription ("");
            content.setContentCateName ("");
        }
        Map<String, Object> result = new HashMap<> (1);
        result.put ("_content", content);
        result.put ("contentCateList", FastJsonUtil.listToJSONArrayString (this.getContentCateList (new QueryParams (-1, ""))));
        result.put ("prefix", fastdfsClientUtil.getFdfsWebUrlPrefix () + "/");
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public Json contentFormSubmit (Content content, User user) {
        String cname = user.getRealName (), contentId = content.getContentId (), tableName = "smy_content";
        LocalDateTime now = LocalDateTime.now ();
        if (StringUtils.isNotBlank (contentId)) {
            String stateStr = NormalEnum.NORMAL.getKey () + "," + NormalEnum.AUDIT_BACK.getKey () + NormalEnum.DELETE.getKey ();
            UpdateMap updateMap = new UpdateMap (tableName);
            updateMap.addField ("conentCateId", content.getConentCateId ());
            updateMap.addField ("title", content.getTitle ());
            updateMap.addField ("subTitle", content.getSubTitle ());
            updateMap.addField ("description", content.getDescription ());
            if (StringUtils.isNotBlank (content.getLinkUrl ())) {
                updateMap.addField ("linkUrl", content.getLinkUrl ());
            }
            if (StringUtils.isNotBlank (content.getPicUrl ())) {
                updateMap.addField ("picUrl", content.getPicUrl ());
            }
            updateMap.addField ("contentSort", this.getContentSort (content.getConentCateId ()));
            updateMap.addField ("content", content.getContent ());
            updateMap.addField ("modifyer", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("contentId", contentId);
            updateMap.addWhere ("state", stateStr, "in");
            if (this.mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            contentId = UUIDUtil.generateUUID ();
            content.setContentId (contentId);
            content.setUserId (user.getId ());
            content.setContentSort (this.getContentSort (content.getConentCateId ()));
            content.setState (NormalEnum.NORMAL.getKey ());
            content.setCreator (cname);
            content.setCreateTime (now);
            content.setModifyer (cname);
            content.setModifyTime (now);
            contentMapper.addContent (content);

        }
        fileLibServiceI.updateTableInfo (content.getContentPicId (), contentId, tableName);
        return Json.ok ();
    }

    synchronized private int getContentSort (String conentCateId) {
        int maxNum = contentMapper.findMaxContentSort (conentCateId);
        if (maxNum == 0) {
            return 1;
        }
        return maxNum++;
    }

    @Override
    @Transactional(readOnly = false)
    public Json contentDelete (String contentId, User user) {
        String userId = user.getId ();
        if (user.getCtype () == 1) {
            userId = "";
        }
        String cname = user.getRealName (), stateStr = NormalEnum.NORMAL.getKey () + "," + NormalEnum.AUDIT_BACK.getKey () + NormalEnum.DELETE.getKey ();
        LocalDateTime now = LocalDateTime.now ();
        UpdateMap updateMap = new UpdateMap ("smy_content");
        updateMap.addField ("state", NormalEnum.DELETE.getKey ());
        updateMap.addField ("modifyer", cname);
        updateMap.addField ("modifyTime", now);
        updateMap.addWhere ("contentId", contentId);
        updateMap.addWhere ("state", stateStr, "in");
        if (StringUtils.isNotBlank (userId)) {
            updateMap.addWhere ("userId", userId);
        }
        if (this.mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    public List<ContentCate> getContentCateList (QueryParams queryParams) {
        List<ContentCate> list = contentMapper.findContentCateList (queryParams.getQueryBy (), queryParams.getQueryText ());
        if (list == null) {
            return new ArrayList<> ();
        }
        return list.stream ().filter (cc -> StringUtils.isBlank (cc.getCatePid ())) //
                .map (cc -> this.combContentCate (cc, list)) //
                .collect (Collectors.toList ());
    }

    /**
     * 组合上下级关系
     *
     * @param cc   当前根节点
     * @param list 所有节点的数据集合
     * @return
     */
    private ContentCate combContentCate (ContentCate cc, List<ContentCate> list) {
        for (ContentCate cate : list) {
            if (StringUtils.trim (cc.getCateId ()).equalsIgnoreCase
                    (StringUtils.trim (cate.getCateId ())) ||
                    cc.getChildren ().contains (cate)) {
                continue;
            }
            if (cc.getCateId ().equalsIgnoreCase (cate.getCatePid ())) {
                cc.getChildren ().add (cate);
            }
        }
        return cc;
    }

    @Override
    public Map<String, Object> getContentCateForm (String contentCateId) {
        ContentCate cate = null;
        if (StringUtils.isNotBlank (contentCateId)) {
            cate = contentMapper.findContentCateById (contentCateId);
            if (cate == null) {
                throw new PageNotFoundException ();
            }
            if (StringUtils.isNotBlank (cate.getCatePid ())) {
                ContentCate pcate = contentMapper.findContentCateById (cate.getCatePid ());
                if (pcate == null) {
                    throw new PageNotFoundException ("上级分类未找到!");
                }
                cate.setCatePname (pcate.getCateName ());
            }
        } else {
            cate = new ContentCate ();
            cate.setCatePname ("");
        }
        Map<String, Object> result = new HashMap<> (2);
        result.put ("cate", cate);
        result.put ("cateType", ContentCateTypeEnum.getList ());
        result.put ("cateTree", FastJsonUtil.listToJSONArrayString (getContentCateList (new QueryParams (-1, ""))));
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public Json contentCateFormSubmit (ContentCate contentCate, User user) {
        String cateId = contentCate.getCateId (), cname = user.getRealName ();
        List<Content> contentList = contentMapper.findContentList ("", 0, 1, cateId, false);
        if (!contentList.isEmpty () && contentList.size () > 0) {
            return Json.fail ("该分类下有未删除的内容,请先删除分类内容!");
        }
        LocalDateTime now = LocalDateTime.now ();
        if (StringUtils.isNotBlank (cateId)) {
            UpdateMap updateMap = new UpdateMap ("smy_content_cate");
            updateMap.addField ("cateName", contentCate.getCateName ());
            updateMap.addField ("ctype", contentCate.getCtype ());
            updateMap.addField ("catePid", contentCate.getCatePid ());
            updateMap.addField ("modifyer", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("cateId", cateId);
            if (mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            contentCate.setCateId (UUIDUtil.generateUUID ());
            contentCate.setState (NormalEnum.NORMAL.getKey ());
            contentCate.setCreator (cname);
            contentCate.setCreateTime (now);
            contentCate.setModifyer (cname);
            contentCate.setModifyTime (now);
            contentMapper.addContentCate (contentCate);
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    public Json contentCateDelete (String contentCateId, User user) {
        String cname = user.getRealName ();
        List<Content> contentList = contentMapper.findContentList ("", 1, 0, contentCateId, false);
        if (!contentList.isEmpty () && contentList.size () > 0) {
            return Json.fail ("该分类下有未删除的内容,请先删除分类内容!");
        }
        LocalDateTime now = LocalDateTime.now ();
        UpdateMap updateMap = new UpdateMap ("smy_content_cate");
        updateMap.addField ("state", NormalEnum.DELETE.getKey ());
        updateMap.addField ("modifyer", cname);
        updateMap.addField ("modifyTime", now);
        updateMap.addWhere ("cateId", contentCateId);
        if (mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }
}
