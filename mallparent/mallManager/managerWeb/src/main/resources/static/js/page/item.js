var itemList = 'itemList', formUrl = '/item/form', title = '商品编辑', width = 1000, heigth = 680;
layui.use(['table'],function(){
    var table = layui.table;
    table.render({
        id: itemList,
        elem:'#'+itemList,
        url:'/item/listData',
        cols:[[
            {checkbox: true, fixed: true, width: '4%'},
            {field: 'title',title:'商品标题',width: '10%'},
            {field: 'sellPoint', title: '商品卖点', width: '12%'},
            {field: 'price', title: '价格', width: '6%'},
            {field: 'disPrice', title: '折扣价格', width: '6%'},
            {field: 'itemNum', title: '商品库存', width: '5%'},
            {field: 'sold', title: '已售数量', width: '5%'},
            {field: 'leftItemNum', title: '剩余数量', width: '5%'},
            {field: 'cateName', title: '商品库分类', width: '6%'},
            {field: 'stateStr', title: '状态', width: '8%'},
            {field: 'creator', title: '创建人', width: '7%'},
            {field: 'createTimeStr', title: '创建时间', width: '10%'},
            // {field: 'modifyor', title: '修改人', width: '8%'},
            // {field: 'modifyTimeStr', title: '修改时间', width: '11%'},
            {field: '_', title: '操作', width: '16%', toolbar: "#itemToolBar"}
        ]],
        page: true,
        limits:[10,20,30,40,50],
    });

    var active = {
        itemSelect:function () {
            var state = $('select[name="state"]').val(),
                queryBy = $('select[name="queryBy"]').val(),
                queryText = $('input[name="queryText"]').val();
            table.reload(itemList,{
                where:{
                    state: state,
                    queryBy: queryBy,
                    queryText: queryText
                }
            });
        },
        itemReload:function () {
            $('select[name="state"]').val(0);
            $('select[name="queryBy"]').val(0);
            $('input[name="queryText"]').val("");
            table.reload(itemList,{
                where:{
                    state: 0,
                    queryBy: 0,
                    queryText: ""
                }
            });
        },
        itemAdd:function () {
            dialog(title, formUrl,width, heigth);
        },
        itemUeditorAdd:function () {
            dialog(title, "/item/ueditorForm",1000, 350);//750
        }
    }

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    table.on('tool(item)',function (obj) {
        var data = obj.data, event = obj.event, id = data.itemId;
        if(event === 'itemEdit') {
            dialog(title, formUrl + "?id=" + id, width, heigth);
        } else if(event === 'itemAudit') {
            layer.confirm('您确定提交审批吗?',function(){
                postAjax('/wait/startTask',{businessId: id,startKey: data.startKey}, itemList);
            });
        } else if(event === 'itemDelete') {
            layer.confirm('您确定删除吗?',function(){
                delAjax('/item/delete',{id:id, ctype:0}, itemList);
            });
        } else if(event === 'itemDown') {
            layer.confirm('您确定下架该商品吗?',function(){
                delAjax('/item/delete',{id:id, ctype:3}, itemList);
            });
        } else if(event === 'itemUp') {
            layer.confirm('您确定上架该商品吗?',function(){
                delAjax('/item/delete',{id:id, ctype:1}, itemList);
            });
        }
    })
})