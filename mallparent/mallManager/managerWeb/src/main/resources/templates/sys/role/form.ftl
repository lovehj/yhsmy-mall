<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>角色编辑</title>
    <link rel="stylesheet" href="/plugin/ztree/css/metroStyle/metroStyle.css">
</head>
<body>
    <div class="x-body">
        <form class="layui-form layui-form-pane" style="margin-left: 20px;">
            <input type="hidden" name="roleId" value="${role.roleId!}">
            <div class="form-h400">
                <div class="layui-form-item">
                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                        <legend style="font-size:16px;">角色信息</legend>
                    </fieldset>
                </div>

                <div class="layui-form-item">
                    <label for="roleName" class="layui-form-label">
                        <span class="x-red">*</span>角色名称
                    </label>

                    <div class="layui-input-inline">
                        <input id="roleName" name="roleName" value="${role.roleName!}" lay-verify="rolename" autocomplete="off" class="layui-input">
                    </div>

                    <div id="ms" class="layui-form-mid layui-word-aux">
                        <span class="x-red">*</span><span id="ums">角色名称必填</span>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label for="remark" class="layui-form-label">
                            <span class="x-red">*</span>角色备注
                        </label>
                        <div class="layui-input-inline">
                            <input id="remark" name="remark" value="${role.remark!}" lay-verify="remark"  autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                        <legend style="font-size:16px;">授权</legend>
                    </fieldset>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label for="menuIds" class="layui-form-label">
                            <span class="x-red">*</span>权限选择
                        </label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="menuIds" value="${role.menuIds!}">
                            <ul id="menuTree" class="ztree"/>
                        </div>
                    </div>
                </div>
                <div style="height: 60px"></div>

                <div class="form-submit-btn-group">
                    <div class="layui-form-item">
                        <#if !isView>
                            <button  class="layui-btn layui-btn-normal" lay-filter="save" lay-submit>保存</button>
                        </#if>
                        <button  class="layui-btn layui-btn-primary" id="close">取消</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/ztree/js/jquery.ztree.core.js"></script>
<script src="/plugin/ztree/js/jquery.ztree.excheck.js"></script>
<script src="/plugin/tools/tool.js"></script>
<#if role?? && role.id??><script src="/plugin/tools/update-settings.js"></script></#if>
<script>
    var settings = {
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    var zNodes = ${menus};
    $(document).ready(function(){
        $.fn.zTree.init($("#menuTree"), settings, zNodes);
    });

    function beforeAsync(treeId, treeNode) {
        console.log(treeId);
        console(treeNode);
    }

    layui.use(['form','layer'],function () {
        $ = layui.jquery;
        var form = layui.form,
            layer = layui.layer;

        // 自定义验证
        form.verify({
            rolename: function (v) {
                if(v.trim() == '') {
                    return '角色名不能为空!';
                }
            }
        })

        $('#close').click(function(){
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });

        form.on('submit(save)',function (data) {
            var ztree = $.fn.zTree.getZTreeObj("menuTree");
            var jsonArr = ztree.getCheckedNodes(true);
            var menus = [];
            for(var item in jsonArr) {
                menus.push(jsonArr[item].id)
            }
            data.field.menuIds = menus.join(",");
            postAjax('/role/edit', data.field,'roleList');
            return false;
        })
    })
</script>
</html>