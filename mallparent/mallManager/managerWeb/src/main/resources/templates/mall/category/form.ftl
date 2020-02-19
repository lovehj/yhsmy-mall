<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>商品分类编辑</title>
</head>
<body>
    <div class="x-body">
        <form class="layui-form" style="margin-left: -20px; margin-top: 10px;">
            <input type="hidden" name="cateId" value="${category.cateId!}">
            <div class="wml25">
                <div class="layui-form-item">
                    <label for="ctype" class="layui-form-label">
                        <span class="x-red">*</span>分类名称
                    </label>
                    <div class="layui-input-inline" >
                        <input class="layui-input" lay-verify="cateName" name="cateName" value="${category.cateName!}" autocomplete="off">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label for="cateParentName" class="layui-form-label">上级分类</label>
                    <div class="layui-input-inline">
                        <input type="hidden" name="catePId" id="catePId" value="${category.catePId!}">
                        <input class="layui-input" onclick="showCategoryTree()" id="cateParentName" value="${category.cateParentName!}" readonly="readonly" autocomplete="off">
                    </div>
                    <div id="categoryTreeNode" class="menuTree">
                        <div id="categoryTree"></div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label for="remark" class="layui-form-label">
                        分类备注
                    </label>
                    <div class="layui-input-inline">
                        <textarea class="layui-textarea textarea-ext" name="remark" maxlength="200" autocomplete="off" placeholder="请输入备注">${category.remark!}</textarea>
                    </div>
                </div>

            </div>

            <div class="form-submit-btn-group">
                <div class="layui-form-item">
                    <a class="layui-btn layui-btn-normal" lay-filter="categoryEdit" lay-submit="">保存</a>
                    <a class="layui-btn layui-btn-primary" id="categoryClose">取消</a>
                </div>
            </div>
        </form>
    </div>
</body>
<script src="/plugin/layuitree/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(['form'], function(){
        var form = layui.form;

        layui.tree({
            elem: '#categoryTree',
            nodes: ${categoryList},
            click: function (node) {
                $('#catePId').val(node.cateId);
                $('#cateParentName').val(node.cateName);
                $('#categoryTree').toggle();
            }
        })

        form.verify({
            cateName:function (v) {
                if(v == '') return '类型名称不能为空!';
            },
        });

        form.on('submit(categoryEdit)', function (data) {
            postAjaxNoTable('/category/edit', data.field);
            return false;
        });

        $('#categoryClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });

        form.render();
    })

    function showCategoryTree() {
        var p = $('#cateParentName'), cityObj = p, cityObjOffset = p.offset(),width = p.css('width');
        $('#categoryTreeNode').css({
            left:cityObjOffset.left+'px',
            top:cityObjOffset.top + cityObj.outerHeight()+'px',
            width: width,
            border:'1px solid #e6e6e6'
        }).slideDown('fast');
        $('body').bind('mousedown',onCategoryBodyDown);
        $('#categoryTreeNode').css('display','inline');
    }

    function onCategoryBodyDown() {
        if (! ( event.target.id == 'categoryTreeNode' || $(event.target).parents('#categoryTreeNode').length > 0)) {
            hideCategory();
        }
    }

    function hideCategory() {
        $('#categoryTreeNode').fadeOut('fast');
        $('body').unbind('blur', onCategoryBodyDown);
    }
</script>
</html>