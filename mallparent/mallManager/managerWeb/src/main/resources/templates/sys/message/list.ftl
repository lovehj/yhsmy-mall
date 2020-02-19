<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>消息管理</title>
</head>
<body>
<!-- 查询区域开始 -->
<div class="layui-form-item">
    <form class="layui-form">
        <div class="layui-form-item search-form-margin">
            <div class="layui-input-inline">
                <select name="flag">
                    <option value="-1" selected>不限</option>
                    <option value="0">未读</option>
                    <option value="1">已读</option>
                </select>
            </div>

            <div class="layui-input-inline">
                <select name="queryBy">
                    <option value="0" selected>不限</option>
                    <option value="1" >消息标题</option>
                    <option value="2" >消息内容</option>
                    <option value="3" >处理地址</option>
                </select>
            </div>

            <div class="layui-input-inline">
                <input class="layui-input" name="queryText" autocomplete="off">
            </div>

            <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="messageSelect">
                <i class="layui-icon"></i>
            </a>

            <a class="layui-btn layui-btn-sm icon-position-button activeBtn" id="refresh" style="float: right;" data-type="messageReload">
                <i class="layui-icon">ဂ</i>
            </a>
        </div>
    </form>

</div>
<!-- 查询区域结束 -->

<!-- 数据表格区域开始 -->
<div class="layui-form-item">
    <table id="messageList" class="layui-hide" lay-filter="message"/>
</div>

<!-- 数据表格区域结束 -->
<script type="text/html" id="messageToolBar">
    {{# if(d.flag==0){ }}
        <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="messageRead">读取</a>
    {{# } }}
    <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="messageDelete">删除</a>
</script>
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/message.js"></script>
</html>