var departLayout = [
    { name: '部门名称', treeNodes: true, headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
    { name: '备注',headerClass: 'value_col', colClass: 'value_col', style: 'width: 18%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username">'+(typeof(row.remark)=="undefined"?'':row.remark)+'</div>';
        }
    },
    { name: '创建人',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username">'+row.creator+'</div>';
        }
    },
    { name: '创建时间',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username">'+(typeof(row.createTimeStr)=="undefined"?'':row.createTimeStr)+'</div>'; //列渲染
        }
    },
    { name: '修改人',headerClass: 'value_col', colClass: 'value_col', style: 'width: 5%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username"><i class="layui-icon">'+(typeof(row.modifyor)=="undefined"?'':row.modifyor)+'</i></div>'; //列渲染
        }
    },
    { name: '修改时间',headerClass: 'value_col', colClass: 'value_col', style: 'width: 5%',
        render: function(row) {
            return '<div class="layui-table-cell laytable-cell-1-username"><i class="layui-icon">'+(typeof(row.modifyTimeStr)=="undefined"?'':row.modifyTimeStr)+'</i></div>'; //列渲染
        }
    },
    {name: '操作',headerClass: 'value_col',colClass: 'value_col',style: 'width: 20%',
        render: function(row) {
            var chil_len=row.children.length;
            var str= departEdit > 0 ?'<a class="layui-btn layui-btn-xs  layui-btn-normal" onclick="edit(\'' + row.id + '\')"><i class="layui-icon">&#xe642;</i> 编辑</a>' : '';
            if(chil_len<=0 || row.children == 'undefined'){
                str+= departDelete > 0 ? '<a class="layui-btn layui-btn-danger layui-btn-xs" onclick="del(\'' + row.id + '\')"><i class="layui-icon">&#xe640;</i> 删除</a>' : '';
            }
            return str;
        }
    }
];

function edit(id) {
   popup('departEditDialog','部门编辑','/depart/form?id='+id,700,500);
}

function del(id) {
    layer.confirm('删除部门时会冻结该部门下的所有用户,您确定删除吗?', function () {
        delAjaxNoTable('/depart/delete',{id:id});
    });
}