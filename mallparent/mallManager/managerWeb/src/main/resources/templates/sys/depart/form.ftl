<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>菜单编辑</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
</head>
<body>
    <div class="x-body">
        <form class="layui-form layui-form-pane" style="margin-left: 20px;">
            <input type="hidden" name="id" value="${depart.id!}">

            <div class="h400">
                <div class="wml25">
                    <div class="layui-form-item">
                        <label for="ctype" class="layui-form-label">
                            <span class="x-red">*</span>部门名称
                        </label>
                        <div class="layui-input-inline">
                            <input id="departName" class="layui-input" name="name" value="${depart.name!}" autocomplete="off" lay-verify="departName">
                        </div>
                        <div id="ms" class="layui-form-mid layui-word-aux">
                            <span class="x-red">*</span><span id="ums">必须填写</span>
                        </div>
                    </div>

                    <div class="layui-form-item" id="pDiv">
                        <label for="pName" class="layui-form-label">上级部门</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="pid" id="pid" value="${depart.pid!}">
                            <input id="departPname" class="layui-input"  onclick="showDepartTree()" value="${parentName!}" autocomplete="off">
                        </div>
                        <div id="departTreeNode" class="menuTree">
                            <div id="departTree"></div>
                        </div>
                    </div>


                    <div class="layui-form-item">
                        <label for="remark" class="layui-form-label">
                            菜单备注
                        </label>
                        <div class="layui-input-inline">
                            <input id="remark" class="layui-input" name="remark" value="${depart.remark!}" autocomplete="off" lay-verify="remark">
                        </div>
                    </div>

                </div>
            </div>

            <div class="form-submit-btn-group">
                <div class="layui-form-item">
                    <button class="layui-btn layui-btn-normal" lay-filter="departEdit" lay-submit="">保存</button>
                    <button class="layui-btn layui-btn-primary" id="departClose">取消</button>
                </div>
            </div>
        </form>
    </div>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(['form','layer'],function () {
        var form = layui.form,layer = layui.layer;
        layui.tree({
            elem:'#departTree',
            nodes:${departTree},
            click:function (node) {
                $('#pid').val(node.id);
                $('#departPname').val(node.name);
                $('#departTree').toggle();
            }
        });

        form.verify({
            departName:function (v) {
                if(v == '') return '部门名称不能为空!';
                if(v.length>40) return '部门名称控制在40个字符内！';
            },
            remark:function (v) {
                if(v.length>120) return '部门备注控制在120个字符内！';
            },
        });

        form.on('submit(departEdit)', function (data) {
            //var idx = parent.layer.getFrameIndex(window.name);
            postAjaxNoTable('/depart/edit',data.field)
            return false;
        });

        $('#departClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });

        form.render();
    });

    function showDepartTree() {
        var p = $('#departPname'), cityObj = p, cityObjOffset = p.offset(),width = p.css('width');
        $('#departTreeNode').css({
            left:cityObjOffset.left+'px',
            top:cityObjOffset.top + cityObj.outerHeight()+'px',
            width: width,
            border:'1px solid #e6e6e6'
        }).slideDown('fast');
        $('body').bind('mousedown',onBodyDown);
        $('#departTreeNode').css('display','inline');
    }

    function onBodyDown() {
        if (!( event.target.id == 'departTreeNode' || $(event.target).parents('#departTreeNode').length > 0)) {
            hideDepartMenu();
        }
    }

    function hideDepartMenu() {
        $('#departTreeNode').fadeOut('fast');
        $('body').unbind('blur', onBodyDown);
    }
    </script>
</html>