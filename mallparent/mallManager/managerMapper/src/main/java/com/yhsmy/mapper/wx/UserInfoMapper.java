package com.yhsmy.mapper.wx;

import com.yhsmy.entity.vo.wx.other.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2020/2/15 14:38
 **/
public interface UserInfoMapper {

    /**
     * 保存从微信公众号里获取到的用户信息
     *
     * @param userInfo
     */
    public void addUserInfo (UserInfo userInfo);

    /**
     * 查询用户信息
     *
     * @param queryBy   0=忽略 1=昵称 2=省 3=市
     * @param queryText
     * @return
     */
    public List<UserInfo> findUserInfoList (@Param("queryBy") int queryBy, @Param("queryText") String queryText);

    /**
     * @param openId
     * @return
     */
    public UserInfo findUserInfo (@Param("openId") String openId);


}
