<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>待办任务管理</title>
</head>
<body>
<!-- 查询区域开始 -->
<div class="layui-form-item">
    <form class="layui-form">
        <div class="layui-form-item search-form-margin">
            <div class="layui-input-inline">
                <select name="queryBy">
                    <option value="0" selected>不限</option>
                    <option value="1">请假审批</option>
                    <option value="2">商品审批</option>
                </select>
            </div>

            <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="waitSelect">
                <i class="layui-icon"></i>
            </a>

            <a class="layui-btn layui-btn-sm icon-position-button activeBtn" style="float: right;" data-type="waitReload">
                <i class="layui-icon">ဂ</i>
            </a>
        </div>
    </form>
</div>
<!-- 查询区域结束 -->

<!-- 数据表格区域开始 -->
<div class="layui-form-item">
    <table id="waitTaskList" class="layui-hide" lay-filter="waitTask"/>

    <script type="text/html" id="waitTaskToolBar">
        <@shiro.hasPermission name="wait:complete">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="waitTaskHandler">办理</a>
        </@shiro.hasPermission>

        <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="waitTaskDetail">审批详情</a>
    </script>
</div>

<!-- 数据表格区域结束 -->
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/waitTask.js"></script>
</html>