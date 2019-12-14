<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>菜单管理</title>
    <!-- 定义权限标识 -->
    <script>
        var departEdit = 0, departDelete = 0;
    </script>
</head>
<body>
    <div class="layui-col-md13-ext">
        <div class="layui-btn-group">
            <@shiro.hasPermission name="depart:edit">
                <button class="layui-btn layui-btn-normal" data-type="departAdd">
                    <i class="layui-icon">&#xe608;</i>新增
                </button>
            </@shiro.hasPermission>
        </div>

        <button class="layui-btn layui-btn-sm icon-position-no-button-ext" id="departRefresh" style="float: right;" onclick="javascript:location.replace(location.href);">
            <i class="layui-icon i-icon" style="font-size: 21px">ဂ</i>
        </button>
    </div>
    <!-- 表格区域 -->
    <div id="departTreeList"></div>

    <!-- js不识别shiro标签 -->
    <@shiro.hasPermission name="depart:edit">
        <script>
            departEdit = 1;
        </script>
    </@shiro.hasPermission>

    <@shiro.hasPermission name="depart:delete">
        <script>
            departDelete = 1;
        </script>
    </@shiro.hasPermission>
</body>
<script src="/plugin/layuitree/layui/layui.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/depart.js"></script>
<script>
    layui.use(['tree', 'layer'],function () {
        var layer = layui.layer;
        layui.treeGird({
            elem: '#departTreeList',
            nodes: ${departs},
            layout: departLayout
        });
        var $ = layui.$,active = {
            departAdd:function () {
                edit("");
            }
        };

        $('.layui-btn-group .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    })
</script>
</html>