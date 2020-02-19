var waitTaskList = "waitTaskList";
layui.use(["table"],function () {
    var table = layui.table;
    table.render({
        id: waitTaskList,
        elem:'#'+waitTaskList,
        url:'/wait/listData',
        cols:[[
            {checkbox: true, fixed: true, width: '6%'},
            {field: 'auditName', title: '申请人', width: '10%'},
            {field: 'createTimeStr', title: '申请时间', width: '12%'},
            {field: 'auditContent', title: '申请内容', width: '20%'},
            {field: 'auditCtypeName', title: '审批类型', width: '10%'},
            {field: 'taskName', title: '任务名称', width: '10%'},
            {field: 'priority', title: '任务优先级', width: '8%'},
            {field: 'taskDefKey', title: '流程定义KEY', width: '10%'},
            {field: '_', title: '操作', width: '13%', toolbar: "#waitTaskToolBar"}
        ]],
        page: true,
        limits:[10,20,30],
    });

    table.on('tool(waitTask)', function (obj) {
        var data = obj.data, event = obj.event, taskId = data.taskId, procInstId = data.procInstId;
        if(event === 'waitTaskHandler') {
            dialog('任务办理', "/wait/form/"+taskId+"/"+procInstId, 700, 500);
        }  else if(event === 'waitTaskDetail') {
            dialog('审批详情', '/wait/auditDetail?processInstanceId='+procInstId, 1000, 450);
        }
    })

    var active = {
        waitSelect: function () {
            var queryBy = $('[name="queryBy"]').val();
            queryText = "leaveBill";
            if(queryBy == 2) {
                queryText = "itemAudit";
            }
            table.reload(waitTaskList, {
                where: {
                    queryBy: queryBy,
                    queryText: queryText,
                }
            });
        },

        waitReload: function () {
            var queryBy = $('[name="queryBy"]').val(0);
            table.reload(waitTaskList, {
                where: {
                    queryBy: 0,
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