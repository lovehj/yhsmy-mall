<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>角色管理</title>
</head>
<body>
    <!-- 查询区域开始 -->
    <div class="yhsmy-search">
        <div class="select">
            角色名:
            <div class="layui-inline">
                <input type="hidden" name="queryBy" value="1">
                <input class="layui-input" name="queryText" height="20px" autocomplete="off">
            </div>

            <button class="select-on layui-btn layui-btn-sm activeBtn" data-type="select">
                <i class="layui-icon"></i>
            </button>

            <button class="layui-btn layui-btn-sm icon-position-button activeBtn" id="refresh" style="float: right;" data-type="reload">
                <i class="layui-icon">ဂ</i>
            </button>
        </div>
    </div>
    <!-- 查询区域结束 -->

    <!-- 数据表格区域开始 -->
    <div class="layui-col-md12-ext" style="height:40px;margin-top:3px;">
        <div class="layui-btn-group">
            <@shiro.hasPermission name="role:edit">
                <button class="layui-btn layui-btn-normal activeBtn" data-type="add">
                    <i class="layui-icon">&#xe608;</i>新增
                </button>
                <button class="layui-btn layui-btn-normal activeBtn" data-type="edit">
                    <i class="layui-icon">&#xe642;</i>编辑
                </button>
            </@shiro.hasPermission>
        </div>
    </div>

    <table id="roleList" class="layui-hide" lay-filter="role"/>

    <script type="text/html" id="roleToolBar">
        <@shiro.hasPermission name="role:edit">
            <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="detail">查看</a>
            <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="edit">编辑</a>
        </@shiro.hasPermission>

        <@shiro.hasPermission name="role:delete">
            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete">删除</a>
        </@shiro.hasPermission>
    </script>
    <!-- 数据表格区域结束 -->
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/js/page/role.js"></script>
</html>