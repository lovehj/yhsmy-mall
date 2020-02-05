<!DOCTYPE html>
<html>
<head>
    <#include "/includs/header.ftl"/>
    <link rel="stylesheet" href="/plugin/plugins/font-awesome/css/font-awesome.min.css" media="all">
    <link rel="stylesheet" href="/plugin/build/css/app.css" media="all">
    <link rel="stylesheet" href="/plugin/build/css/themes/default.css" media="all" id="skin" kit-skin>
</head>
<body class="kit-theme">
<div class="layui-layout layui-layout-admin kit-layout-admin">
    <!-- 头部区域开始 -->
    <div class="layui-header">
        <div class="layui-logo">永红山盟院后端管理平台</div>
        <div class="layui-logo kit-logo-mobile"></div>
        <div class="layui-hide-xs">
            <ul class="layui-nav layui-layout-left kit-nav">
                <li class="layui-nav-item" style="width: 100px;">
                    <a href="javascript:;" kit-target data-options="{url:'/message/list',icon:'&#xe658;',title:'消息中心'}">
                        消息中心<#if messageCount gt 0><span class="layui-badge" id="messageCount">${messageCount}</span></#if></a>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;" kit-target data-options="{url:'/article/articleList',icon:'&#xe658;',title:'点位2',id:'966'}">点位2</a>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">点位3</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;">子级点位1</a></dd>
                        <dd><a href="javascript:;">子级点位2</a></dd>
                    </dl>
                </li>
            </ul>
        </div>

        <!-- 区域开始 -->
        <ul class="layui-nav layui-layout-right kit-nav">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <i class="layui-icon">&#xe63f;</i> 皮肤</a>
                </a>
                <dl class="layui-nav-child skin">
                    <dd><a href="javascript:;" data-skin="default" style="color:#393D49;"><i class="layui-icon">&#xe658;</i> 默认</a></dd>
                    <dd><a href="javascript:;" data-skin="orange" style="color:#ff6700;"><i class="layui-icon">&#xe658;</i> 橘子橙</a></dd>
                    <dd><a href="javascript:;" data-skin="green" style="color:#00a65a;"><i class="layui-icon">&#xe658;</i> 春天绿</a></dd>
                    <dd><a href="javascript:;" data-skin="pink" style="color:#FA6086;"><i class="layui-icon">&#xe658;</i> 少女粉</a></dd>
                    <dd><a href="javascript:;" data-skin="blue.1" style="color:#00c0ef;"><i class="layui-icon">&#xe658;</i> 天空蓝</a></dd>
                    <dd><a href="javascript:;" data-skin="red" style="color:#dd4b39;"><i class="layui-icon">&#xe658;</i> 枫叶红</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <!-- currentPrincipal参数在常量文件中 -->
                    <#assign currentUser = Session["currentPrincipal"]>
                    <img src="<#if currentUser?? && currentUser.photo??>${currentUser.photo}<#else>/images/default_photo_2.png</#if>" alt="" class="layui-nav-img">${currentUser.realName!}
                </a>
                <#if currentUser?? && currentUser.id??>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" kit-target data-options="{url:'/user/view?id=${currentUser.id}',icon:'&#xe658;',title:'基本资料',id:'688'}"><span>基本资料</span></a></dd>
                        <dd><a href="javascript:;" kit-target data-options="{url:'/user/editPasswdForm?id=${currentUser.id}',icon:'&#xe658;',title:'安全设置',id:'689'}"><span>安全设置</span></a></dd>
                    </dl>
                </#if>
            </li>
            <li class="layui-nav-item">
                <a href="/logout"><i class="fa fa-sign-out" aria-hidden="true"></i> 注销</a>
            </li>
        </ul>
        <!-- 区域结束 -->
    </div>
    <!-- 头部区域结束 -->

    <!-- 区域开始 -->
    <#macro tree data start end>
        <#if (start=="start")>
        <div class="layui-side layui-nav-tree layui-bg-black kit-side">
            <div class="layui-side-scroll">
                <div class="kit-side-fold"><i class="fa fa-navicon" aria-hidden="true"></i></div>
                <ul class="layui-nav layui-nav-tree" lay-filter="kitNavbar" kit-navbar>
        </#if>

        <#list data as child>
            <#if child.children?size gt 0>
                <li class="layui-nav-item">
                    <a href="javascript:;" class=""><i aria-hidden="true" class="layui-icon">${child.icon}</i><span>${child.name}</span></a>
                    <dl class="layui-nav-child">
                        <@tree data=child.children start="" end=""/>
                    </dl>
                </li>
            <#else>
                <dd>
                    <a href="javascript:;" kit-target data-options="{url:'${child.url}',icon:'${child.icon}',title:'${child.name}',id:'${child.id}'}">
                        <i class="layui-icon">${child.icon}</i><span> ${child.name}</span></a>
                </dd>
            </#if>
        </#list>

        <#if (end=="end")>
        </ul>
        </div>
        </div>
        </#if>
    </#macro>
    <!-- 区域结束 -->

    <@tree data=menu start="start" end="end"/>
    <div class="layui-body" id="container">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63e;</i> 请稍等...</div>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        2019 &copy;
        <a target="_blank" href="http://www.yhsmy.com">永红山盟院</a>
    </div>
</div>
</body>
<script src="/plugin/layui/layui.js"></script>
<script src="/js/tools/main.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script type="text/javascript">
    //pullMessage(1, 'messageCount');
</script>
</html>