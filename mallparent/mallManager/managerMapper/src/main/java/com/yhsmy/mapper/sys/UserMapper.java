package com.yhsmy.mapper.sys;

import com.yhsmy.entity.vo.sys.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/9 22:55
 **/
public interface UserMapper {

    public void addUser (User user);

    /**
     * @param ctype     0=用户名 1=手机 2=邮箱 3=微信公众号的 openId
     * @param queryText
     * @return
     */
    public User findUserByLogin (@Param("ctype") int ctype, @Param("queryText") String queryText);


    /**
     * 根据ID查询对象
     *
     * @param id
     * @return
     */
    public User findUserById (@Param("id") String id);

    /**
     * @param state 0=删除 1=正常 2 =冻结。参数来源于NormalSateEnum
     * @param queryBy 0=用户名 1=手机 2=邮箱 3=微信公众号的 openId
     * @param queryText
     * @return
     */
    public List<User> findUserList(@Param ("state") int state, @Param("queryBy")  int queryBy, @Param("queryText")  String queryText);

}
