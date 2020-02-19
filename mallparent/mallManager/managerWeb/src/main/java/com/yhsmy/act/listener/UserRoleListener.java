package com.yhsmy.act.listener;

import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.sys.Role;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.mapper.sys.RoleMapper;
import com.yhsmy.mapper.sys.UserMapper;
import com.yhsmy.service.sys.UserServcieI;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/14 11:02
 **/
@Aspect
@Component
public class UserRoleListener {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;



    @Around ("execution(* com.yhsmy.web.sys.UserController.formSubmit(..))")
    public Object editUserListener(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object object = proceedingJoinPoint.proceed (proceedingJoinPoint.getArgs ());
        Object[] args = proceedingJoinPoint.getArgs ();
        Json json = (Json) object;
        if(Json.SUC_CODE == json.getStatus ()) {
            // 更新activiti中的用户信息
            User user = (User) args[0];
            String userId = user.getId (), roleId = user.getRoleId ();
            identityService.deleteUser (userId);

            org.activiti.engine.identity.User act_user = new UserEntity ();
            act_user.setId (userId);
            act_user.setFirstName (user.getRealName ());
            act_user.setEmail (user.getEmail ());
            identityService.saveUser (act_user);

            identityService.deleteGroup (roleId);
            Role role = roleMapper.findRoleById (roleId);
            Group group = new GroupEntity ();
            group.setId (roleId);
            group.setName (role.getRoleName ());
            identityService.saveGroup (group);

            // 更新activiti中用户的角色信息
            identityService.deleteMembership (userId, roleId);
            identityService.createMembership (userId, roleId);
        }
        return object;
    }

    @Around ("execution(* com.yhsmy.web.sys.UserController.delete(..))")
    public Object deleteUserListener(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed (proceedingJoinPoint.getArgs ());
        Json json = (Json) object;
        if(Json.SUC_CODE == json.getStatus ()) {
            Object args = proceedingJoinPoint.getArgs ()[0];
            String userId = (String)args;
            identityService.deleteUser (userId);
            User user = userMapper.findUserById (userId);
            if(StringUtils.isNotBlank (user.getRoleId ())) {
                Role role = roleMapper.findRoleById (userId);
                identityService.deleteMembership (userId, role.getRoleId ());
            }
        }
        return object;
    }

    @Around ("execution(com.yhsmy.entity.Json com.yhsmy.web.sys.RoleController.delete(..))")
    public Object deleteRoleListener(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed (proceedingJoinPoint.getArgs ());
        Json json = (Json) object;
        if(Json.SUC_CODE == json.getStatus ()) {
            Object args = proceedingJoinPoint.getArgs ()[0];
            String roleId = (String)args;
            List<User> userList = userMapper.findUserList (-1, 4, roleId);
            if(userList != null && userList.size () > 0) {
                for(User user : userList) {
                    identityService.deleteMembership (user.getId (), roleId);
                }
            }
            identityService.deleteGroup (roleId);
        }
        return object;
    }


}
