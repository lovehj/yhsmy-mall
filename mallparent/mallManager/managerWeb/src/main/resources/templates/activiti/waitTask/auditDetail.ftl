<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>审批详情</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
    <style>
        .layui-input {
            height: 30px;
            width: 120px;
        }

        .x-nav {
            padding: 0 20px;
            position: relative;
            z-index: 99;
            border-bottom: 1px solid #e5e5e5;
            height: 32px;
            overflow: hidden;
        }
    </style>
</head>
<body>
    <table id="auditDetailList" class="layui-hide" lay-filter="auditDetail"/>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use('table', function () {
        var table = layui.table;
        //方法级渲染
        table.render({
            id: 'auditDetailList',
            elem: '#auditDetailList'
            , data: ${auditDetailList!}
            , cols: [[
                {field: 'taskName', title: '任务名称', width: '15%'},
                {field: 'assgin', title: '审批人', width: '10%'},
                {field: 'opinion', title: '审批意见', width: '25%'},
                {field: 'startTime', title: '任务开始时间', width: '17%'},
                {field: 'endTime', title: '任务结束时间', width: '17%'},
                {field: 'duration', title: '任务办理时长', width: '16%'},
            ]]
            , page: false
            , height: 'full-160'
        });
    });
</script>
</html>