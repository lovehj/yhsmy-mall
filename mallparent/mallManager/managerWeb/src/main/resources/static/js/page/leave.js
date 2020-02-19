var leaveList = "leaveList", formUrl = "/leave/form", width = 700, heigth = 500, title = "请假编辑";

layui.use(["table","laydate"], function () {
    var table = layui.table, laydate = layui.laydate;

    // 初始化日期选择器
    laydate.render({
        elem:'#leaveStartDate',
        value: new Date(),
        type: 'datetime',
        min:'2019-12-10 00:00:00'
    })

    laydate.render({
        elem:'#leaveEndDate',
        value: new Date(),
        type: 'datetime',
        min:'2019-12-10 00:00:00'
    })

    table.render({
        id: leaveList,
        elem:'#'+leaveList,
        url:'/leave/listData',
        cols:[[
            {checkbox: true, fixed: true, width: '4%'},
            {field: 'creator', title: '请假人', width: '7%'},
            {field: 'daysStr', title: '请假天数', width: '6%'},
            {field: 'content', title: '请假事由', width: '20%'},
            {field: 'startDateStr', title: '请假开始时间', width: '11%'},
            {field: 'endDateStr', title: '请假结束时间', width: '11%'},
            {field: 'remark', title: '审批备注', width: '10%'},
            {field: 'stateStr', title: '状态', width: '6%'},
            {field: 'createTimeStr', title: '创建时间', width: '10%'},
            {field: '_', title: '操作', width: '15%', toolbar: "#leaveToolBar"}
        ]],
        page: true,
        limits:[10,20,30],
    })

    table.on('tool(leave)', function (obj) {
        var data = obj.data, event = obj.event, id = data.leaveId;
        if(event === 'leaveEdit') {
            dialog(title, formUrl+"?id="+id, width, heigth);
        } else if(event === 'leaveDelete') {
            layer.confirm('您确定删除吗?',function(){
                delAjax('/leave/delete',{id:id},leaveList);
            });
        } else if(event === 'leaveAudit') {
            layer.confirm('您确定提交请假申请吗?', function () {
                postAjax('/wait/startTask',{businessId: id,startKey: data.startKey}, leaveList);
            });
        } else if(event === 'leaveProcessImg') {
            // 查看流程图
            dialog('查看流程图','/act/viewProcessImg/'+data.processInstanceId,950, 500);
        } else if(event === 'leaveAuditDetail') {
            // 查看审批详情
            //dialog('审批详情', '/leave/auditDetail?processInstanceId='+data.processInstanceId, 600, 450);
            dialog('审批详情', '/wait/auditDetail?bussinessId='+id, 600, 450);
        }
    })

    var active = {
        leaveSelect: function () {
            var queryBy = $('[name="queryBy"]').val(),
                queryText = $('[name="queryText"]').val(),
                queryDate = $('[name="queryDate"]').val(),
                startDate = $('#startDate').val(),
                endDate = $('#endDate').val(),
                state = $('[name="state"]').val();
            table.reload(leaveList, {
                where: {
                    state: state,
                    queryBy: queryBy,
                    queryText: queryText,
                    queryDate: queryDate,
                    startDate: startDate,
                    endDate: endDate
                }
            });
        },

        leaveReload: function () {
            var queryBy = $('[name="queryBy"]').val(0),
                queryText = $('[name="queryText"]').val(''),
                queryDate = $('[name="queryDate"]').val(0),
                startDate = $('#startDate').val(''),
                endDate = $('#endDate').val(''),
                state = $('[name="state"]').val(0);
            table.reload(leaveList, {
                where: {
                    state: 0,
                    queryBy: 0,
                    queryText: '',
                    queryDate: 0,
                    startDate: '',
                    endDate: ''
                }
            });
        },

        leaveAdd: function () {
            dialog(title, formUrl, width, heigth);
        }
    }

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
})