<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>流程列表管理</title>
</head>
<body>
<!-- 查询区域开始 -->
<div class="layui-form-item">
    <form class="layui-form">
        <div class="layui-form-item  search-form-margin">
            <div class="layui-input-inline">
                <select name="queryBy">
                    <option value="1">流程KEY</option>
                    <option value="2">流程名称</option>
                    <option value="3">流程备注</option>
                </select>
            </div>

            <div class="layui-input-inline">
                <input class="layui-input search-input-sm" name="queryText" autocomplete="off">
            </div>

            <div class="layui-input-inline">
                <select name="queryDate">
                    <option value="1">创建时间</option>
                </select>
            </div>

            <div class="layui-input-inline">
                <input class="layui-input search-input-sm" id="startDate" name="startDate" autocomplete="off">-
            </div>

            <div class="layui-input-inline">
                <input class="layui-input search-input-sm" id="endDate" name="endDate" autocomplete="off">
            </div>

            <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="modelSelect">
                <i class="layui-icon"></i>
            </a>

            <a class="layui-btn layui-btn-normal activeBtn" data-type="modelSyncdata">
                <i class="layui-icon">&#xe618;</i>同步数据
            </a>

            <a class="layui-btn layui-btn-normal activeBtn" id="processGroup" data-type="modelAdd">
                <i class="layui-icon">&#xe642;</i>新建流程
            </a>

            <a class="layui-btn layui-btn-sm icon-position-button activeBtn" id="modelRefresh" style="float: right;" data-type="modelReload">
                <i class="layui-icon">ဂ</i>
            </a>
        </div>
    </form>
</div>

<div class="layui-form-item">
    <table id="processModelTable" class="layui-hide" lay-filter="processModel"/>
</div>

<script type="text/html" id="processModelToolBar">
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="processModelUpdate"><i class="layui-icon">&#xe640;</i>编辑</a>
    {{# if(d.deploymentId == '' || d.deploymentId == null){ }}
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="processModelDeploy"><i class="layui-icon">&#xe640;</i>发部</a>
    {{# } }}
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="processModelDel"><i class="layui-icon">&#xe640;</i>删除</a>
</script>
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/modelList.js"></script>
</html>