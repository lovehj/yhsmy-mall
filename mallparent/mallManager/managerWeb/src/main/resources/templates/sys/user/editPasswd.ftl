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
        <input type="hidden" name="id" value="${user.id}">
        <div class="layui-form-item">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                <legend style="font-size:16px;">被修改的用户：${user.realName}</legend>
            </fieldset>
        </div>

        <div class="layui-form-item">
            <label for="pass" class="layui-form-label">
                <span class="x-red">*</span>原密码
            </label>
            <div class="layui-input-inline">
                <input type="password"  id="pass" name="originalPasswd"  lay-verify="pass" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label for="newPass" class="layui-form-label">
                <span class="x-red">*</span>新密码
            </label>
            <div class="layui-input-inline">
                <input type="password" id="newPasswd" name="newPasswd" lay-verify="newPasswd"  autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label for="newPass" class="layui-form-label">
                <span class="x-red">*</span>确认密码
            </label>
            <div class="layui-input-inline">
                <input type="password" id="confirmPsswd" name="confirmPsswd" lay-verify="confirmPsswd"  autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="form-submit-btn-group">
            <div class="layui-form-item">
                <@shiro.hasPermission name="user:editPasswd">
                    <button class="layui-btn layui-btn-normal" lay-filter="userEditPasswd" lay-submit>保存</button>
                </@shiro.hasPermission>
                <button class="layui-btn layui-btn-primary" id="userEditPasswdClose">取消</button>
            </div>
        </div>

    </form>
</div>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(['form','layer'], function(){
        var form = layui.form,
                layer = layui.layer;
        form.verify({
            pass:function (v) {
                if(v.trim() == '') {
                    return '原始密码不能为空!';
                }
            },

            newPasswd: [/(.+){6,12}$/, '密码必须6到12位'],

            confirmPsswd:function (v) {
                if(v.trim() == '') {
                    return '确认新密码必填!';
                }
                if($('#newPasswd').val() != $('#confirmPsswd').val()) {
                    return '两次密码不一致';
                }
            }

        });

        $('#userEditPasswdClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });

        form.on('submit(userEditPasswd)',function (data) {
            postAjax('/user/editPasswd',data.field,'userList');
            return false;
        })

        form.render();
    });
</script>
</html>