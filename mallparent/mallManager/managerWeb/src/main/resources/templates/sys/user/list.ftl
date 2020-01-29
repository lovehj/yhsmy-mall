<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>用户管理</title>
</head>
<body>
    <!-- 查询区域开始 -->
    <div class="layui-form-item">
        <form class="layui-form">
            <div class="layui-form-item search-form-margin">
                <div class="layui-input-inline">
                    <select name="state">
                        <option value="1" selected>正常</option>
                        <option value="2">冻结</option>
                    </select>
                </div>

                <div class="layui-input-inline">
                    <select name="queryBy">
                        <option value="0" selected>用户名</option>
                        <option value="1" >手机</option>
                        <option value="2" >邮箱</option>
                    </select>
                </div>

                <div class="layui-input-inline">
                    <input class="layui-input" name="queryText" autocomplete="off">
                </div>

                <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="select">
                    <i class="layui-icon"></i>
                </a>

                <@shiro.hasPermission name="user:edit">
                    <a class="layui-btn layui-btn-normal activeBtn" data-type="userAdd">
                        <i class="layui-icon">&#xe608;</i>新增
                    </a>
                </@shiro.hasPermission>

                <a class="layui-btn layui-btn-warm activeBtn" data-type="exportExcel">
                    <i class="layui-icon">&#xe67d;</i>导出Excel
                </a>

                <a class="layui-btn layui-btn-sm icon-position-button activeBtn" id="refresh" style="float: right;" data-type="reload">
                    <i class="layui-icon">ဂ</i>
                </a>
            </div>
        </form>

    </div>
    <!-- 查询区域结束 -->

    <!-- 数据表格区域开始 -->
    <div class="layui-form-item">
        <table id="userList" class="layui-hide" lay-filter="user"/>
    </div>

    <!-- 数据表格区域结束 -->
    <script type="text/html" id="userToolBar">
        <@shiro.hasPermission name="user:edit">
            <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="edit">查看</a>
            <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="detail">编辑</a>
        </@shiro.hasPermission>

        <@shiro.hasPermission name="user:delete">
            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
        </@shiro.hasPermission>

        <@shiro.hasPermission name="user:editPasswd">
            <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="editPasswd">修改密码</a>
            <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="initPasswd">初始化密码</a>
            <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="freezeUser">冻结/启用</a>
        </@shiro.hasPermission>
    </script>
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/user.js"></script>
</html>