<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>流程部署管理</title>
</head>
<body>
    <!-- 查询区域开始 -->
    <div class="layui-form-item">
        <form class="layui-form">
            <div class="layui-form-item search-form-margin" style="margin-left: -20px;">
                <label class="layui-form-label">部署ID:</label>
                <div class="layui-input-inline">
                    <input class="layui-input search-input-sm" id="deploymentId" autocomplete="off">
                </div>

                <label class="layui-form-label">部署名称:</label>
                <div class="layui-input-inline">
                    <input class="layui-input search-input-sm" id="deployName" autocomplete="off">
                </div>

                <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="processSelect">
                    <i class="layui-icon"></i>
                </a>

                <a class="layui-btn layui-btn-sm icon-position-button activeBtn" id="processRefresh" style="float: right;" data-type="processRefresh">
                    <i class="layui-icon">ဂ</i>
                </a>
            </div>
        </form>
    </div>

    <div class="layui-form-item">
        <table id="processDeployTable" class="layui-hide" lay-filter="processDeploy"/>
    </div>

    <script type="text/html" id="processListToolBar">
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="processDeployDel"><i class="layui-icon">&#xe640;</i>删除</a>
    </script>
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/processList.js"></script>
</html>