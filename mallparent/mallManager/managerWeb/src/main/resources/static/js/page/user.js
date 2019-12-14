var width = 600, height = 500, title = '用户编辑', formUrl = '/user/form', userList = 'userList';
layui.use(['table'],function(){
    var table = layui.table;
    table.render({
        id: userList,
        elem:'#'+userList,
        url:'/user/listData',
        cols:[[
            {checkbox: true, fixed: true, width: '4%'},
            {field: 'username',title:'用户名',width: '7%'},
            {field: 'realName', title: '真实姓名', width: '9%'},
            {field: 'mobile', title: '手机号', width: '9%'},
            {field: 'email', title: '邮箱', width: '12%'},
            {field: 'departName', title: '所在部门', width: '13%'},
            {field: 'roleName', title: '拥有角色', width: '10%'},
            {field: 'stateStr', title: '状态', width: '8%'},
            {field: '_', title: '操作', width: '25%', toolbar: "#userToolBar"}
        ]],
        page: true,
        height: 'full-83',
        limits:[10,20,30,40,50],
    });

    var active = {
        select:function () {
            var state = $('select[name="state"]').val(),
                queryBy = $('select[name="queryBy"]').val(),
                queryText = $('input[name="queryText"]').val();
            table.reload(userList,{
                where:{
                    state: state,
                    queryBy: queryBy,
                    queryText: queryText
                }
            });
        },
        reload:function () {
            $('select[name="state"]').val(1);
            $('select[name="queryBy"]').val(0);
            $('input[name="queryText"]').val("");
            table.reload(userList,{
                where:{
                    state: 1,
                    queryBy: 0,
                    queryText: ""
                }
            });
        },
        userAdd:function () {
            dialog(title, formUrl, width, height+ 100);
        }
    }

    $('.activeBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    table.on('tool(user)',function (obj) {
        var data = obj.data, event = obj.event, id = data.id,realName = data.realName;
        if(event === 'detail') {
            dialog('用户查看', "/user/view?id="+id, width, height + 100);
        } else if(event === 'edit') {
            dialog(title, formUrl+"?id="+id, width, height);
        } else if(event === 'delete') {
            layer.confirm('您确定删除['+realName+']吗?',function(){
                delAjax('/user/delete',{id:id},userList);
            });
        } else if(event === 'editPasswd') {
            dialog('更新密码','/user/editPasswdForm?id='+id,300,260);
        } else if(event === 'initPasswd') {
            layer.confirm('您确定将密码初始人为[123456]吗？',function(){
                postAjax('/user/initPasswd',{id:id,newPasswd:'123456'},userList);
            });
        } else if(event === 'freezeUser') {
            layer.confirm('确定将['+realName+']启用/冻结吗?', function () {
                postAjax('/user/status',{id:id},userList);
            });
        }
    })
})

