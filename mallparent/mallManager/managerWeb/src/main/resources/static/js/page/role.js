document.onkeydown = function (ev) {
    var thisEvent = window.event || ev;
    var code = thisEvent.keyCode || thisEvent.which;
    if (code == 13) {
        $(".select .select-on").click();
    }
}

layui.use(['table'],function () {
    var table = layui.table;
    // 方法渲染
    table.render({
        id:'roleList',
        elem:'#roleList',
        url:'/role/listData',
        cols:[[
            {checkbox:true,fixed:true,width:'6%'},
            {field:'roleName',title:'角色名称',width: '10%', sort: true},
            {field: 'remark', title: '角色描述', width: '25%'},
            {field: 'creator', title: '创建人', width: '8%'},
            {field: 'createTimeStr', title: '创建时间', width: '12%'},
            {field: 'modifyor', title: '修改人', width: '8%'},
            {field: 'modifyTimeStr', title: '修改时间', width: '12%'},
            {field: '_', title: '操作', width: '15%', toolbar: "#roleToolBar"}
        ]],
        page:true,
        limits:[10,20,30,40,50],
        height:'full-80'
    });

    // 监听工具条
    table.on('tool(role)',function (obj) {
        var data = obj.data;
        if(obj.event == 'detail') {
            roleForm('角色查看', '/role/view?id='+data.roleId, 700, 450);
        } else if(obj.event == 'edit') {
            roleForm('角色编辑', '/role/form?id='+data.roleId, 700, 450);
        } else if(obj.event == 'delete') {
            layer.confirm('删除[<label style="color: #00AA91;">' +data.roleName+ '</label>]角色时会冻结该角色下的用户，您确定删除吗?', function(){
                deleteRole(data.roleId);
            });
        }
    })


    var $ = layui.$, active = {
        select: function () {
            var queryText = $('[name="queryText"]').val(),
                queryBy = $('[name="queryBy"]').val();
            table.reload('roleList',{
                where:{
                    queryBy:queryBy,
                    queryText:queryText
                }
            })
        },

        reload:function () {
            $('[name="queryText"]').val('');
            $('[name="queryBy"]').val(1);
            table.reload('roleList',{
                where:{
                    queryBy:-1,
                    queryText:''
                }
            })
        },

        add:function () {
            roleForm('角色添加','/role/form',700, 450);
        },

        edit:function () {
            var checkStatus = table.checkStatus('roleList'), data = checkStatus.data;
            if(data.length != 1) {
                layer.msg('一次只能编辑一个角色!', {icon: 5});
                return false;
            }
            roleForm('角色编辑', '/role/form?id='+data[0].roleId, 700, 450);
        },

        detail:function () {
            var checkStatus = table.checkStatus('roleList'), data = checkStatus.data;
            if(data.length != 1) {
                layer.msg('一次只能查看一个角色!', {icon: 5});
                return false;
            }
            roleForm('角色查看', '/role/view?id='+data[0].roleId, 700, 450);
        },
    }

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
})

/**
 *
 * @param title 标题
 * @param url 请求的url
 * @param w 弹出层宽度（缺省调默认值）
 * @param h 弹出层高度（缺省调默认值）
 */
function roleForm(title, url, w,h) {
    if (title == null || title == '') {
        title = false;
    }

    if (url == null || url == '') {
        url = "/error/404";
    }

    if (w == null || w == '') {
        w = ($(window).width() * 0.9);
    }

    if (h == null || h == '') {
        h = ($(window).height() - 50);
    }

    layer.open({
        id: 'roleEdit',
        type: 2,
        area: [w + 'px', h + 'px'],
        fix: false,
        maxmin: true,
        shadeClose: false,
        shade: 0.4,
        title: title,
        content: url
    });
}

/**
 * 删除
 * @param id 角色ID
 */
function deleteRole(id) {
    $.ajax({
        url: "/role/delete",
        type: "DELETE",
        data: {id: id},
        success: function (res) {
            var msg = res.msg;
            if(res.status == 200) {
                layui.table.reload('roleList');
                layer.msg(msg,{icon:6,offset: 'rb',area:['120px','80px'],anim:2});
            } else {
                layer.msg(msg,{icon:5,offset: 'rb',area:['120px','80px'],anim:2});
                return false;
            }
        }
    });
}