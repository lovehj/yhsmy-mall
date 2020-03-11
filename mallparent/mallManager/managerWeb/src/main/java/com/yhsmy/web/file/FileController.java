package com.yhsmy.web.file;

import com.yhsmy.IConstant;
import com.yhsmy.annotation.SysLog;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.LayuiEdit;
import com.yhsmy.entity.vo.file.FileLib;
import com.yhsmy.enums.UeditorTypeEnum;
import com.yhsmy.service.file.FileLibServiceI;
import com.yhsmy.util.FastdfsClientUtil;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/1 17:32
 **/
@Api("文件操作接口")
@Controller
@RequestMapping("/file")
@Scope("request")
@Slf4j
public class FileController extends BaseController {

    @Autowired
    private FastdfsClientUtil fastdfsClientUtil;

    @Autowired
    private FileLibServiceI fileLibServiceI;


    @SysLog(content = "单个文件上传", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiImplicitParam(name = "file", value = "文件对象")
    @PostMapping(value = "upload", headers = "content-type=multipart/form-data")
    @ResponseBody
    public Json upload(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            return Json.fail();
        }
        try {
            FileLib fileLib = new FileLib();
            BeanUtils.copyProperties(fileLib, fastdfsClientUtil.upload(file));
            if (StringUtils.isBlank(fileLib.getFilePath())) {
                return Json.fail("上传失败!");
            }
            return fileLibServiceI.formSubmit(fileLib, ShiroUtil.getUser());
        } catch (Exception e) {
            log.error("文件上传失败!", e);
            return Json.fail();
        }
    }

    @SysLog(content = "多个文件上传", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiImplicitParam(name = "fileList", value = "多个文件对象")
    @PostMapping(value = "multiFileUpload", headers = "content-type=multipart/form-data")
    @ResponseBody
    public Json multiFileUpload(List<MultipartFile> fileList) {
        if(fileList.isEmpty()) {
            return Json.fail();
        }
        try{
            List<FileLib> fileLibs = new ArrayList<>(fileList.size());
            for (MultipartFile file : fileList) {
                FileLib fileLib = new FileLib();
                BeanUtils.copyProperties(fileLib, fastdfsClientUtil.upload(file));
                if (StringUtils.isBlank(fileLib.getFilePath())) {
                    return Json.fail("上传失败!");
                }
                fileLibs.add(fileLib);
            }
            return fileLibServiceI.multiFileLibSubmit(fileLibs, ShiroUtil.getUser());
        }catch (Exception e) {
            log.error("文件上传失败!", e);
            return Json.fail();
        }
    }

    @SysLog(content = "layuiEdit单个文件上传", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiImplicitParam(name = "file", value = "文件对象")
    @PostMapping(value = "editorUpload", headers = "content-type=multipart/form-data")
    @ResponseBody
    public LayuiEdit editorUpload(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            return LayuiEdit.fail();
        }

        try {
            Json json = this.upload(file);
            if (json.getStatus() != Json.FAIL_CODE || json.getObj() == null) {
                return LayuiEdit.fail();
            }

            FileLib saveFileLib = (FileLib) json.getObj();
            return LayuiEdit.ok(saveFileLib.getUrlPrefix()
                    + "/" + saveFileLib.getFilePath(), saveFileLib.getFileName());
        } catch (Exception e) {
            log.error("文件上传失败!", e);
            return LayuiEdit.fail();
        }
    }

    /**
     * 百度富文本编辑器文件上传
     *
     * @param file
     * @param fileType
     * @return
     */
    @SysLog(content = "百度编辑器单个文件上传", type = SysLog.LOG_TYPE_ENUM.UPDATE)
    @ApiImplicitParam(name = "file", value = "文件对象")
    @PostMapping(value = "ueditorUpload", headers = "content-type=multipart/form-data")
    @ResponseBody
    public Map<String, Object> ueditorUpload(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "") String fileType) {
        Map<String, Object> result = new HashMap<>(1);
        result.put("state", "文件上传失败!");
        result.put("url", "");
        result.put("title", "");
        result.put("original", "");
        if (file != null && StringUtils.isNotBlank(UeditorTypeEnum.getValueByKey(fileType))) {
            try {
                FileLib fileLib = new FileLib();
                BeanUtils.copyProperties(fileLib, fastdfsClientUtil.upload(file, IConstant.FDFS_UEDITOR_GROUP));
                if (StringUtils.isBlank(fileLib.getFilePath())) {
                    return result;
                }
                Json resultJson = fileLibServiceI.formSubmit(fileLib, ShiroUtil.getUser());
                if (resultJson.getStatus() == Json.SUC_CODE) {
                    result.put("state", "SUCCESS"); // UEDITOR的规则:不为SUCCESS则显示state的内容
                    result.put("url", fastdfsClientUtil.getFdfsWebUrlPrefix() + "/" + fileLib.getFilePath());  //能访问到你现在图片的路径
                    result.put("title", fileLib.getFileName());
                    result.put("original", fileLib.getFileName());
                }
                return result;
            } catch (Exception e) {
                log.error("文件上传失败!", e);
            }
        }
        return result;
    }
}
