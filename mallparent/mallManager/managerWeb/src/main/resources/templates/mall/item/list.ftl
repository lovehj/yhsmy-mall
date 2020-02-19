<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>商品管理</title>
    <script>
        var itemEditFlag = 0, itemDeleteFlag = 0;
    </script>
</head>
<body>
    <!-- 查询区域开始 -->
    <div class="layui-form-item">
        <form class="layui-form">
            <div class="layui-form-item search-form-margin">
                <div class="layui-input-inline">
                    <select name="state">
                        <option value="0" selected>不限</option>
                        <option value="1">正常</option>
                        <option value="2">冻结</option>
                        <option value="3">下架</option>
                    </select>
                </div>

                <div class="layui-input-inline">
                    <select name="queryBy">
                        <option value="1" selected>商品标题</option>
                        <option value="2" >商品卖点</option>
                    </select>
                </div>

                <div class="layui-input-inline">
                    <input class="layui-input" name="queryText" placeholder="商品标题/卖点" autocomplete="off">
                </div>

                <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="itemSelect">
                    <i class="layui-icon"></i>
                </a>

                <@shiro.hasPermission name="item:edit">
                    <a class="layui-btn layui-btn-normal activeBtn" data-type="itemAdd">
                        <i class="layui-icon">&#xe608;</i>新增
                    </a>
                </@shiro.hasPermission>

                <a class="layui-btn layui-btn-sm icon-position-button activeBtn" style="float: right;" data-type="itemReload">
                    <i class="layui-icon">ဂ</i>
                </a>
            </div>
        </form>
    </div>
    <!-- 查询区域结束 -->

    <!-- 数据表格区域开始 -->
    <div class="layui-form-item">
        <table id="itemList" class="layui-hide" lay-filter="item"/>

        <script type="text/html" id="itemToolBar">
            <@shiro.hasPermission name="item:edit">
                <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="itemEdit">编辑</a>
                {{# if(d.state==1){ }}
                    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="itemAudit">提交审批</a>
                {{# } }}
            </@shiro.hasPermission>

            <@shiro.hasPermission name="item:delete">
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="itemDelete">删除</a>
                {{# if(d.state == 11){ }}
                    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="itemUp">上架</a>
                {{#  }else if(d.state == 11) { }}
                    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="itemDown">下架</a>
                {{#  } }}
            </@shiro.hasPermission>
        </script>
    </div>

    <@shiro.hasPermission name="item:edit">
       <script>
           var itemEditFlag = 1;
       </script>
    </@shiro.hasPermission>

    <@shiro.hasPermission name="item:delete">
        <script>
            var itemDeleteFlag = 1;
        </script>
    </@shiro.hasPermission>
    <!-- 数据表格区域结束 -->
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/item.js"></script>
</html>