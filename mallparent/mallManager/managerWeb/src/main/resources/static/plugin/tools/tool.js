/**
 * @param id 弹窗ID
 * @param title 标题
 * @param url 请求的url
 * @param w 弹出层宽度（缺省调默认值）
 * @param h 弹出层高度（缺省调默认值）
 */
function popup(id, title, url, w, h) {
    if (id == 'undefined' || id == '' || id == null) {
        id = "timestemp" + (Math.random() + new Date().getTime());
    }

    if (title == null || title == '') {
        title = false;
    }

    if (url == null || url == '') {
        url = "/error/404";
    }

    if (w == null || w == '') {
        w = ($(window).width() * 0.9);
    }

    if (h == null || h == '') {
        h = ($(window).height() - 50);
    }
    layer.open({
        id: id,
        type: 2,
        area: [w + 'px', h + 'px'],
        fix: false,
        maxmin: true,
        shadeClose: true,
        shade: 0.4,
        title: title,
        content: url
    });
}

/**
 *
 * @param title 标题
 * @param url 请求的url
 * @param w 弹出层宽度（缺省调默认值）
 * @param h 弹出层高度（缺省调默认值）
 */
function dialog(title, url, w, h) {
    popup(null, title, url, w, h);
}


/**
 * @param title 标题
 * @param url 请求的url
 */
function dialogNoWOrH(title, url) {
    popup('', title, url, null, null);
}

/**
 * 全屏弹窗
 * @param id 弹窗ID
 * @param title 弹窗标题
 * @param url 加载的URL页面地址
 */
function fullDialog(id, title, url) {
    if (id == 'undefined' || id == '' || id == null) {
        id = "timestemp" + (Math.random() + new Date().getTime());
    }

    if (title == null || title == '') {
        title = false;
    }

    if (url == null || url == '') {
        url = "/error/404";
    }

    var index =layer.open({
        id: id,
        type: 2,
        area: ['600px','350px'],
        fix: false,
        maxmin: true,
        shadeClose: false,
        shade: 0.4,
        title: title,
        content: url
    });
    layer.full(index);
}

function get(url,tableId) {
    $.get(url, function (res) {
        var msg = res.msg;
        if (res.status == 200) {
            if(tableId != 'undefined' || tableId != '' || tableId != null) {
                window.parent.layui.table.reload(tableId);
                layui.table.reload(tableId);
            }
            try{
                parent.layer.close(parent.layer.getFrameIndex(window.name));
            }catch (e) {
                layer.closeAll();
            }
            window.top.layer.msg(msg, {icon: 6, offset: 'rb', area: ['120px', '80px'], anim: 2});
        } else {
            layer.msg(msg, {icon: 5, offset: 'rb', area: ['120px', '80px'], anim: 2});
        }
    })
}

/**
 *
 * @param url
 * @param data
 * @param tableId
 */
function postAjax(url, data, tableId) {
    $.post(url, data, function (res) {
        var msg = res.msg;
        if (res.status == 200) {
            if(tableId != 'undefined' || tableId != '' || tableId != null) {
                window.parent.layui.table.reload(tableId);
                layui.table.reload(tableId);
            }
            try{
                parent.layer.close(parent.layer.getFrameIndex(window.name));
            }catch (e) {
                layer.closeAll();
            }
            window.top.layer.msg(msg, {icon: 6, offset: 'rb', area: ['120px', '80px'], anim: 2});
        } else {
            layer.msg(msg, {icon: 5, offset: 'rb', area: ['120px', '80px'], anim: 2});
        }
    })
}

function postAjaxNoTable(url, data) {
    var idx = parent.layer.getFrameIndex(window.name);
    $.post(url, data, function (res) {
        window.top.layer.msg(res.msg, {icon: 6, offset: 'rb', area: ['120px', '80px'], anim: 2});
        if (res.status == 200) {
            parent.layer.close(idx);
            parent.location.replace(parent.location.href);
        } else if (res.obj != null) {
            var obj = res.obj;
            for (var o in obj) {
                window.top.layer.tips(obj[o], '[name="' + o + '"]', {tips: 2});
            }
        }
    });
}

