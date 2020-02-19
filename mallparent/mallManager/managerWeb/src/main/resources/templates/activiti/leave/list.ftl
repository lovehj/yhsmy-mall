<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>请假管理</title>
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
                    <option value="10">审批中</option>
                    <option value="11">审批通过</option>
                    <option value="12">审批退回</option>
                </select>
            </div>

            <div class="layui-input-inline">
                <select name="queryBy">
                    <option value="0" selected>不限</option>
                    <option value="1">请假人</option>
                    <option value="2">请假事由</option>
                    <option value="3">审批备注</option>
                </select>
            </div>

            <div class="layui-input-inline">
                <input class="layui-input" name="queryText" placeholder="关键字" autocomplete="off">
            </div>

            <div class="layui-input-inline">
                <select name="queryDate">
                    <option value="0" selected>不限</option>
                    <option value="1">请假开始时间</option>
                    <option value="2">请假结束时间</option>
                    <option value="3">创建时间</option>
                </select>
            </div>

            <div class="layui-input-inline">
                <input class="layui-input" id="leaveStartDate" name="startDate" placeholder="开始时间" autocomplete="off"> -
            </div>

            <div class="layui-input-inline">
                <input class="layui-input" id="leaveEndDate" name="endDate" placeholder="结束时间" autocomplete="off">
            </div>

            <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="leaveSelect">
                <i class="layui-icon"></i>
            </a>

            <@shiro.hasPermission name="leave:edit">
                <a class="layui-btn layui-btn-normal activeBtn" data-type="leaveAdd">
                    <i class="layui-icon">&#xe608;</i>新增
                </a>
            </@shiro.hasPermission>

            <a class="layui-btn layui-btn-sm icon-position-button activeBtn" style="float: right;" data-type="leaveReload">
                <i class="layui-icon">ဂ</i>
            </a>
        </div>
    </form>
</div>
<!-- 查询区域结束 -->

<!-- 数据表格区域开始 -->
<div class="layui-form-item">
    <#assign currentUser = Session["currentPrincipal"]>
    <table id="leaveList" class="layui-hide" lay-filter="leave"/>

    <script type="text/html" id="leaveToolBar">
        <@shiro.hasPermission name="leave:edit">
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="leaveEdit">编辑</a>
            {{# if(d.state == 1 && d.userId === '${currentUser.id}'){ }}
                <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="leaveAudit">提交申请</a>
            {{#  } }}

            {{# if(d.processInstanceId !='' && d.processInstanceId != null && d.state > 1 ){ }}
                <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="leaveProcessImg">查看流程图</a>
                <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="leaveAuditDetail">审批详情</a>
            {{# } }}
        </@shiro.hasPermission>

        <@shiro.hasPermission name="leave:delete">
            <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="leaveDelete">删除</a>
        </@shiro.hasPermission>
    </script>
</div>

<!-- 数据表格区域结束 -->
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/leave.js"></script>
</html>