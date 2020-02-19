var tableId = "processDeployTable";
layui.use(["table"],function () {
    var table = layui.table;
    table.render({
        id: tableId,
        elem:'#'+tableId,
        url:'/act/processListData',
        cols:[[
            {checkbox: true, fixed: true, width: '6%'},
            {field: 'id', title: '编号', width: '12%'},
            {field: 'name', title: '流程名称', width: '10%'},
            {field: 'key', title: 'key', width: '10%'},
            {field: 'deploymentId', title: '部署id', width: '7%'},
            {field: 'diagramResourceName', title: '流程图资源', width: '15%'},
            {field: 'version', title: '版本', width: '8%'},
            {field: 'resourceName', title: '资源名称', width: '15%'},
            {field: '_', title: '操作', width: '15%', toolbar: "#processListToolBar"}
        ]],
        page: true,
        limits:[10,20,30],
        height: 'full-85'
    })

    var active = {
        processSelect:function () {
            var deployId = $('#deploymentId').val(),
                name = $('#deployName').val();
            table.reload(tableId, {
                where: {
                    deploymentId: deployId,
                    name: name
                }
            })

        },
        processRefresh: function () {
            $('#deploymentId').val('');
            $('#deployName').val('');
            table.reload(tableId, {
                where: {
                    deploymentId: '',
                    name: ''
                }
            })
        }
    };

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    table.on('tool(processDeploy)', function (obj) {
        var data = obj.data, id = data.deploymentId,event = obj.event, name = data.name;
        if (event === 'processDeployDel') {
            // 流程删除
            layer.confirm('您确定删除吗?',function(){
                delAjax('/act/processListDelete',{id: id}, tableId);
            });

        }

    })
})