/**
 * 提交表单
 * @param formId 表单ID
 * @param tableId 提交后刷新的表格ID
 */
function postForm(formId, tableId) {
    var form = $('#' + formId), url = form.attr("action"), params = form.serialize();
    if (url == 'undefined' || url == '') {
        layer.msg('提交的URL地址错误', {icon: 5, offset: 'rb', area: ['120px', '80px'], anim: 2});
    }
    $.post(url, params, function (res) {
        var msg = res.msg;
        if (res.status == 200) {
            window.parent.layui.table.reload(tableId);
            parent.layer.close(parent.layer.getFrameIndex(window.name));
            window.top.layer.msg(msg, {icon: 6, offset: 'rb', area: ['120px', '80px'], anim: 2});
        } else {
            layer.msg(msg, {icon: 5, offset: 'rb', area: ['120px', '80px'], anim: 2});
        }
    })
}

/**
 * 通用删除
 * @param url 删除的URL地址
 * @param data 删除时的请求参数，JSON格式
 * @param tableId 删除后刷新的tableId
 */
function delAjax(url, data, tableId) {
    $.ajax({
        url: url,
        type: "DELETE",
        data: data,
        success: function (res) {
            var msg = res.msg;
            if (res.status == 200) {
                layui.table.reload(tableId);
                try{
                    layer.close(parent.layer.getFrameIndex(window.name));
                }catch (e) {
                   layer.closeAll();
                }
                layer.msg(msg, {icon: 6, offset: 'rb', area: ['120px', '80px'], anim: 2});
            } else {
                layer.msg(msg, {icon: 5, offset: 'rb', area: ['120px', '80px'], anim: 2});
            }
        }
    });
}

function delAjaxNoTable(url, data) {
    $.ajax({
        url: url,
        type: "DELETE",
        data: data,
        success: function (res) {
            var msg = res.msg;
            if (res.status == 200) {
                layer.msg(msg, {icon: 6, offset: 'rb', area: ['120px', '80px'], anim: 2});
                location.replace(location.href);
            } else {
                layer.msg(msg, {icon: 5, offset: 'rb', area: ['120px', '80px'], anim: 2});
            }
        }
    });
}


/**
 * 初始化一个下拉树，需要引入/plugin/layuitree/layui/layui.all.js文件
 * @param layui layui对象
 * @param eleId 包裹下拉树DIV的ID属性
 * @param dataJson 初始化树需要的数据
 * @param inputForId 选中时将元素的ID属性设置到的input
 * @param inputFormName 选中时将元素的NAME属性设置到的input
 */
function initSelectTree(layui, eleId, dataJson, inputForId, inputFormName) {
    layui.tree({
        elem: '#' + eleId,
        nodes: dataJson,
        click: function (node) {
            $('#' + inputForId).val(node.id);
            $('#' + inputFormName).val(node.name);
            $('#' + eleId).toggle();
        }
    })
}

/**
 * 图片上传
 * @param upload 图片上传的upload对象
 * @param layer layer对象
 * @param eleId 图片上传的ID
 * @param previewId 图片上传成功后的预览ID
 * @param setImgPathId 设置文件上传成功后的的图片路径
 * @param setUploadFileId 设置文件上传后的图片ID
 * @param noCircle 图片不显示为一个圆形
 */
function singleUpload(upload,layer,eleId, previewId,setImgPathId, setUploadFileId, noCircle){
    upload.render({
        elem:'#'+eleId,
        url:'/file/upload',
        accept:'file',
        before: function (obj) {
            //预读，不支持ie8
            obj.preview(function(index, file, result){
                var userUpload = $('#'+previewId);
                userUpload.find('img').remove();
                if(typeof (noCircle) != "undefined" || noCircle) {
                    userUpload.append('<img src="'+ result +'" alt="'+ file.name +'" width="130px" height="130px" class="layui-upload-img">');
                } else {
                    userUpload.append('<img src="'+ result +'" alt="'+ file.name +'" width="130px" height="130px" class="layui-upload-img layui-circle">');
                }

            });
        },
        done:function (res) {
            if(res.status == 200) {
                $('#'+setImgPathId).val(res.obj.filePath);
                $('#'+setUploadFileId).val(res.obj.id);
            } else {
                layer.msg(res.msg,{icon: 5,anim: 6});
            }
        }
    });
}

