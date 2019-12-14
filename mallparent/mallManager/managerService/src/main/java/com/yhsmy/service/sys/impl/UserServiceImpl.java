package com.yhsmy.service.sys.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.IConstant;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.file.FileLib;
import com.yhsmy.entity.vo.sys.Depart;
import com.yhsmy.entity.vo.sys.Role;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.file.FileLibMapper;
import com.yhsmy.mapper.sys.DepartMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.mapper.sys.RoleMapper;
import com.yhsmy.mapper.sys.UserMapper;
import com.yhsmy.service.sys.DepartServiceI;
import com.yhsmy.service.sys.UserServcieI;
import com.yhsmy.util.FastdfsClientUtil;
import com.yhsmy.utils.FastJsonUtil;
import com.yhsmy.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/9 23:12
 **/
@Service
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserServcieI {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartMapper departMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Autowired
    private FileLibMapper fileLibMapper;

    @Autowired
    private DepartServiceI departServiceI;

    @Autowired
    private FastdfsClientUtil fastdfsClientUtil;

    public static final String TABLE_USER = "smy_user";

    public static final String CACHE_KEY = "user";

    @Override
    public DataGrid getListData (int state, QueryParams params) {
        Page<User> page = PageHelper.startPage (params.getPageNo (), params.getPageSize ());
        List<User> userList = userMapper.findUserList (state, params.getQueryBy (), params.getQueryText ());
        return new DataGrid (page.getTotal (), userList);
    }

    @Override
    @Cacheable(value = CACHE_KEY, condition = "#ctype>-1 and #queryText != 'null'")
    public User getUserByLogin (int ctype, String queryText) {
        User user = userMapper.findUserByLogin (ctype, queryText);
        if(user != null && StringUtils.isNotBlank (user.getPhoto ().trim ())) {
            user.setPhoto (fastdfsClientUtil.getFdfsWebUrlPrefix ()+"/"+user.getPhoto ());
        }
        return user;
    }

    @Override
    public Map<String, Object> getForm (String id, boolean otherInfo) {
        Map<String, Object> result = new HashMap<> (1);
        User user = null;
        if (StringUtils.isNotBlank (id)) {
            user = userMapper.findUserById (id);
            if (user == null) {
                throw new PageNotFoundException ();
            }
            if (otherInfo && StringUtils.isNotBlank (user.getRoleId ())) {
                Role role = roleMapper.findRoleById (user.getRoleId ());
                if (role != null) {
                    user.setRoleName (role.getRoleName ());
                }
            }

            if (otherInfo && StringUtils.isNotBlank (user.getDepartId ())) {
                Depart depart = departMapper.findDepartById (user.getDepartId ());
                if (depart != null) {
                    user.setDepartName (depart.getName ());
                }
            }

            if(otherInfo && StringUtils.isNotBlank (user.getPhoto ().trim ())) {
                user.setPhoto (fastdfsClientUtil.getFdfsWebUrlPrefix ()+"/"+user.getPhoto ());
            }
        } else {
            user = new User ();
            user.setId ("");
            user.setUsername ("");
            user.setRealName ("");
            user.setDepartName ("");
            user.setRoleName ("");
            user.setRoleId ("");
            user.setDepartId ("");
            user.setMobile ("");
            user.setEmail ("");
            user.setPhoto ("");
        }

        // 准备部门数据
        result.put ("user", user);
        if(otherInfo) {
            result.put ("departDataList", FastJsonUtil.listToJSONArrayString (departServiceI.getDepartList (true)));
            result.put ("roleDataList", roleMapper.findRoleList (-1, ""));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    @CacheEvict(value = CACHE_KEY, allEntries = true, beforeInvocation = true)
    public Json formSubmit (User editUser, User operatorUser) {
        // 检测角色和部门是否正确
        Role role = roleMapper.findRoleById (editUser.getRoleId ());
        if (role == null || role.getState () == NormalEnum.DELETE.getKey ()) {
            return Json.fail ("角色选择不正确!");
        }
        Depart depart = departMapper.findDepartById (editUser.getDepartId ());
        if (depart == null || depart.getState () == NormalEnum.DELETE.getKey ()) {
            return Json.fail ("部门选择不正确!");
        }

        // 参数验证
        boolean isEdit = StringUtils.isNotBlank (editUser.getId ());
        User _user = null;
        if(StringUtils.isEmpty (editUser.getId ())) {
            // 验证用户名
            _user = userMapper.findUserByLogin (0, editUser.getUsername ());
            if(_user != null) {
                Json.fail ("用户名已存在!");
            }
        }

        // 验证手机号是否存在
        _user = userMapper.findUserByLogin (1, editUser.getMobile ());
        if(_user != null && ((isEdit && !_user.getId ().equals (editUser.getId ()))||!isEdit)) {
            return Json.fail ("手机号已存在!");
        }

        _user = userMapper.findUserByLogin (2, editUser.getEmail ()) ;
        if(_user != null && ((isEdit && !_user.getId ().equals (editUser.getId ()))||!isEdit)) {
            return Json.fail ("邮箱已存在!");
        }

        FileLib fileLib = fileLibMapper.findFileLibById (editUser.getFileLibId ());
        String cname = operatorUser.getRealName ();
        LocalDateTime now = LocalDateTime.now ();
        if (isEdit) {
            UpdateMap updateMap = new UpdateMap (TABLE_USER);
            updateMap.addField ("realName", editUser.getRealName ());
            updateMap.addField ("departId", editUser.getDepartId ());
            updateMap.addField ("roleId", editUser.getRoleId ());
            updateMap.addField ("mobile", editUser.getMobile ());
            updateMap.addField ("email", editUser.getEmail ());
            if (fileLib != null) {
                updateMap.addField ("photo", fileLib.getFilePath ());
            }
            updateMap.addField ("modifyor", cname);
            updateMap.addField ("modifyTime", now);
            updateMap.addWhere ("id", editUser.getId ());
            if (mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            String password = new SimpleHash (IConstant.SHIRO_SCCRITY, editUser.getUsername (),
                    editUser.getPassword (), IConstant.SHIRO_ITEAROTR).toString ();
            editUser.setPassword (password);
            editUser.setOpenId ("");
            editUser.setId (UUIDUtil.generateUUID ());
            editUser.setState (NormalEnum.NORMAL.getKey ());
            editUser.setCreator (cname);
            editUser.setCreateTime (now);
            editUser.setModifyor (cname);
            editUser.setModifyTime (now);
            userMapper.addUser (editUser);
        }
        if (fileLib != null) {
            UpdateMap updateMap = new UpdateMap ("smy_file_lib");
            updateMap.addField ("tableId", editUser.getId ());
            updateMap.addField ("tableName", TABLE_USER);
            updateMap.addWhere ("id", fileLib.getId ());
            if (this.mybatisMapper.update (updateMap) <= 0) {
                throw new ServiceException ("更新图片出错!");
            }
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    @CacheEvict(value = CACHE_KEY, allEntries = true, beforeInvocation = true)
    public Json delete (String id, User operatorUser) {
        UpdateMap delMap = new UpdateMap (TABLE_USER);
        delMap.addField ("state", NormalEnum.DELETE.getKey ());
        delMap.addField ("modifyor", operatorUser.getRealName ());
        delMap.addField ("modifyTime", LocalDateTime.now ());
        delMap.addWhere ("id", id);
        if (mybatisMapper.update (delMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    @CacheEvict(value = CACHE_KEY, allEntries = true, beforeInvocation = true)
    public Json updatePasswd (String id, String originalPasswd, String newPasswd, User operatorUser) {
        User user = userMapper.findUserById (id);
        if (user == null) {
            return Json.fail ("用户未找到!");
        }

        if(StringUtils.isNotBlank (originalPasswd)) {
            originalPasswd = new SimpleHash (IConstant.SHIRO_SCCRITY, user.getUsername (),
                    originalPasswd, IConstant.SHIRO_ITEAROTR).toString ();
            if(!user.getPassword ().equals (originalPasswd.trim ())) {
                return Json.fail ("原始密码不相等！");
            }
        }

        newPasswd = new SimpleHash (IConstant.SHIRO_SCCRITY, user.getUsername (),
                newPasswd, IConstant.SHIRO_ITEAROTR).toString ();
        UpdateMap updateMap = new UpdateMap (TABLE_USER);
        updateMap.addField ("password", newPasswd);
        updateMap.addField ("modifyor", operatorUser.getRealName ());
        updateMap.addField ("modifyTime", LocalDateTime.now ());
        updateMap.addWhere ("id", id);
        if (mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    @CacheEvict(value = CACHE_KEY, allEntries = true, beforeInvocation = true)
    public Json updateStatus (String id, User operatorUser) {
        User user = userMapper.findUserById (id);
        if (user == null) {
            return Json.fail ("用户未找到!");
        }
        int state = NormalEnum.NORMAL.getKey (), freeze = NormalEnum.FREEZE.getKey ();
        if (user.getState () != freeze) {
            state = freeze;
        }
        UpdateMap updateMap = new UpdateMap (TABLE_USER);
        updateMap.addField ("state", state);
        updateMap.addField ("modifyor", operatorUser.getRealName ());
        updateMap.addField ("modifyTime", LocalDateTime.now ());
        updateMap.addWhere ("id", id);
        if (mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }
        return Json.ok ();
    }
}
