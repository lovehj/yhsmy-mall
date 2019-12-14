package com.yhsmy.service.sys;

import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.User;

import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/9 23:09
 **/
public interface UserServcieI {

    /**
     * 按查询条件获取数据
     *
     * @param state       1=正常 2 =冻结。参数来源于NormalSateEnum
     * @param queryParams 查询条件
     * @return
     */
    public DataGrid getListData (int state, QueryParams queryParams);

    /**
     * @param ctype     0=用户名 1=手机 2=邮箱 3=微信公众号的 openId
     * @param queryText
     * @return
     */
    public User getUserByLogin (int ctype, String queryText);

    /**
     * 获取用户表单
     *
     * @param id        用户ID
     * @param otherInfo true=获取用户角色、部门等信息
     * @return
     */
    public Map<String, Object> getForm (String id, boolean otherInfo);

    /**
     * 修改用户信息
     *
     * @param editUser     用户信息
     * @param operatorUser 当前操作的用户
     * @return 操作的结果
     */
    public Json formSubmit (User editUser, User operatorUser);

    /**
     * 删除用户
     *
     * @param id           用户ID
     * @param operatorUser 当前操作的用户
     * @return
     */
    public Json delete (String id, User operatorUser);

    /**
     * 更新密码
     *
     * @param id             更新的ID
     * @param originalPasswd 原始密码
     * @param newPasswd      新密码
     * @param operatorUser   当前操作的用户
     * @return
     */
    public Json updatePasswd (String id, String originalPasswd, String newPasswd, User operatorUser);

    /**
     * @param id           更新的ID
     * @param operatorUser 当前操作的用户
     * @return
     */
    public Json updateStatus (String id, User operatorUser);

}