function multiFileUpload(upload, layer, eleId, previewAearId) {
    upload.render({
        elem: '#' + eleId,
        url: '/file/multiFileUpload',
        accept: 'file',
        auto: false, //不允许自动上传
        multiple: true,
        drag: true,
        progress:function(n, elem) {
            //文件上传进度条
            console.log("=============进度条部分开始=============")
            console.log(n);
            console.log(elem)
            console.log("=============进度条部分结束=============")
        },
        choose: function (obj) {
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)

            obj.preview(function (index, file, result) {
               // $('#'+previewAearId).append('<img src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img multiFile">');
                var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td><img src='+result+' style="width:60px;height:60px"></td>'
                    ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
                    ,'<td>等待上传</td>'
                    ,'<td>'
//             ,'<button class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                    ,'</tr>'].join(''));
                //单个重传
                tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                });
                //删除
                tr.find('.demo-delete').on('click', function(){
                    delete files[index]; //删除对应的文件
                    tr.remove();
                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                });

                $('#'+previewAearId).append(tr);
            });
        },

        before: function (obj) {
            // 在这里设置请求参数
            console.log("=============before start=============")
            console.log(obj)
            console.log("=============before end=============")
        },

        allDone: function(obj) {
            // 请求结束操作
            console.log("=============请求结束操作开始=============")
            console.log(obj.total); //得到总文件数
            console.log(obj.successful); //请求成功的文件数
            console.log(obj.aborted); //请求失败的文件数
            console.log("=============请求结束操作结束=============")
        },

        done:function (res) {
            console.log("=============done开始=============")
            console.log(res)
            console.log("=============done结束=============")
            // if(res.status == 200) {
            //     $('#'+setImgPathId).val(res.obj.filePath);
            //     $('#'+setUploadFileId).val(res.obj.id);
            // } else {
            //     layer.msg(res.msg,{icon: 5,anim: 6});
            // }
        },
        error: function (obj) {
            layer.alert('上传失败！');
            layer.closeAll('loading'); //关闭loading
        }
    });
}


function pullMessage(ctype, eleId) {
    if(ctype == 1 && (eleId == null || eleId == 'undefined' || eleId == '')) {
        layer.alert('接收消息总数的ID不能为空!');
        return false;
    }

    // 检测浏览器的兼容性
    var pullUrl = "/message/pushNew?ctype="+ctype;
    if(window.applicationCache) {
        var source = new EventSource(pullUrl);
        source.onmessage = function (event) {
            var data = event.data;
            if(data.indexOf("noData") == -1) {
                if(ctype == 0) {
                    layer.msg(event.data, {icon: 1});
                } else if ( ctype == 1) {
                    $('#'+eleId).text(data);
                }
            }
        }
        source.onopen = function (event) {
        }
    } else {
        $.get(pullUrl,function (data) {
            if(data.indexOf("noData") == -1) {
                if(ctype == 0) {
                    alert(data);
                } else if ( ctype == 1) {
                    if(parseInt(data) <= 0) {
                        return false;
                    }
                    $('#'+eleId).text(data);
                }
            }
        })
    }

}

/**
 * 验证金额的正则表达式
 */
$('.onlyPriceNum').on('keyup',function () {
    if(!(/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/.test($(this).val()))) {
        $(this).val('0.00');
    }
})

/**
 * 验证整数的正则表达式
 */
$('.onlyNum').on('keyup',function () {
    if(!(/^[0-9]+$/.test($(this).val()))){
        $(this).val('0');
    }
})