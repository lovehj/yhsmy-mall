function edit(menuId) {
    add('菜单编辑', 'form?id='+menuId, 700, 550);
}

function del(menuId) {
    layer.confirm('您确定删除吗?', function () {
        $.ajax({
            url:'/menu/delete',
            type:'DELETE',
            data:{id:menuId},
            success:function (res) {
                var iconNum = 5;
                if(res.status == 200) {
                    location.replace(location.href);
                    iconNum = 6;
                }
                parent.layer.msg(res.msg,{icon:iconNum,offset: 'rb',area:['120px','80px'],anim:2});
            }
        });
    })
}

var menuLayout = [
    { name: '菜单名称', treeNodes: true, headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
    { name: '地址',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username">'+(typeof(row.url)=="undefined"?'':row.url)+'</div>';
        }
    },
    { name: '类型',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username">'+row.menuTypeStr+'</div>'; //列渲染
        }
    },
    { name: '权限',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username">'+(typeof(row.permission)=="undefined"?'':row.permission)+'</div>'; //列渲染
        }
    },
    { name: '图标',headerClass: 'value_col', colClass: 'value_col', style: 'width: 5%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username"><i class="layui-icon">'+(typeof(row.icon)=="undefined"?'':row.icon)+'</i></div>'; //列渲染
        }
    },
    { name: '序号',headerClass: 'value_col', colClass: 'value_col', style: 'width: 5%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username"><i class="layui-icon">'+(typeof(row.orderNum)=="undefined"?'':row.orderNum)+'</i></div>'; //列渲染
        }
    },
    {name: '操作',headerClass: 'value_col',colClass: 'value_col',style: 'width: 20%',
        render: function(row) {
            var chil_len=row.children.length;
            var str= menuEdit > 0 ?'<a class="layui-btn layui-btn-xs  layui-btn-normal" onclick="edit(\'' + row.id + '\')"><i class="layui-icon">&#xe642;</i> 编辑</a>' : '';
            if(chil_len==0){
                str+= menuDelete > 0 ? '<a class="layui-btn layui-btn-danger layui-btn-xs" onclick="del(\'' + row.id + '\')"><i class="layui-icon">&#xe640;</i> 删除</a>' : '';
            }
            return str;
        }
    }
];

function add(title, url, w, h) {
    if(title == null || title == '') {
        title = false;
    }

    if(url == null || url == '') {
        url = '/error/404';
    }
    if (w == null || w == '') {
        w = ($(window).width() * 0.9);
    }
    if (h == null || h == '') {
        h = ($(window).height() - 50);
    }
    layer.open({
        id: 'menuAdd',
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

