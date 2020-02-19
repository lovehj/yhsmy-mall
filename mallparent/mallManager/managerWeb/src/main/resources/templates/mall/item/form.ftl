<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>商品编辑</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
</head>
<body>
<div class="x-body">
    <form class="layui-form layui-form-pane" style="margin-left: 20px;">
        <input type="hidden" id="itemImg" name="itemImg" value="${item.itemImg!}">
        <input type="hidden" id="itemImgId" name="itemImgId" lay-verify="itemImgId">
        <input type="hidden" name="itemId" value="${item.itemId}">
        <div class="layui-form-item">
            <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                <legend style="font-size:16px;">商品图片<#if !itemView>上传<#else>信息</#if></legend>
            </fieldset>
            <div class="layui-input-inline">
                <#if !itemView>
                    <div class="layui-upload-drag" style="margin-left:10%;" id="itemUpload">
                        <i style="font-size:30px;" class="layui-icon"></i>
                        <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>
                    </div>
                </#if>
            </div>

            <div class="layui-input-inline">
                <div id="itemImgPreview" style="margin-top: 20px;margin-left: 50px">
                    <#if item.itemImg?? && item.itemImg?length gt 5>
                        <img src="${prefix}${item.itemImg!}" width="160px" height="150px" class="layui-upload-img">
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
            <label class="layui-form-label">商品标题</label>
            <div class="layui-input-inline">
                <input name="title" value="${item.title!}" lay-verify="title" autocomplete="off" class="layui-input w800" />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">商品卖点</label>
            <div class="layui-input-inline">
                <input name="sellPoint" value="${item.sellPoint!}" lay-verify="sellPoint" autocomplete="off" class="layui-input w800" />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">商品价格</label>
            <div class="layui-input-inline">
                <input name="price" value="${item.price!}" lay-verify="price" autocomplete="off" class="layui-input onlyPriceNum" />
            </div>

            <label class="layui-form-label">折扣价格</label>
            <div class="layui-input-inline">
                <input name="disPrice" value="${item.disPrice!}" lay-verify="disPrice" autocomplete="off" class="layui-input onlyPriceNum" >
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">商品库存</label>
            <div class="layui-input-inline">
                <input name="itemNum" value="${item.itemNum!}" lay-verify="itemNum" autocomplete="off" class="layui-input onlyNum" >
            </div>

            <label class="layui-form-label">商品分类</label>
            <div class="layui-input-inline">
                <input type="hidden" name="categoryId" id="cateId" value="${item.categoryId!}" lay-verify="cateId">
                <input id="cateName" class="layui-input" onclick="showItemCategoryTree()" value="${item.cateName!}" readonly="readonly" autocomplete="off">
            </div>

            <div id="itemCategoryTreeNode" class="menuTree">
                <div id="itemCategoryTree"></div>
            </div>
        </div>

        <#if !noCloseBtn>
            <div class="form-submit-btn-group">
                <div class="layui-form-item">
                    <#if !itemView>
                        <@shiro.hasPermission name="item:edit">
                            <button class="layui-btn layui-btn-normal" lay-filter="itemEdit" lay-submit>保存</button>
                        </@shiro.hasPermission>
                    </#if>
                    <button class="layui-btn layui-btn-primary" id="itemClose">取消</button>
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

        initSelectTree(layui,'itemCategoryTree',${categoryList},'cateId','cateName');

        var itemId = $('[name="itemId"]').val();
        form.verify({
            itemImgId: function (v) {
                if((itemId == '' || itemId == null) && v.trim() == '') {
                    return '请上传商品图片!';
                }
            },

            title:function (v) {
                if(v.trim() == '') {
                    return '请填写商品标题!';
                }
                // if(v.length > 220) {
                //     return '商品标题控制在220个字符内!';
                // }
            },
            sellPoint:function (v) {
                if(v.trim() == '') {
                    return '请填写商品卖点!';
                }
            },
            price:function (v) {
                if(v.trim() == '') {
                    return '请填写商品价格！';
                }
                if(!(/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(v))) {
                    return '商品价格格式不正确！';
                }
            },
            disPrice:function (v) {
                if(v.trim() == '') {
                    $('[name="disPrice"]').val($('[name="price"]').val());
                }
                if(!(/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(v))) {
                    return '折扣价格格式不正确！';
                }
            },
            itemNum:function (v) {
                if(v.trim() == ''){
                    return '请填写商品库存！';
                }
                if(!(/^[0-9]+$/.test(v))) {
                    return '商品价格格式不正确！';
                }
            },
            cateId:function (v) {
                if(v.trim() == '') {
                    return '商品分类未选择!';
                }
            },
        })

        form.on("submit(itemEdit)", function (data) {
            postAjax('/item/edit',data.field,'itemList');
            return false;
        })

        $('#itemClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
            return false;
        });

        singleUpload(upload, layer, 'itemUpload', 'itemImgPreview', 'itemImg', 'itemImgId')

        form.render();
    })


    function showItemCategoryTree() {
        var p = $('#cateName'), cityObj = p, cityObjOffset = p.offset(), width = p.css('width');
        $('#itemCategoryTreeNode').css({
            left: cityObjOffset.left + 'px',
            top: cityObjOffset.top + cityObj.outerHeight() + 'px',
            width: width,
            border: '1px solid #e6e6e6'
        }).slideDown('fast');
        $('body').bind('mousedown', onItemCategoryBodyDown);
        $('#itemCategoryTreeNode').css('display', 'inline');
    }

    function onItemCategoryBodyDown() {
        if (!(event.target.id == 'itemCategoryTreeNode' || $(event.target).parents('#itemCategoryTreeNode').length > 0)) {
            hideItemCategoryTree();
        }
    }

    function hideItemCategoryTree() {
        $('#itemCategoryTreeNode').fadeOut('fast');
        $('body').unbind('blur', onItemCategoryBodyDown);
    }
</script>
</html>