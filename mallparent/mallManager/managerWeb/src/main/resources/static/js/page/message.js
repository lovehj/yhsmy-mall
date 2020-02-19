var messageList = "messageList";
layui.use(['table'],function(){
    var table = layui.table;
    table.render({
        id: messageList,
        elem:'#'+messageList,
        url:'/message/listData',
        cols:[[
            {checkbox: true, fixed: true, width: '6%'},
            {field: 'positionStr',title:'出现位置',width: '8%'},
            {field: 'flagStr', title: '状态', width: '8%'},
            {field: 'title', title: '消息标题', width: '12%'},
            {field: 'content', title: '消息内容', width: '16%'},
            {field: 'processUrl', title: '处理地址', width: '15%'},
            {field: 'createTimeStr', title: '创建时间', width: '15%'},
            {field: '_', title: '操作', width: '18%', toolbar: "#messageToolBar"}
        ]],
        page: true,
        height: 'full-83',
        limits:[10,20,30,40,50],
    });

    var active = {
        messageSelect:function () {
            var flag = $('select[name="flag"]').val(),
                queryBy = $('select[name="queryBy"]').val(),
                queryText = $('input[name="queryText"]').val();
            table.reload(messageList,{
                where:{
                    flag: flag,
                    queryBy: queryBy,
                    queryText: queryText
                }
            });
        },

        messageReload:function () {
            $('select[name="flag"]').val(-1);
            $('select[name="queryBy"]').val(0);
            $('input[name="queryText"]').val("");
            table.reload(messageList,{
                where:{
                    flag: -1,
                    queryBy: 0,
                    queryText: ""
                }
            });
        },
    }

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    table.on('tool(message)',function (obj) {
        var data = obj.data, event = obj.event, id = data.id;
        if(event === 'messageRead') {
            postAjax("/message/readOrDelete",{id:id, ctype:0}, messageList)
        } else if(event === 'delete') {
            layer.confirm('您确定删除吗?',function(){
                postAjax("/message/readOrDelete",{id:id, ctype:1}, messageList)
            });
        }
    })
})