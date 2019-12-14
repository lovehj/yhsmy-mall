package com.yhsmy.web.file;

import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.file.FileLib;
import com.yhsmy.service.file.FileLibServiceI;
import com.yhsmy.util.FastdfsClientUtil;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auth 李正义
 * @date 2019/12/1 17:32
 **/
@Api("文件操作接口")
@Controller
@RequestMapping("/file")
@Scope("request")
public class FileController extends BaseController {

    @Autowired
    private FastdfsClientUtil fastdfsClientUtil;

    @Autowired
    private FileLibServiceI fileLibServiceI;


    @SysLog(content = "单个文件上传", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiImplicitParam(name = "file", value = "文件对象")
    @PostMapping(value = "upload", headers = "content-type=multipart/form-data")
    @ResponseBody
    public Json upload (@RequestParam("file") MultipartFile file) {
        if (file == null) {
            return Json.fail ();
        }
        try {
            FileLib fileLib = new FileLib ();
            BeanUtils.copyProperties (fileLib, fastdfsClientUtil.upload (file));
            return fileLibServiceI.formSubmit (fileLib, ShiroUtil.getUser ());
        } catch (Exception e) {
            return Json.fail ();
        }
    }

}
