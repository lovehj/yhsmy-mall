var tableId = "processModelTable";
layui.use(["table","laydate"],function () {
    var table = layui.table, laydate = layui.laydate;

    // 初始化日期选择器
    laydate.render({
        elem:'#startDate',
        value: new Date(),
        type: 'datetime',
        min:'2019-12-10 00:00:00'
    })

    laydate.render({
        elem:'#endDate',
        value: new Date(),
        type: 'datetime',
        min:'2019-12-10 00:00:00'
    })

    table.render({
        id: tableId,
        elem:'#'+tableId,
        url:'/act/modelListData',
        cols:[[
            {checkbox: true, fixed: true, width: '5%'},
            {field: 'processKey', title: '流程KEY', width: '8%'},
            {field: 'processName', title: '流程名称', width: '10%'},
            {field: 'version', title: '流程版本', width: '7%'},
            {field: 'metaInfo', title: 'METAINF', width: '15%'},
            {field: 'deploymentId', title: '部署ID', width: '10%'},
            {field: 'description', title: '流程描述', width: '15%'},
            {field: 'createTimeStr', title: '创建时间', width: '12%'},
            {field: '_', title: '操作', width: '15%', toolbar: "#processModelToolBar"}
        ]],
        page: true,
        limits:[10,20,30],
        height: 'full-85'
    })


    var active = {
        modelSelect: function () {
            var queryBy = $('[name="queryBy"]').val(),
                queryText = $('[name="queryText"]').val(),
                queryDate = $('[name="queryDate"]').val(),
                startDate = $('[name="startDate"]').val(),
                endDate = $('[name="endDate"]').val();

            table.reload(tableId, {
                where:{
                    queryBy: queryBy,
                    queryText: queryText,
                    queryDate: queryDate,
                    startDate: startDate,
                    endDate: endDate
                }
            });
        },

        modelReload:function () {
            $('[name="queryBy"]').val(1);
            $('[name="queryText"]').val('');
            $('[name="queryDate"]').val(1);
            $('[name="startDate"]').val('');
            $('[name="endDate"]').val('');
            table.reload(tableId, {
                where:{
                    queryBy: 1,
                    queryText: '',
                    queryDate: 1,
                    startDate: '',
                    endDate: ''
                }
            });
        },

        modelSyncdata:function () {
            postAjax('/act/syncdata',null, null);
        },

        modelAdd: function () {
            dialog('流程新增','/processAct/form',500, 300);
        }

        
    };

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    table.on('tool(processModel)', function (obj) {
        var data = obj.data, id = data.id,event = obj.event, name = data.processName;
        if(event === 'processModelUpdate') {
            fullDialog('processModelUpdate','流程编辑', '/act/modelForm?id='+data.modelId);
        } else if(event === 'processModelDeploy') {
            postAjax('/act/deployProcess', {id:data.modelId}, tableId);
        } else if(event === 'processModelDel') {
            layer.confirm('您确定删除['+name+']这个流程吗?', function () {
                delAjax('/act/deleteModel',{id:id},tableId);
            });
        }
    });


})

$('#processGroup').on('mouseover',function(){
    layer.tips('设置流程节点的代办人/候选人/候选组，目前只开发到组', this,{time:2000});
});