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
                    <select name="queryBy" >
                        <option value="1" selected>操作人</option>
                        <option value="2">操作类型</option>
                        <option value="3">操作来源</option>
                    </select>
                </div>

                <div class="layui-input-inline">
                    <input class="layui-input" name="queryText" autocomplete="off">
                </div>

                <div class="layui-input-inline">
                    <select name="queryDate" >
                        <option value="1" selected>操作时间</option>
                    </select>
                </div>

                <div class="layui-input-inline">
                    <input class="layui-input" id="startDate" name="startDate" autocomplete="off">
                </div>

                <div class="layui-input-inline">
                    <input class="layui-input" id="endDate" name="endDate" autocomplete="off">
                </div>

                <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="logSelect">
                    <i class="layui-icon"></i>
                </a>

                <a class="layui-btn layui-btn-sm icon-position-button activeBtn" id="logRefresh" style="float: right;" data-type="reload">
                    <i class="layui-icon">ဂ</i>
                </a>

            </div>
        </form>
    </div>


    <!-- 数据区域 -->
    <div class="layui-form-item">
        <table id="logList" class="layui-hide" lay-filter="log"/>

        <script type="text/html" id="logToolBar">
            <@shiro.hasPermission name="log:delete">
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="logDelete">删除</a>
            </@shiro.hasPermission>
        </script>
    </div>
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/log.js"></script>
</html>