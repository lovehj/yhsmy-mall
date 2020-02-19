<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>内容分类编辑</title>
</head>
<body>
<div class="x-body">
    <form class="layui-form" style="margin-left: -20px; margin-top: 10px;">
        <input type="hidden" name="cateId" value="${cate.cateId!}">
        <div class="wml25">
            <div class="layui-form-item">
                <label for="ctype" class="layui-form-label">
                    <span class="x-red">*</span>分类名称
                </label>
                <div class="layui-input-inline" >
                    <input class="layui-input" lay-verify="cateName" name="cateName" value="${cate.cateName!}" autocomplete="off">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">
                    <span class="x-red">*</span>分类类型
                </label>
                
                <div class="layui-input-inline" >
                    <select name="ctype" class="layui-form-select">
                        <#list cateType as ccList>
                            <#list ccList?keys as cKey>
                                <option value="${cKey}" <#if cKey?number == cate.ctype>selected</#if>>${ccList[cKey]}</option>
                            </#list>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">上级分类</label>
                <div class="layui-input-inline">
                    <input type="hidden" name="catePid" id="contentCatePid" value="${cate.catePid!''}">
                    <input class="layui-input" onclick="showContentCateTree()" id="contentCatePname" value="${cate.catePname!}" readonly="readonly" autocomplete="off">
                </div>
                <div id="cateContentTreeNode" class="menuTree">
                    <div id="cateContentTree"></div>
                </div>
            </div>

        </div>

        <div class="form-submit-btn-group">
            <div class="layui-form-item">
                <a class="layui-btn layui-btn-normal" lay-filter="cateContentEdit" lay-submit="">保存</a>
                <a class="layui-btn layui-btn-primary" id="cateContentClose">取消</a>
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
            elem: '#cateContentTree',
            nodes: ${cateTree},
            click: function (node) {
                $('#contentCatePid').val(node.cateId);
                $('#contentCatePname').val(node.cateName);
                $('#cateContentTree').toggle();
            }
        })

        form.verify({
            cateName:function (v) {
                if(v == '') return '类型名称不能为空!';
            },
        });

        form.on('submit(cateContentEdit)', function (data) {
            postAjaxNoTable('/content/cateFormSubmit', data.field);
            return false;
        });

        $('#cateContentClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });

        form.render();
    })

    function showContentCateTree() {
        var p = $('#contentCatePname'), cityObj = p, cityObjOffset = p.offset(),width = p.css('width');
        $('#cateContentTreeNode').css({
            left:cityObjOffset.left+'px',
            top:cityObjOffset.top + cityObj.outerHeight()+'px',
            width: width,
            border:'1px solid #e6e6e6'
        }).slideDown('fast');
        $('body').bind('mousedown',onCategoryBodyDown);
        $('#cateContentTreeNode').css('display','inline');
    }

    function onCategoryBodyDown() {
        if (! ( event.target.id == 'categoryTreeNode' || $(event.target).parents('#cateContentTreeNode').length > 0)) {
            hideCategory();
        }
    }

    function hideCategory() {
        $('#cateContentTreeNode').fadeOut('fast');
        $('body').unbind('blur', onCategoryBodyDown);
    }
</script>
</html>