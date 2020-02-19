var contentList = 'contentList', title = '内容编辑', formUrl = '/content/form', w = 1000, h = 630;

layui.use(['table'],function(){
    var table = layui.table;
    table.render({
        id: contentList,
        elem:'#'+contentList,
        url:'/content/listData',
        cols:[[
            {checkbox: true, fixed: true, width: '5%'},
            {field: 'title',title:'标题',width: '12%'},
            {field: 'subTitle', title: '副标题', width: '15%'},
            {field: 'contentCateName', title: '所属分类', width: '8%'},
            {field: 'linkUrl', title: '链接地址', width: '13%'},
            {field: 'content', title: '内容', width: '12%'},
            {field: 'contentSort', title: '排序值', width: '5%'},
            {field: 'stateStr', title: '状态', width: '8%'},
            {field: 'description', title: '备注', width: '10%'},
            {field: '_', title: '操作', width: '12%', toolbar: "#contentToolBar"}
        ]],
        page: true,
        height: 'full-83',
        limits:[10,20,30,40,50],
    });

    var active = {
        contentSelect:function () {
            var queryBy = $('select[name="queryBy"]').val(),
                queryText = $('input[name="queryText"]').val(),
                state = $('select[name="state"]').val();
            table.reload(contentList,{
                where:{
                    queryBy: queryBy,
                    queryText: queryText,
                    state: state
                }
            });
        },
        contentReload:function () {
            $('select[name="queryBy"]').val(2);
            $('input[name="queryText"]').val("");
            $('select[name="state"]').val(1);
            layui.form.render();
            table.reload(contentList,{
                where:{
                    queryBy: 2,
                    queryText: "",
                    state: 0
                }
            });
        },
        contentAdd:function () {
            dialog(title, formUrl, w, h);
        }
    }

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    table.on('tool(content)',function (obj) {
        var data = obj.data, event = obj.event, id = data.contentId;
        if(event === 'contentEdit') {
            dialog(title, formUrl+"?id="+id, w, h);
        } else if(event === 'contentDelete') {
            layer.confirm('您确定删除吗?',function(){
                delAjax('/content/delete',{id:id},contentList);
            });
        } else if(event === 'contentAudit') {
            layer.confirm('您确定提交审批吗？',function(){
                postAjax('/wait/startTask',{businessId: id,startKey: data.startKey}, contentList);
            });
        } else if(event === 'contentDetail') {
            dialog(title, formUrl+"?contentView=true&noCloseBtn=true&id="+id, w, h);
        }
    })
})

