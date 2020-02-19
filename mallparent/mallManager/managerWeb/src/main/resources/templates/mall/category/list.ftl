<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>商品分类管理</title>
    <!-- 定义权限标识 -->
    <script>
        var categoryEditFlag = 0, categoryDeleteFlag = 0;
    </script>
</head>
<body>
    <div class="layui-col-md13-ext">
        <div class="layui-btn-group">
            <@shiro.hasPermission name="category:edit">
                <button class="layui-btn layui-btn-normal activeBtn" data-type="categoryAdd">
                    <i class="layui-icon">&#xe608;</i>新增
                </button>
            </@shiro.hasPermission>
        </div>

        <button class="layui-btn layui-btn-sm icon-position-no-button-ext" style="float: right;" onclick="javascript:location.replace(location.href);">
            <i class="layui-icon i-icon" style="font-size: 21px">ဂ</i>
        </button>
    </div>
    <!-- 表格区域 -->
    <div id="categoryTreeList"></div>

    <!-- js不识别shiro标签 -->
    <@shiro.hasPermission name="category:edit">
        <script>
            categoryEditFlag = 1;
        </script>
    </@shiro.hasPermission>

    <@shiro.hasPermission name="category:delete">
        <script>
            categoryDeleteFlag = 1;
        </script>
    </@shiro.hasPermission>
</body>
<script src="/plugin/layuitree/layui/layui.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script type="text/javascript">
    var w = 500, h = 350;
    var categoryLayout = [
        {name: '分类名称', treeNodes: true, headerClass: 'value_col', colClass: 'value_col', style: 'width: 15%'},
        {field: 'cateSort', name: '序号',headerClass: 'value_col', colClass: 'value_col', style: 'width: 8%'},
        {field: 'remark', name: '备注',headerClass: 'value_col', colClass: 'value_col', style: 'width: 15%'},
        {field: 'creator', name: '创建人',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
        {field: 'createTimeStr', name: '创建时间',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
        {field: 'modifyor', name: '修改人',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
        {field: 'modifyTimeStr', name: '修时间',headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
        {name: '操作',headerClass: 'value_col',colClass: 'value_col',style: 'width: 20%',
            render: function(row) {
                var chil_len=row.children.length;
                var str= categoryEditFlag > 0 ?'<a class="layui-btn layui-btn-xs  layui-btn-normal" onclick="categoryEdit(\'' + row.cateId + '\')"><i class="layui-icon">&#xe642;</i> 编辑</a>' : '';
                if(chil_len==0){
                    str+= categoryDeleteFlag > 0 ? '<a class="layui-btn layui-btn-danger layui-btn-xs" onclick="categoryDel(\'' + row.cateId + '\')"><i class="layui-icon">&#xe640;</i> 删除</a>' : '';
                }
                return str;
            }
        }
    ];

    layui.use(['tree', 'layer'],function () {
        var layer = layui.layer;
        layui.treeGird({
            elem: '#categoryTreeList',
            nodes: ${categoryList},
            layout: categoryLayout
        });
        var active = {
            categoryAdd:function () {
                dialog('商品分类编辑', '/category/form', w, h);
            }
        };

        $('.activeBtn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    })

    function categoryEdit(id) {
        dialog('商品分类编辑', '/category/form?id='+id, w, h);
    }

    function categoryDel(id) {
        layer.confirm('您确定删除吗?', function () {
            delAjaxNoTable('/category/delete',{id:id});
        });
    }
</script>
</html>