package com.yhsmy.service.sys;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Message;
import com.yhsmy.entity.vo.sys.User;

/**
 * @auth 李正义
 * @date 2019/12/26 17:58
 **/
public interface MessageServiceI {

    /**
     * @param position    -1=忽略 0=顶部位置 1=底部位置
     * @param flag        -1=忽略 0=未读 1=已读
     * @param queryParams
     * @param user        用户ID
     * @return
     */
    public DataGrid getListData (int position, int flag, QueryParams queryParams, User user);

    /**
     * 根据ID查询消息
     *
     * @param id
     * @return
     */
    public Message getMessageById (String id);

    /**
     * 保存消息
     *
     * @param message 消息体
     * @return
     */
    public Json addMessage (Message message);

    /**
     * 读取或删除消息
     *
     * @param id    消息ID
     * @param ctype 0=读取消息 1=删除消息
     * @param user  当前操作的用户
     * @return
     */
    public Json readOrDelete (String id, int ctype, User user);

    /**
     * 获取一条最新的未读消息
     *
     * @return
     */
    public Message getPushMessage (User user);
}
