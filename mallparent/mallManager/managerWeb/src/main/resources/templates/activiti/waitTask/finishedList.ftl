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
                <select name="queryText">
                    <option value="" selected>不限</option>
                    <#list approveList as approve>
                        <option value="${approve.key}">${approve.value}</option>
                    </#list>
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
    <table id="finishedTaskList" class="layui-hide" lay-filter="finishedTask"/>

    <script type="text/html" id="finishedTaskToolBar">
        <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="taskDetail">任务详情</a>
        <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="finishedTaskDetail">办理详情</a>
    </script>
</div>

<!-- 数据表格区域结束 -->
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script type="text/javascript">
    var finishedTaskList = "finishedTaskList";
    layui.use(["table"],function () {
        var table = layui.table;
        table.render({
            id: finishedTaskList,
            elem:'#'+finishedTaskList,
            url:'/wait/finishedListData',
            cols:[[
                {checkbox: true, fixed: true, width: '4%'},
                {field: 'taskName', title: '任务名称', width: '10%'},
                {field: 'auditCtypeName', title: '任务类型', width: '8%'},
                {field: 'auditName', title: '申请人', width: '6%'},
                {field: 'auditContent', title: '审批内容', width: '20%'},
                {field: 'startTime', title: '任务开始时间', width: '12%'},
                {field: 'endTime', title: '任务结束时间', width: '12%'},
                {field: 'durationTime', title: '任务办理时长', width: '10%'},
                {field: '_', title: '操作', width: '13%', toolbar: "#finishedTaskToolBar"}
            ]],
            page: true,
            limits:[10,20,30],
        });

        table.on('tool(finishedTask)', function (obj) {
            var data = obj.data, event = obj.event, startKey = data.startKey, procInstId = data.procInstId;
            if(event === 'taskDetail') {
                dialog('任务详情', "/wait/view/"+startKey+"/"+procInstId, 700, 500);
            }  else if(event === 'finishedTaskDetail') {
                dialog('办理详情', '/wait/auditDetail?processInstanceId='+procInstId, 1000, 450);
            }
        })

        var active = {
            waitSelect: function () {
                var queryText = $('[name="queryText]').val();
                table.reload(finishedTaskList, {
                    where: {
                        queryText: queryText,
                    }
                });
            },

            waitReload: function () {
                var queryText = $('[name="queryText"]').val('');
                table.reload(finishedTaskList, {
                    where: {
                        queryText: '',
                    }
                });
            }
        }

        $('.activeBtn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    })
</script>
</html>