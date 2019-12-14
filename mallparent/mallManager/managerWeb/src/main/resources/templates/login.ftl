<!DOCTYPE html>
<html>
<head>
    <#include "/includs/header.ftl"/>
    <link rel="stylesheet" href="/css/login.css" >
</head>
<body>
    <div class="login-form">
        <form class="layui-form layui-form-pane">
            <div class="layui-form-item">
                <label class="layui-form-label">用户名</label>
                <div class="layui-input-inline">
                    <input type="text" name="username" lay-verify="required" placeholder="用户名/手机/邮箱" autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">密&nbsp; &nbsp;码</label>
                <div class="layui-input-inline">
                    <input type="password" name="password" lay-verify="required" placeholder="密码" autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item ">
                <label class="layui-form-label">记住密码</label>
                <div class="layui-input-block">
                    <input type="checkbox" name="rememberMe" checked  lay-skin="primary">
                </div>
            </div>

            <div class="layui-form-item loginBtn">
                <div class="layui-input-inline">
                    <button class="layui-btn layui-btn-normal layui-btn-lg" lay-submit lay-filter="login">登 &nbsp; &nbsp; 录</button>
                </div>
            </div>
        </form>
    </div>

    <div class="copyright-warp">
        <#include '/includs/copyright.ftl'>
    </div>
</body>
<script src="/plugin/layui/layui.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/login.js"></script>
</html>