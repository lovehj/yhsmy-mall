package com.yhsmy.service.mall.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.file.FileLib;
import com.yhsmy.entity.vo.mall.Category;
import com.yhsmy.entity.vo.mall.Item;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.file.FileLibMapper;
import com.yhsmy.mapper.mall.CategoryMapper;
import com.yhsmy.mapper.mall.ItemMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.file.FileLibServiceI;
import com.yhsmy.service.mall.CategoryServiceI;
import com.yhsmy.service.mall.ItemServiceI;
import com.yhsmy.util.FastdfsClientUtil;
import com.yhsmy.utils.FastJsonUtil;
import com.yhsmy.utils.UUIDUtil;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/17 22:39
 **/
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemServiceI {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Autowired
    private FileLibServiceI fileLibServiceI;

    @Autowired
    private CategoryServiceI categoryServiceI;

    @Autowired
    private FastdfsClientUtil fastdfsClientUtil;

    @Override
    public DataGrid getListData (int state, QueryParams params) {
        Page<Item> page = PageHelper.startPage (params.getPageNo (), params.getPageSize ());
        List<Item> itemList = itemMapper.findItemList (state, params.getQueryBy (), params.getQueryText ());
        return new DataGrid (page.getTotal (), itemList);
    }

    @Override
    public Map<String, Object> getForm (String id) {
        Item item = null;
        if (StringUtils.isNotBlank (id)) {
            item = itemMapper.findItemById (id);
            if (item == null) {
                throw new ServiceException ("商品未找到!");
            }
            Category category = categoryMapper.findCategoryById (item.getCategoryId ());
            if (category == null) {
                throw new ServiceException ("商品分类未找到!");
            }
            item.setCateName (category.getCateName ());

        } else {
            item = new Item ();
            item.setItemId ("");
            item.setTitle ("");
            item.setSellPoint ("");
            item.setPrice ("0.00");
            item.setItemNum (0);
            item.setItemImg ("");
            item.setCategoryId ("");
            item.setCateName ("");
        }
        Map<String, Object> result = new HashMap<> (1);
        result.put ("item", item);
        result.put ("categoryList", FastJsonUtil.listToJSONArrayString (categoryServiceI.getCategoryRelation (new QueryParams (-1, ""))));
        result.put ("prefix", fastdfsClientUtil.getFdfsWebUrlPrefix () + "/");
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public Json formSubmit (Item item, User user) {
        String cname = user.getRealName (), id = item.getItemId (), tableName = "smy_item";
        LocalDateTime now = LocalDateTime.now ();
        if (StringUtils.isNotBlank (id)) {
            UpdateMap updateMap = new UpdateMap (tableName);
            updateMap.addField ("title", item.getTitle ());
            updateMap.addField ("sellPoint", item.getSellPoint ());
            updateMap.addField ("price", item.getPrice ());
            updateMap.addField ("disPrice", item.getDisPrice ());
            if(item.getItemNum () > 0) {
                updateMap.addField ("itemNum", item.getItemNum ());
                // 商品数量要大于库存余量
                updateMap.addWhere ("leftItemNum", item.getItemNum (), "<=");
            }
            updateMap.addField ("itemImg", item.getItemImg ());
            updateMap.addField ("categoryId", item.getCategoryId ());
            updateMap.addField ("modifyor", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("itemId", id);
            if (mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            item.setItemId (UUIDUtil.generateUUID ());
            item.setUserId(user.getId());
            item.setLeftItemNum (item.getItemNum ());
            item.setState (NormalEnum.NORMAL.getKey ());
            item.setCreator (cname);
            item.setModifyor (cname);
            item.setCreateTime (now);
            item.setModifyTime (now);
            itemMapper.addItem (item);
            id = item.getItemId ();
        }

        // 更新附件表信息
        fileLibServiceI.updateTableInfo (item.getItemImgId (), id, tableName);
        return Json.ok ();
    }

    /**
     * @param ctype 0=删除 1=正常/上架 2=冻结 3=下架商品
     * @param id
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Json delete (int ctype, String id, User user) {
        if (StringUtils.isEmpty (NormalEnum.getValueByKey (ctype))) {
            ctype = NormalEnum.NORMAL.getKey ();
        }
        String cname = user.getRealName ();
        String stateStr = NormalEnum.NORMAL.getKey ()+","+NormalEnum.AUDIT_PASS.getKey ()+","+NormalEnum.AUDIT_BACK.getKey ();
        LocalDateTime now = LocalDateTime.now ();
        UpdateMap delMap = new UpdateMap ("smy_item");
        delMap.addField ("state", ctype);
        delMap.addField ("modifyor", cname);
        delMap.addField ("modifyTime", now);
        delMap.addWhere ("itemId", id);
        if(ctype == 0) { // 删除时只能删除正常、审批通过、审批退回的商品
            delMap.addWhere ("state", stateStr, "in");
        }
        if (mybatisMapper.update (delMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }
}
