package com.yhsmy.mapper.sys;

import com.yhsmy.entity.vo.sys.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/26 17:44
 **/
public interface MessageMapper {

    public void addMessage (Message message);

    /**
     * @param userId    用户ID
     * @param position  -1=忽略 0=顶部位置 1=底部位置
     * @param flag      -1=忽略 0=未读 1=已读
     * @param queryBy   0=忽略 1=消息标题 2=消息内容 3=处理地址
     * @param queryText
     * @return List
     */
    public List<Message> findMessageList (@Param("userId") String userId, @Param("position") int position, @Param("flag") int flag, @Param("queryBy") int queryBy, @Param("queryText") String queryText);

    /**
     * 根据Id查询消息
     *
     * @param id 消息ID
     * @return
     */
    public Message findMessageById (@Param("id") String id);

    /**
     * 获取一条最新的未读消息
     *
     * @param userId ""=忽略
     * @return
     */
    public Message findLatest (@Param("userId") String userId);
}
