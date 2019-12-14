package com.yhsmy.service.sys;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.User;

/**
 * @auth 李正义
 * @date 2019/12/10 13:32
 **/
public interface LogServiceI {

    /**
     * 获取日志列表数据
     *
     * @param queryParams 查询条件
     * @return
     */
    public DataGrid listDate (QueryParams queryParams);

    /**
     * 删除日志
     *
     * @param id       日志ID
     * @param operator 当前操作的用户
     * @return
     */
    public Json delete (String id, User operator);

}
