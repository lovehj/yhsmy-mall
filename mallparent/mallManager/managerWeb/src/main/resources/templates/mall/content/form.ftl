<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>内容编辑</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
</head>
<body>
<div class="x-body">
    <form class="layui-form" style="margin-left: 20px;">
        <input type="hidden" id="picUrl" name="picUrl" value="${_content.picUrl!}">
        <input type="hidden" id="contentPicId" name="contentPicId" lay-verify="contentPicId">
        <input type="hidden" name="contentId" value="${_content.contentId!}">
        <div class="layui-form-item">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                <legend style="font-size:16px;">商品图片<#if !contentView>上传<#else>信息</#if></legend>
            </fieldset>
            <div class="layui-input-inline">
                <#if !contentView>
                    <div class="layui-upload-drag" style="margin-left:10%;" id="contentUpload">
                        <i style="font-size:30px;" class="layui-icon"></i>
                        <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>
                    </div>
                </#if>
            </div>

            <div class="layui-input-inline">
                <div id="contentImgPreview" style="margin-top: 20px;margin-left: 50px">
                    <#if _content.picUrl?? && _content.picUrl?length gt 5>
                        <img src="${prefix}${_content.picUrl!}" width="160px" height="150px" class="layui-upload-img">
                    </#if>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                <legend style="font-size:16px;">基础信息</legend>
            </fieldset>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">标题</label>
            <div class="layui-input-inline">
                <input name="title" value="${_content.title!}" lay-verify="title" autocomplete="off" class="layui-input w800" />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">副标题</label>
            <div class="layui-input-inline">
                <input name="subTitle" value="${_content.subTitle!}" lay-verify="subTitle" autocomplete="off" class="layui-input w800"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">所属分类</label>
            <div class="layui-input-inline">
                <input type="hidden" name="conentCateId" id="conentCateId" value="${_content.conentCateId!}" lay-verify="conentCateId">
                <input id="contentCateName" class="layui-input" onclick="showContentCateTree()" value="${_content.contentCateName!}" readonly="readonly" autocomplete="off">
            </div>

            <label class="layui-form-label">链接地址</label>
            <div class="layui-input-inline">
                <input name="linkUrl" value="${_content.linkUrl!}" lay-verify="linkUrl" autocomplete="on" class="layui-input w500" />
            </div>

            <div id="contentCateTreeNode" class="menuTree">
                <div id="contentCateTree"></div>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">内容</label>
            <div class="layui-input-inline">
                <input name="content" value="${_content.content!}" autocomplete="on" class="layui-input w800" maxlength="200">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-inline">
                <input name="description" value="${_content.description!}" autocomplete="on" class="layui-input w800" maxlength="200">
            </div>
        </div>

        <#if !noCloseBtn>
            <div class="form-submit-btn-group">
                <div class="layui-form-item">
                    <#if !contentView>
                        <@shiro.hasPermission name="content:edit">
                            <button class="layui-btn layui-btn-normal" lay-filter="contentEdit" lay-submit>保存</button>
                        </@shiro.hasPermission>
                    </#if>
                    <button class="layui-btn layui-btn-primary" id="contentClose">取消</button>
                </div>
            </div>
        </#if>
    </form>
</div>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(["form", "layer","upload"],function () {
        var form = layui.form, layer = layui.layer, upload = layui.upload;

        initSelectTree(layui,'contentCateTree',${contentCateList},'conentCateId','contentCateName');

        var contentId = $('[name="contentId"]').val();
        form.verify({
            contentPicId: function (v) {
                if((contentId == '' || contentId == null) && v.trim() == '') {
                    return '请上传内容图片!';
                }
            },

            title:function (v) {
                if(v.trim() == '') {
                    return '请填写内容标题!';
                }
            },
            subTitle:function (v) {
                if(v.trim() == '') {
                    return '请填写副标题!';
                }
            },
            conentCateId:function (v) {
                if(v.trim() == '') {
                    return '请选择内容分类！';
                }
            },
            linkUrl:function (v) {
                if(v.trim() == '') {
                    return '请填写链接地址!';
                }
            },
        })

        form.on("submit(contentEdit)", function (data) {
            postAjax('/content/formSubmit',data.field,'contentList');
            return false;
        })

        $('#contentClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
            return false;
        });

        singleUpload(upload, layer, 'contentUpload', 'contentImgPreview', 'picUrl', 'contentPicId', true)
        form.render();
    })

    function showContentCateTree() {
        var p = $('#contentCateName'), cityObj = p, cityObjOffset = p.offset(), width = p.css('width');
        $('#contentCateTreeNode').css({
            left: cityObjOffset.left + 'px',
            top: cityObjOffset.top + cityObj.outerHeight() + 'px',
            width: width,
            border: '1px solid #e6e6e6'
        }).slideDown('fast');
        $('body').bind('mousedown', onContentCateBodyDown);
        $('#contentCateTreeNode').css('display', 'inline');
    }

    function onContentCateBodyDown() {
        if (!(event.target.id == 'contentCateTreeNode' || $(event.target).parents('#contentCateTreeNode').length > 0)) {
            hideContentCateTree();
        }
    }

    function hideContentCateTree() {
        $('#contentCateTreeNode').fadeOut('fast');
        $('body').unbind('blur', onContentCateBodyDown);
    }
</script>
</html>