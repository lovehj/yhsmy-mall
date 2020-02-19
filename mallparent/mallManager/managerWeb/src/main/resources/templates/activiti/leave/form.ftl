<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>请假编辑</title>
    <script src="/plugin/layuitree/layui/layui.all.js"></script>
</head>
<body>
<div class="x-body">
    <form class="layui-form" style="margin-left: 20px;">
        <input type="hidden" name="leaveId" value="${leave.leaveId!}">

        <div class="layui-form-item">
            <label class="layui-form-label">请假天数</label>
            <div class="layui-input-inline">
                <input name="days" value="${leave.days!}" lay-verify="days" <#if !leaveEditFlag>readonly="readonly"</#if> autocomplete="off" class="layui-input onlyPriceNum" />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">请假事由</label>
            <div class="layui-input-inline">
                <textarea class="layui-textarea textarea-ext" name="content" lay-verify="content" <#if !leaveEditFlag>readonly="readonly"</#if>>${leave.content!}</textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">开始时间</label>
            <div class="layui-input-inline">
                <input id="leaveFormStartDate" name="startDate" readonly="readonly" value="${leave.startDate!}" lay-verify="startDate" autocomplete="off" class="layui-input" />
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">结束时间</label>
            <div class="layui-input-inline">
                <input id="leaveFormEndDate" name="endDate" readonly="readonly" value="${leave.endDate!}" lay-verify="endDate" autocomplete="off" class="layui-input" >
            </div>
        </div>

        <#if leaveView && !noCloseBtn>
            <div class="layui-form-item">
                <label class="layui-form-label">审批备注</label>
                <div class="layui-input-inline">
                    <textarea class="layui-textarea textarea-ext" name="remark">${leave.remark!}</textarea>
                </div>
            </div>
        </#if>

        <#if !noCloseBtn>
            <div class="form-submit-btn-group">
                <div class="layui-form-item">
                    <#if !leaveView>
                        <@shiro.hasPermission name="leave:edit">
                            <button class="layui-btn layui-btn-normal" lay-filter="leaveEdit" lay-submit="">保存</button>

                            <#assign currentUser = Session["currentPrincipal"]>
                            <#if leave.state==1 && leave.userId == '${currentUser.id}'>
                                <button class="layui-btn layui-btn-normal" lay-filter="leaveAudit" lay-submit="">提交申请</button>
                            </#if>
                        </@shiro.hasPermission>
                    </#if>
                    <button class="layui-btn layui-btn-primary" id="leaveClose">取消</button>
                </div>
            </div>
        </#if>
    </form>
</div>
</body>
<script src="/plugin/tools/tool.js"></script>
<script>
    layui.use(["form", "laydate"],function () {
        var form = layui.form, laydate = layui.laydate;
        // 初始化日期选择器
        laydate.render({
            elem:'#leaveFormStartDate',
            value: new Date(),
            type: 'datetime',
            min:'2019-12-10 00:00:00'
        })

        laydate.render({
            elem:'#leaveFormEndDate',
            value: new Date(),
            type: 'datetime',
            min:'2019-12-10 00:00:00'
        })

        form.verify({
            days: function (v) {
                if(v.trim() == '') {
                    return '请填写请假天数!';
                }
            },

            content:function (v) {
                if(v.trim() == '') {
                    return '请填写请假事由!';
                }
            },
            startDate:function (v) {
                if(v.trim() == '') {
                    return '请选择开始时间!';
                }
            },
            endDate:function (v) {
                if(v.trim() == '') {
                    return '请选择商品价格！';
                }
            },
        })

        form.on("submit(leaveEdit)", function (data) {
            postAjax('/leave/edit',data.field,'leaveList');
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