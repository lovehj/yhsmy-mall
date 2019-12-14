<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>菜单管理</title>
    <!-- 定义权限标识 -->
    <script>
        var menuEdit = 0, menuDelete = 0;
    </script>
</head>
<body>
    <div class="layui-col-md13-ext">
        <div class="layui-btn-group">
            <@shiro.hasPermission name="menu:edit">
                <button class="layui-btn layui-btn-normal" data-type="add">
                    <i class="layui-icon">&#xe608;</i>新增
                </button>
            </@shiro.hasPermission>
        </div>

        <button class="layui-btn layui-btn-sm icon-position-no-button-ext" id="refresh" style="float: right;" onclick="javascript:location.replace(location.href);">
            <i class="layui-icon i-icon" style="font-size: 21px">ဂ</i>
        </button>
    </div>
    <!-- 表格区域 -->
    <div id="menuTreeList"></div>

    <!-- js不识别shiro标签 -->
    <@shiro.hasPermission name="menu:edit">
            <script>
                menuEdit = 1;
            </script>
    </@shiro.hasPermission>

    <@shiro.hasPermission name="menu:delete">
        <script>
            menuDelete = 1;
        </script>
    </@shiro.hasPermission>
</body>
<script src="/plugin/layuitree/layui/layui.js"></script>
<script src="/js/page/menu.js"></script>
<script>
    layui.use(['tree', 'layer'],function () {
        var layer = layui.layer;
        layui.treeGird({
            elem: '#menuTreeList',
            nodes: ${menus},
            layout: menuLayout
        });
        var $ = layui.$,active = {
            add:function () {
                add('菜单添加', 'form', 700, 550);
            }
        };

        $('.layui-btn-group .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    })
</script>
</html>