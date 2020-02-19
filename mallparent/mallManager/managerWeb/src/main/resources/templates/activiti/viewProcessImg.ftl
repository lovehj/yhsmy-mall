<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <link rel="stylesheet" href="/plugin/ztree/css/metroStyle/metroStyle.css">
    <title>查看流程图</title>
    <style>
        .tab-2{margin-left: 40%}
    </style>
</head>
<body style="margin-left: 0px;">
    <div class="layui-tab">
        <ul class="layui-tab-title">
            <li class="layui-this">流程图</li>
            <li>流程审批</li>
        </ul>

        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <div id="image" style="width:100%;height:100%;overflow: auto;">
                    <div class="layui-form-item">
                        <image id="processImag1" style="display: none;"></image>
                        <image id="processImag2"></image>
                    </div>
                </div>
            </div>

            <div class="layui-tab-item tab-2">
                <ul class="layui-timeline"></ul>
            </div>
        </div>
    </div>
</body>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/plugin/tools/tool.js"></script>
<script type="text/javascript">
    var countNum = 0;
    layui.use(['form','layer','element'], function () {
        layer.load(1);
        $.getJSON('/act/getViewProcessImg?processInstanceId=${processInstanceId}', function (data) {
            var result = data.images;
            var task=data.taskSqu;
            if(typeof(task) != "undefined" ) {
                showTask(task);
            }
            var image1 = document.getElementById('processImag1'),
                    image2 = document.getElementById('processImag2');
            image1.src = "data:image/png;base64,"+result[0];
            image2.src = "data:image/png;base64,"+result[1];

            window.setInterval(function () {
                if(countNum == 0) {
                    $("#processImag1").show();
                    $("#processImag2").hide();
                } else {
                    $("#processImag1").hide();
                    $("#processImag2").show();
                }
                countNum++;
                if(countNum == 2) {
                    countNum = 0;
                }
            }, 1000);
            layer.closeAll('loading');
        })
    });

    function showTask(task) {
        let msg = '';
        let taskLen = task.length;
        for(let i=0; i<taskLen; i++) {
            var t = task[i];
            msg+= `<li class="layui-timeline-item">
                        <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                        <br/>
                        <i class="layui-icon layui-timeline-axis">&#xe61a;</i>
                        <div class="layui-timeline-content layui-text">
                            <h3 class="layui-timeline-title">`;
            msg+=t.taskName;
            msg+=`</h3>
                            <p>
                                <br>审批人：`;

            if(t.userName!==null){
                msg+=t.userName;
            }
            msg+=`<br>审批组：`;
            if(t.groupNames){
                t.groupNames.push('aaa');
                t.groupNames.forEach(function(v,index){
                    msg+=v;
                    msg+=(index===t.groupNames.length-1?'':'/');
                });
            }
            msg+=`
                                <br>审批时间：`;
            if(t.time!==null) {
                msg += t.time;
            }
            msg+=`
                            </p>
                        </div>
                    </li>`;
        }
        let line=document.getElementsByClassName('layui-timeline')[0];
        line.innerHTML=msg;
    }
</script>
</html>