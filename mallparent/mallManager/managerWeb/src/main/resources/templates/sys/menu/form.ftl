<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/includs/header.ftl"/>
    <title>菜单编辑</title>
    <script src="/plugin/layui/layui.all.js"></script>
</head>
<body>
    <div class="x-body">
        <form class="layui-form layui-form-pane" style="margin-left: 20px;">
            <input type="hidden" name="id" value="${menu.id!}">
            <div class="h500">
                <div class="wml25">
                    <div class="layui-form-item">
                        <label for="ctype" class="layui-form-label">
                            <span class="x-red">*</span>类型
                        </label>
                        <div class="layui-input-block" style="width:190px;">
                            <select name="ctype" id="ctype" lay-verify="ctype" lay-filter="ctype">
                                <option value="0" <#if menu.ctype==0>selected</#if>>目录</option>
                                <option value="1" <#if menu.ctype==1>selected</#if>>菜单</option>
                                <option value="2" <#if menu.ctype==2>selected</#if>>按钮</option>
                            </select>
                        </div>
                    </div>

                    <div class="layui-form-item" id="pDiv">
                        <label for="pName" class="layui-form-label">父级菜单</label>
                        <div class="layui-input-inline">
                            <input type="hidden" name="pid" id="pid" value="${menu.pid!}">
                            <input id="pName" class="layui-input" <#if menu.id??>disabled</#if> onclick="showTree()" lay-verify="pName" value="${menu.parentName!}" autocomplete="off">
                        </div>
                        <div id="treeNode" class="menuTree">
                            <div id="tree"></div>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="name" class="layui-form-label">
                            <span class="x-red">*</span>菜单名称
                        </label>
                        <div class="layui-input-inline">
                            <input id="name" class="layui-input" name="name" value="${menu.name!}" autocomplete="off" lay-verify="name">
                        </div>
                        <div id="ms" class="layui-form-mid layui-word-aux">
                            <span class="x-red">*</span><span id="ums">必须填写</span>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="url" class="layui-form-label">
                            <span class="x-red">*</span>菜单地址
                        </label>
                        <div class="layui-input-inline">
                            <input id="url" class="layui-input" name="url" value="${menu.url!}" autocomplete="off" lay-verify="url">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="permission" class="layui-form-label">
                            <span class="x-red">*</span>菜单权限
                        </label>
                        <div class="layui-input-inline">
                            <input id="permission" class="layui-input" name="permission" value="${menu.permission!}" lay-verify="permission" autocomplete="off">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label for="icon" class="layui-form-label">
                            <span class="x-red">*</span>图标
                        </label>
                        <div class="layui-input-inline">
                            <div style="margin-top: 5px; margin-left: 20px;">
                                <ul>
                                    <li style="display: inline-block;width: 50px;" id="menu-icon"><i class="layui-icon" id="icon"  style="font-size: 25px;">${menu.icon!}</i></li>
                                    <li style="display: inline-block;"><i class="layui-btn layui-btn-primary layui-btn-sm" id="select_icon">选择图标</i></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div class="form-submit-btn-group">
                <div class="layui-form-item">
                    <button class="layui-btn layui-btn-normal" lay-filter="edit" lay-submit="">保存</button>
                    <button class="layui-btn layui-btn-primary" id="close">取消</button>
                </div>
            </div>
        </form>
    </div>
</body>
<script>
    layui.use(['form','layer'],function () {
        $ = layui.jquery;
        var form = layui.form,layer = layui.layer;
        layui.tree({
            elem:'#tree',
            nodes:${menus},
            click:function (node) {
                if(node.ctype == 2) {
                    layer.msg('请勿选择按钮', {icon: 5,anim:6});
                    return false;
                }
                $('#pid').val(node.id);
                $('#pName').val(node.name);
                $('#tree').hide(); // 把树隐藏
            }
        });

        var type = $('#ctype');
        form.verify({
            ctype:function (v) {
                if(v == '') return '类型不能为空!';
            },
            pName:function (v) {
                if(type.val() > 0 && v == '') return '父菜单不能为空!';
            },
            name:function (v) {
                if(v.trim() == '') return '菜单名称不能为空';
            },
            url:function (v) {
                if(type.val()==0){
                    $('#url').val('');
                }
                if(type.val() > 0 && v.trim() == '') {
                    return 'url地址不能为空!';
                }
            },
            permission:function (v) {
                if(type.val()==0){
                    $('#permission').val('');
                }
                if(type.val() > 0 && v.trim() == '') {
                    return '权限地址不能为空!';
                }
            }
        });

        form.on('select(ctype)',function (data) {
            var val = data.value;
            if(val == 0) { // 目录及菜单
                $('#pid').val('');
                dOs('pName',true);
                dOs('url',true);
                dOs('permission',true);
            } else {
                dOs('pName',false);
                dOs('url',false);
                dOs('permission',false);
            }
        })

        form.on('submit(edit)', function (data) {
            var idx = parent.layer.getFrameIndex(window.name);
            data.field['icon']= $('#icon').attr('val');
            $.ajax({
                url:'/menu/edit',
                type:'post',
                data:data.field,
                dataType: "json",
                traditional: true,
                success: function (res) {
                    window.top.layer.msg(res.msg,{icon:6,offset: 'rb',area:['120px','80px'],anim:2});
                    if(res.status == 200) {
                        parent.layer.close(idx);
                        parent.location.replace(parent.location.href);
                    } else if(res.obj != null) {
                        var obj = res.obj;
                        for(var o in obj) {
                            window.top.layer.tips(obj[o],'[name="'+o+'"]', {tips:2});
                        }
                    }
                },
                error:function (res) {
                    window.top.layer.msg(res.msg,{icon:5,offset: 'rb',area:['120px','80px'],anim:2});
                    parent.layer.close(idx);
                }
            });
            return false;
        });

        $('#close').click(function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });

        form.render();
    });

    $('#select_icon').click(function () {
        parent.layer.open({
            id:'icon',
            type: 2,
            area: ['800px','600px'],
            shade: 0.4,
            zIndex: layer.zIndex,
            title: '图标',
            content: '/plugin/html/icon.html'
        });
    });

    /**
     *
     * @param id 元素Id
     * @param flag true=禁止输入，false=允许输入
     */
    function dOs(id, flag) {
        var $id = $('#'+id);
        if(flag) {
            $id.val('');
            $id.attr('disabled','disabled').css('background','#e6e6e6');
        } else {
            $id.removeAttr('disabled').css('background','white');
        }
    }

    function showTree() {
        var p = $('#pName'), cityObj = p, cityObjOffset = p.offset(),width = p.css('width');
        $('#treeNode').css({
            left:cityObjOffset.left+'px',
            top:cityObjOffset.top + cityObj.outerHeight()+'px',
            width: width,
            border:'1px solid #e6e6e6'
        }).slideDown('fast');
        $('body').bind('mousedown',onBodyDown);
        $('#treeNode').css('display','inline');
    }
    
    function onBodyDown() {
        if (! ( event.target.id == 'treeNode' || $(event.target).parents('#treeNode').length > 0)) {
            hideMenu();
        }
    }
    
    function hideMenu() {
        $('#treeNode').fadeOut('fast');
        $('body').unbind('blur', onBodyDown);
    }

    $(function () {
        if($('#ctype').val() == 0) {
            $('#pid').val('');
            dOs('pName',true);
            dOs('url',true);
            dOs('permission',true);
        }
    })
</script>
</html>