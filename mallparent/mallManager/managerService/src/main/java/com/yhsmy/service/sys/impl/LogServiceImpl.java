package com.yhsmy.service.sys.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Log;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.mapper.sys.LogMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.sys.LogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/10 13:32
 **/
@Service
@Transactional(readOnly = true)
public class LogServiceImpl implements LogServiceI {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Override
    public DataGrid listDate (QueryParams params) {
        Page<Log> page = PageHelper.startPage (params.getPageNo (), params.getPageSize ());
        List<Log> logList = logMapper.findLogList (params.getQueryBy (), params.getQueryText (), params.getQueryDate (), params.getStartDate (), params.getEndDate ());
        return new DataGrid (page.getTotal (), logList);
    }

    @Override
    @Transactional(readOnly = false)
    public Json delete (String id, User operator) {
        UpdateMap updateMap = new UpdateMap ("smy_log");
        updateMap.addField ("state", NormalEnum.DELETE.getKey ());
        updateMap.addWhere ("logId", id);
        if (mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }
}
