<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>内容分类管理</title>
</head>
<body>
    <!-- 查询区域开始 -->
    <div class="layui-form-item">
        <form class="layui-form">
            <div class="layui-form-item search-form-margin">
                <div class="layui-input-inline">
                    <select name="state">
                        <option value="0" selected>不限</option>
                        <option value="1">待审</option>
                        <option value="10">审批中</option>
                        <option value="11">审批通过</option>
                        <option value="12">审批退回</option>
                    </select>
                </div>
                <div class="layui-input-inline">
                    <select name="queryBy">
                        <option value="2" selected>标题</option>
                        <option value="3" >副标题</option>
                        <option value="4" >备注</option>
                        <option value="5" >内容</option>
                    </select>
                </div>

                <div class="layui-input-inline">
                    <input class="layui-input" name="queryText" placeholder="查询关键字" autocomplete="off">
                </div>

                <a class="select-on layui-btn layui-btn-sm activeBtn" data-type="contentSelect">
                    <i class="layui-icon"></i>
                </a>

                <@shiro.hasPermission name="content:edit">
                    <a class="layui-btn layui-btn-normal activeBtn" data-type="contentAdd">
                        <i class="layui-icon">&#xe608;</i>新增
                    </a>
                </@shiro.hasPermission>

                <a class="layui-btn layui-btn-sm icon-position-button activeBtn" style="float: right;" data-type="contentReload">
                    <i class="layui-icon">ဂ</i>
                </a>
            </div>
        </form>
    </div>

    <div class="layui-form-item">
        <table id="contentList" class="layui-hide" lay-filter="content"/>
    </div>

    <script type="text/html" id="contentToolBar">
        <@shiro.hasPermission name="content:edit">
            {{#  if(d.state==1 || d.state==12){ }}
                <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="contentEdit">编辑</a>
                <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="contentAudit">提交审批</a>
            {{#  } else { }}
                <a class="layui-btn layui-btn-xs  layui-btn-normal" lay-event="contentDetail">查看</a>
            {{#  } }}
        </@shiro.hasPermission>

        <@shiro.hasPermission name="content:delete">
            {{#  if(d.state==1 || d.state==12){ }}
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="contentDelete">删除</a>
            {{#  } }}
        </@shiro.hasPermission>
    </script>
</body>
<script src="/plugin/layuitree/layui/layui.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script src="/js/page/content.js"></script>
</html>