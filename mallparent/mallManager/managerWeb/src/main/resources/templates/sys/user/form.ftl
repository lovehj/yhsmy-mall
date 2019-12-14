<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>用户编辑</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
</head>
<body>
    <div class="x-body">
        <form class="layui-form layui-form-pane" style="margin-left: 20px;">
            <input type="hidden" id="userPhoto" name="photo" value="${user.photo!}">
            <input type="hidden" id="userPhotoId" name="fileLibId" lay-verify="userPhotoId">
            <input type="hidden" name="id" value="${user.id}">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">头像<#if !userView>上传<#else>信息</#if></legend>
                </fieldset>
                <div class="layui-input-inline">
                    <#if !userView>
                        <div class="layui-upload-drag" style="margin-left:10%;" id="userUpload">
                            <i style="font-size:30px;" class="layui-icon"></i>
                            <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>
                        </div>
                    </#if>
                </div>

                <div class="layui-input-inline">
                    <div id="userPhotoPreview" style="margin-top: 20px;margin-left: 50px">
                        <img src="<#if user.photo?? && user.photo?length gt 5>${user.photo!}<#else>/images/default_photo_1.png</#if>" width="130px" height="130px" class="layui-upload-img layui-circle">
                    </div>
                </div>
            </div>

            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">基础信息</legend>
                </fieldset>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label" for="username">用户名</label>
                <div class="layui-input-inline">
                    <input id="username" name="username" <#if user.id?length gt 5>readonly="readonly"</#if> value="${user.username!}" lay-verify="username" autocomplete="off" class="layui-input" >
                </div>
            </div>

            <#if user.id?? && user.id?length gt 5>
            <#else >
                <div class="layui-form-item">
                    <label class="layui-form-label" for="username">密码</label>
                    <div class="layui-input-inline">
                        <input type="password" id="password" name="password"  lay-verify="password" autocomplete="off" class="layui-input" >
                    </div>
                </div>
            </#if>

            <div class="layui-form-item">
                <label class="layui-form-label" for="username">真实姓名</label>
                <div class="layui-input-inline">
                    <input id="realName" name="realName" value="${user.realName!}" lay-verify="realName" autocomplete="off" class="layui-input" >
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label" for="username">手机号</label>
                <div class="layui-input-inline">
                    <input id="mobile" name="mobile" value="${user.mobile!}" lay-verify="mobile" autocomplete="off" class="layui-input" >
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label" for="username">邮箱</label>
                <div class="layui-input-inline">
                    <input id="email" name="email" value="${user.email!}" lay-verify="email" autocomplete="off" class="layui-input" >
                </div>
            </div>

            <div class="layui-form-item" id="departDiv">
                <label for="userDepartName" class="layui-form-label">所在部门</label>
                <div class="layui-input-inline">
                    <input type="hidden" name="departId" id="userDepartId" value="${user.departId!}" lay-verify="userDepartId">
                    <input id="userDepartName" class="layui-input" onclick="showUserDepartTree()" value="${user.departName!}" <#if userView>readonly="readonly"</#if> autocomplete="off">
                </div>
                <div id="userDepartTreeNode" class="menuTree">
                    <div id="userDepartTree"></div>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">拥有角色</label>
                <div class="layui-input-block">
                    <#list roleDataList as role>
                        <input type="radio" name="roleId" title="${role.roleName!}" lay-filter="check" value="${role.roleId!}" <#if role.roleId == user.roleId>checked</#if>>
                    </#list>
                </div>
            </div>

            <div class="form-submit-btn-group">
                <div class="layui-form-item">
                        <#if !userView>
                            <@shiro.hasPermission name="user:edit">
                                <button  class="layui-btn layui-btn-normal" lay-filter="userEdit" lay-submit>保存</button>
                            </@shiro.hasPermission>
                        </#if>
                    <button  class="layui-btn layui-btn-primary" id="userClose">取消</button>
                </div>
            </div>

        </form>
    </div>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(["form", "layer","upload"],function () {
        var form = layui.form, layer = layui.layer, upload = layui.upload;

        initSelectTree(layui,'userDepartTree',${departDataList},'userDepartId','userDepartName');

        var userId = $('[name="id"]').val();
        form.verify({
            userPhotoId: function (v) {
                if((userId == '' || userId == null) && v.trim() == '') {
                    return '请上传头像!';
                }
            },

            username:function (v) {
                if((userId == '' || userId == null) && v.trim() == '') {
                    return '请填写登录的用户名!';
                }
            },
            password:function (v) {
                if((userId == '' || userId == null) && v.trim() == '') {
                    return '请填写密码!';
                }
            },
            realName:function (v) {
                if(v.trim() == '' || v.length < 2 || v.length > 20) {
                    return '真实姓名控制在2-20个汉字内！';
                }
            },
            mobile:function (v) {
                if(v.trim() == '' || !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(v))){
                    return '手机号格式不正确！';
                }
            },
            email:function (v) {
                if(v.trim() == '' ||!/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(v)) {
                    return '邮箱格式不正确!';
                }
            },
            userDepartId:function (v) {
                if(v.trim() == '' || v.length < 2) {
                    return '请选择用户所在部门!';
                }
            },
            check:function (v) {
                if(v.trim() == '' || v.length < 2) {
                    return '请选择用户拥有的角色!';
                }
            }
        })

        form.on("submit(userEdit)", function (data) {
            postAjax('/user/edit',data.field,'userList');
            return false;
        })

        $('#userClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });


        upload.render({
            elem:'#userUpload',
            url:'/file/upload',
            before: function (obj) {
                //预读，不支持ie8
                obj.preview(function(index, file, result){
                    var userUpload = $('#userPhotoPreview');
                    userUpload.find('img').remove();
                    userUpload.append('<img src="'+ result +'" alt="'+ file.name +'" width="130px" height="130px" class="layui-upload-img layui-circle">');
                });
            },
            done:function (res) {
                if(res.status == 200) {
                    $('#userPhoto').val(res.obj.filePath);
                    $('#userPhotoId').val(res.obj.id);
                } else {
                    layer.msg(res.msg,{icon: 5,anim: 6});
                }
            }
        });

        form.render();
    })


    function showUserDepartTree() {
        var p = $('#userDepartName'), cityObj = p, cityObjOffset = p.offset(), width = p.css('width');
        $('#userDepartTreeNode').css({
            left: cityObjOffset.left + 'px',
            top: cityObjOffset.top + cityObj.outerHeight() + 'px',
            width: width,
            border: '1px solid #e6e6e6'
        }).slideDown('fast');
        $('body').bind('mousedown', onBodyDown);
        $('#userDepartTreeNode').css('display', 'inline');
    }

    function onBodyDown() {
        if (!(event.target.id == 'userDepartTreeNode' || $(event.target).parents('#userDepartTreeNode').length > 0)) {
            hideUserDepartTree();
        }
    }

    function hideUserDepartTree() {
        $('#userDepartTreeNode').fadeOut('fast');
        $('body').unbind('blur', onBodyDown);
    }
</script>
</html>