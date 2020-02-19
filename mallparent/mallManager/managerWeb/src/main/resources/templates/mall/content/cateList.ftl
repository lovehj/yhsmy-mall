<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>内容分类管理</title>
    <!-- 定义权限标识 -->
    <script>
        var contentEditFlag = 0, contentDeleteFlag = 0;
    </script>
</head>
<body>
    <div class="layui-col-md13-ext">
        <div class="layui-btn-group">
            <@shiro.hasPermission name="content:edit">
                <button class="layui-btn layui-btn-normal activeBtn" data-type="contentAdd">
                    <i class="layui-icon">&#xe608;</i>新增
                </button>
            </@shiro.hasPermission>
        </div>

        <button class="layui-btn layui-btn-sm icon-position-no-button-ext" style="float: right;" onclick="javascript:location.replace(location.href);">
            <i class="layui-icon i-icon" style="font-size: 21px">ဂ</i>
        </button>
    </div>

    <!-- 表格区域 -->
    <div id="contentTreeList"></div>

    <!-- js不识别shiro标签 -->
    <@shiro.hasPermission name="content:edit">
        <script>
            contentEditFlag = 1;
        </script>
    </@shiro.hasPermission>

    <@shiro.hasPermission name="content:delete">
        <script>
            contentDeleteFlag = 1;
        </script>
    </@shiro.hasPermission>
</body>
<script src="/plugin/layuitree/layui/layui.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script>
    var contentCateLayout = [
        {name: '分类名称', treeNodes: true, headerClass: 'value_col', colClass: 'value_col', style: 'width: 15%'},
        {field: 'ctypeStr', name: '类型', headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
        {field: 'creator',name: '创建人', headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
        {field: 'createTimeStr',name: '创建时间', headerClass: 'value_col', colClass: 'value_col', style: 'width: 10%'},
        {field: 'modifyer', name: '修改人', headerClass: 'value_col', colClass: 'value_col', style: 'width: 5%'},
        {field: 'modifyTimeStr', name: '修改时间', headerClass: 'value_col', colClass: 'value_col', style: 'width: 15%'},
        {name: '操作', headerClass: 'value_col', colClass: 'value_col', style: 'width: 20%',
            render: function (row) {
                var chil_len = row.children.length;
                var str = contentEditFlag > 0 ? '<a class="layui-btn layui-btn-xs  layui-btn-normal" onclick="contentCateEdit(\'' + row.cateId + '\')"><i class="layui-icon">&#xe642;</i> 编辑</a>' : '';
                if (chil_len <= 0 || row.children == 'undefined') {
                    str += contentDeleteFlag > 0 ? '<a class="layui-btn layui-btn-danger layui-btn-xs" onclick="contentCateDel(\'' + row.cateId + '\')"><i class="layui-icon">&#xe640;</i> 删除</a>' : '';
                }
                return str;
            }
        }
    ];

    var title = '内容分类编辑', formUrl = '/content/cateForm', w = 650, h = 400;

    layui.use(['tree', 'layer'], function () {
        layui.treeGird({
            elem: '#contentTreeList',
            nodes: ${contentCate},
            layout: contentCateLayout
        });

        var active = {
            contentAdd: function () {
                dialog(title, formUrl, w, h);
            }
        };

        $('.activeBtn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    })

    function contentCateEdit(id) {
        dialog(title, formUrl+"?cateId="+id, w, h)
    }
    
    function contentCateDel(id) {
        layer.confirm('您确定删除吗？', function () {
            delAjaxNoTable('/content/cateDelete', {cateId: id})
        });
    }

</script>
</html>