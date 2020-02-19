package com.yhsmy.service.sys.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Message;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.mapper.sys.MessageMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.sys.MessageServiceI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/26 18:01
 **/
@Service
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageServiceI {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Override
    public DataGrid getListData (int postion, int flag, QueryParams queryParams, User user) {
        String userId = user.getId ();
        if (user.getCtype () == 1) {
            userId = "";
        }
        Page<Message> page = PageHelper.startPage (queryParams.getPageNo (), queryParams.getPageSize ());
        List<Message> messageList = messageMapper.findMessageList (userId, postion, flag,
                queryParams.getQueryBy (), queryParams.getQueryText ());
        return new DataGrid (page.getTotal (), messageList);
    }

    @Override
    @Cacheable(value = "messageById")
    public Message getMessageById (String id) {
        return messageMapper.findMessageById (id);
    }

    @Override
    @CacheEvict(value = {"messageList", "messageById", "pushMessage"}, allEntries = true, beforeInvocation = true)
    @Transactional(readOnly = false)
    public Json addMessage (Message message) {
        messageMapper.addMessage (message);
        return Json.ok ();
    }

    @Override
    @CacheEvict(value = {"messageList", "messageById", "pushMessage"}, allEntries = true, beforeInvocation = true)
    @Transactional(readOnly = false)
    public Json readOrDelete (String id, int ctype, User user) {
        String userId = user.getId ();
        if (user.getCtype () == 1) {
            userId = null;
        }

        UpdateMap updateMap = new UpdateMap ("smy_message");
        if (ctype == 0) {
            updateMap.addField ("flag", NormalEnum.NORMAL.getKey ());
        } else if (ctype == 1) {
            updateMap.addField ("state", NormalEnum.DELETE.getKey ());
        }

        updateMap.addWhere ("id", id);
        if (StringUtils.isNotEmpty (userId)) {
            updateMap.addWhere ("userId", userId);
        }
        if (mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    @Cacheable(value = "pushMessage", key = "#user.id")
    public Message getPushMessage (User user) {
        String userId = user.getId ();
        if (user.getCtype () == 1) {
            userId = "";
        }
        return messageMapper.findLatest (userId);
    }
}
