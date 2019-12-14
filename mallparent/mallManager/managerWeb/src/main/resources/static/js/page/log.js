var logList = "logList";
layui.use(["table","laydate"], function () {
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
        id: logList,
        elem:'#'+logList,
        url:'/log/listData',
        cols:[[
            {checkbox: true, fixed: true, width: '4%'},
            {field: 'operaType', title: '操作类型', width: '7%'},
            {field: 'formIp', title: '操作IP', width: '9%'},
            {field: 'formDevice', title: '操作设备', width: '6%'},
            {field: 'browser', title: '浏览器', width: '7%'},
            {field: 'browserVersion', title: '浏览器版本', width: '10%'},
            {field: 'remark', title: '操作备注', width: '10%'},
            {field: 'requestMethod', title: '操作方法', width: '10%'},
            {field: 'requestParams', title: '操作参数', width: '10%'},
            {field: 'exceptionMessage', title: '错误信息', width: '8%'},
            {field: 'operator',title:'操作人',width: '7%'},
            {field: 'createTimeStr',title:'操作时间',width: '7%'},
            {field: '_', title: '操作', width: '5%', toolbar: "#logToolBar"}
        ]],
        page: true,
        limits:[10,15,20,25],
    })
    
    table.on('tool(log)', function (obj) {
        var data = obj.data, event = obj.event, id = data.logId;
        if(event === 'logDelete') {
            layer.confirm('您确定删除吗?',function(){
                delAjax('/log/delete',{id:id},logList);
            });
        }
    })

    var active = {
        logSelect:function () {
            var queryBy = $('[name="queryBy"]').val(),
                queryText = $('[name="queryText"]').val(),
                queryDate = $('[name="queryDate"]').val(),
                startDate = $('#startDate').val(),
                endDate = $('#endDate').val();
            table.reload(logList, {
                where: {
                    queryBy: queryBy,
                    queryText: queryText,
                    queryDate: queryDate,
                    startDate: startDate,
                    endDate: endDate
                }
            });
        },

        logRefresh:function () {
            var queryBy = $('[name="queryBy"]').val(1),
                queryText = $('[name="queryText"]').val(''),
                queryDate = $('[name="queryDate"]').val(1),
                startDate = $('#startDate').val(''),
                endDate = $('#endDate').val('');
            table.reload(logList, {
                where: {
                    queryBy: queryBy,
                    queryText: queryText,
                    queryDate: queryDate,
                    startDate: startDate,
                    endDate: endDate
                }
            });
        }
    }

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


})