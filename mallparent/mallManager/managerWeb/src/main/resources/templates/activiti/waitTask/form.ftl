<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>审批页面</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
</head>
<body class="x-body">
    <#if iframeUrl??>
        <div style="width:100%; height:280px;overflow: hidden;">
            <iframe style="width:100%;height:100%;border: none;" src="${iframeUrl!}"></iframe>
        </div>
    </#if>

    <form class="layui-form" style="margin-left: 20px;">
        <div style="width:98%;height:80px;overflow: hidden;">
            <input type="hidden" name="taskId" value="${taskId}">
            <input type="hidden" name="flag">

            <div class="layui-form-item">
                <label class="layui-form-label">审批意见</label>
                <div class="layui-input-inline">
                    <textarea name="opinion" id="opinion" lay-verify="opinion" class="layui-textarea textarea-ext" style="min-height: 70px; min-width: 450px;"></textarea>
                </div>
            </div>
        </div>

        <div class="form-submit-btn-group">
            <div class="layui-form-item">
                <#if hasOperaBtn>
                    <button class="layui-btn layui-btn-normal" lay-filter="auditPass" lay-submit="">审批通过</button>
                    <button class="layui-btn layui-btn-normal" lay-filter="auditBack" lay-submit="">审批不通过</button>
                </#if>
                <button class="layui-btn layui-btn-primary" id="auditClose">取消</button>
            </div>
        </div>
    </form>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(["form", "layer"],function () {
        var form = layui.form, layer = layui.layer;

        form.verify({
            opinion: function (v) {
                if(v.trim() == '' ) {
                    return '审批意见不能为空!';
                }
            },
        })

        form.on("submit(auditPass)", function (data) {
            data.field.flag=true;
            postAjax('/wait/complete',data.field,'waitTaskList');
            return false;
        })

        form.on("submit(auditBack)", function (data) {
            data.field.flag=false;
            postAjax('/wait/complete',data.field,'waitTaskList');
            return false;
        })

        $('#auditClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
            return false;
        });

        form.render();
    })

</script>
</html>