package com.yhsmy.service.sys.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.sys.Depart;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.sys.DepartMapper;
import com.yhsmy.mapper.sys.MenuMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.sys.DepartServiceI;
import com.yhsmy.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auth 李正义
 * @date 2019/11/29 18:28
 **/
@Service
@Transactional(readOnly = true)
public class DepartServiceImpl implements DepartServiceI {

    public static final String TABLE_DEPART = "smy_depart";

    @Autowired
    private DepartMapper departMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Override
    public List<Depart> getDepartList (boolean combRelation) {
        List<Depart> departs = departMapper.findDepartList (0, "", "");
        if(combRelation) {
           return departs.stream ()
                   .map (d -> this.combChildRelation (d))
                   .collect (Collectors.toList ());
        }
        return departs;
    }

    @Override
    public Map<String, Object> getForm (String id) {
        Map<String, Object> result = new HashMap<> ();
        Depart depart = null;
        if(StringUtils.isNotBlank (id)) {
            depart = departMapper.findDepartById (id);
            if(depart == null) {
                throw new PageNotFoundException ();
            }
            if(StringUtils.isNotBlank (depart.getPid ())) {
                Depart pDepart = departMapper.findDepartById (depart.getPid ());
                if(pDepart == null) {
                    throw new PageNotFoundException ();
                }
                result.put ("parentName", pDepart.getName ());
            }
        } else {
            depart = new Depart ();
            depart.setId ("");
            depart.setName ("");
            depart.setRemark ("");
            depart.setPName ("");
        }
        result.put ("depart", depart);
        result.put ("departTree", JSON.toJSONString (JSONArray.parseArray (JSON.toJSONString (this.getDepartList (true)))));
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public Json formSubmit (Depart depart, User user) {
        String cname = user.getRealName (),departId = depart.getId ();
        LocalDateTime now = LocalDateTime.now ();
        if(StringUtils.isNotBlank (departId)) {
            UpdateMap updateMap = new UpdateMap (TABLE_DEPART);
            updateMap.addField ("name", depart.getName ());
            updateMap.addField ("remark", depart.getRemark ());
            if(StringUtils.isNotBlank (depart.getPid ())) {
                updateMap.addField ("pid", depart.getPid ());
            }
            updateMap.addField ("modifyor", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("id", departId);
            if(this.mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            depart.setId (UUIDUtil.generateUUID ());
            depart.setState (NormalEnum.NORMAL.getKey ());
            depart.setCreator (cname);
            depart.setCreateTime (now);
            depart.setModifyor (cname);
            depart.setModifyTime (now);
            this.departMapper.addDepart (depart);
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    public Json delete (String id, User user) {
        String cname = user.getRealName ();
        LocalDateTime now = LocalDateTime.now ();
        UpdateMap delMap = new UpdateMap (TABLE_DEPART);
        delMap.addField ("state", NormalEnum.DELETE.getKey ());
        delMap.addField ("modifyor", cname);
        delMap.addField ("modifyTime", now);
        delMap.addWhere ("id", id);
        if(this.mybatisMapper.update (delMap) <= 0) {
            return Json.fail ();
        }

        // 解除部门与用户之间的关联关系，并冻结用户
        delMap = new UpdateMap ("smy_user");
        delMap.addField ("departId", "");
        delMap.addField ("state", NormalEnum.FREEZE.getKey ());
        delMap.addField ("modifyor", cname);
        delMap.addField ("modifyTime", now);
        delMap.addWhere ("departId", id);
        this.mybatisMapper.update (delMap);
        return Json.ok ();
    }

    public Depart combChildRelation(Depart depart) {
        List<Depart> departList = depart.getChildren ();
        if(departList.size () > 0) {
            for(Depart child : departList) {
                List<Depart> childList = departMapper.findDepartList (0, "", child.getId ());
                if(childList != null && childList.size () > 0) {
                    child.setChildren (childList);
                    for (Depart _child : departList) {
                        this.combChildRelation (_child);
                    }
                }
            }
        }
        return depart;
    }
}
