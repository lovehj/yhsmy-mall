<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>流程模型编辑</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
</head>
<body>
<div class="x-body">
    <form class="layui-form" style="margin-left: 20px;">

        <div class="layui-form-item">
            <label class="layui-form-label">流程名称</label>
            <div class="layui-input-inline">
                <select name="processKey">
                    <#list approveList as approve>
                        <option value="${approve.key!}">${approve.value!}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">流程描述</label>
            <div class="layui-input-inline">
                <textarea class="layui-textarea textarea-ext" name="description"></textarea>
            </div>
        </div>

        <div class="form-submit-btn-group">
            <div class="layui-form-item">
                <@shiro.hasPermission name="act:modelList">
                    <button class="layui-btn layui-btn-normal" lay-filter="processActRelationEdit" lay-submit="">保存</button>
                </@shiro.hasPermission>
                <button class="layui-btn layui-btn-primary" id="processActRelationClose">取消</button>
            </div>
        </div>
    </form>
</div>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(["form","layer"],function () {
        var form = layui.form, layer = layui.layer;

        form.on("submit(processActRelationEdit)", function (data) {
            $.post('/processAct/edit', data.field, function (res) {
                if(res.status != 200) {
                    layer.alert(res.msg);
                    return false;
                }

                parent.layer.close(parent.layer.getFrameIndex(window.name));

                var index =parent.layer.open({
                    id: 'processModelAdd',
                    type: 2,
                    area: ['600px','350px'],
                    fix: false,
                    maxmin: true,
                    shadeClose: false,
                    shade: 0.4,
                    title: '新建流程',
                    content: '/act/modelForm?processActId='+res.obj+"&id="
                });
                parent.layer.full(index);

                //fullDialog('processModelAdd','新建流程','/act/modelForm?processActId='+res.obj+"&id=");
            });
            return false;
        })

        $('#leaveClose').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
            return false;
        });

        form.render();
    })
</script>
</html>