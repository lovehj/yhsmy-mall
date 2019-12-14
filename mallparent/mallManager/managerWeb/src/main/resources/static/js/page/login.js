$(function(){
    layui.use(['form', 'layer'], function(){
        var form = layui.form, layer = layui.layer;
        form.verify({
            username:function(v){
                if(v.trim() == '' || v.trim() == 'undefined') {
                    return '用户名不能为空！';
                }
            },
            password:function (v) {
                if(v.trim() == '' || v.trim() == 'undefined') {
                    return '密码不能为空！';
                }
            }
        })
        form.render();

        form.on('submit(login)',function (data) {
            $.post('/login',data.field, function (res) {
                if(res.status == 200) {
                    location.href = '/home';
                } else {
                    layer.alert(res.msg, {icon: 5,anim:6,offset: 't'});
                    $('[name="username"]').focus();
                    $('[name="password"]').val('');
                }
            });
            return false;
        })
    })
})

if(window != top) {
    top.location.href = location.href;
}