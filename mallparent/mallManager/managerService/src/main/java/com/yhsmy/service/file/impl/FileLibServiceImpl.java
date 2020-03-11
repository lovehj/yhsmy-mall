package com.yhsmy.service.file.impl;

import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.file.FileLib;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.ServiceException;
import com.yhsmy.mapper.file.FileLibMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.file.FileLibServiceI;
import com.yhsmy.utils.UUIDUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/1 17:47
 **/
@Service
@Transactional(readOnly = true)
public class FileLibServiceImpl implements FileLibServiceI {

    @Autowired
    private FileLibMapper fileLibMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Override
    @Transactional(readOnly = false)
    public Json formSubmit(FileLib fileLib, User user) {
        if (StringUtils.isNotBlank(fileLib.getId())) {
            UpdateMap updateMap = new UpdateMap("smy_file_lib");
            updateMap.addField("tableId", fileLib.getTableId());
            updateMap.addField("tableName", fileLib.getTableName());
            if (StringUtils.isNotEmpty(fileLib.getFileName())) {
                updateMap.addField("fileName", fileLib.getFileName());
            }
            if (StringUtils.isNotEmpty(fileLib.getFilePath())) {
                updateMap.addField("filePath", fileLib.getFilePath());
            }
            if (fileLib.getFileSize() > 0L) {
                updateMap.addField("fileSize", fileLib.getFileSize());
            }
            if (StringUtils.isNotEmpty(fileLib.getContentType())) {
                updateMap.addField("contentType", fileLib.getContentType());
            }
            updateMap.addField("remark", fileLib.getRemark());
            if (this.mybatisMapper.update(updateMap) <= 0) {
                return Json.fail();
            }
        } else {
            fileLib.setId(UUIDUtil.generateUUID());
            fileLib.setUserId(user.getId());
            fileLib.setTableId("");
            fileLib.setTableName("");
            fileLib.setRemark("");
            fileLib.setState(NormalEnum.NORMAL.getKey());
            fileLib.setCreator(user.getRealName());
            fileLib.setCreateTime(LocalDateTime.now());
            fileLibMapper.addFile(fileLib);
        }
        return Json.ok(fileLib);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateTableInfo(String id, String tableId, String tableName) {
        // 更新附件表信息
        if (StringUtils.isNotBlank(id)) {
            FileLib fileLib = fileLibMapper.findFileLibById(id);
            if (fileLib != null) {
                UpdateMap fileUpdateMap = new UpdateMap("smy_file_lib");
                fileUpdateMap.addField("tableId", tableId);
                fileUpdateMap.addField("tableName", tableName);
                fileUpdateMap.addWhere("id", fileLib.getId());
                if (this.mybatisMapper.update(fileUpdateMap) <= 0) {
                    throw new ServiceException("更新附件失败!");
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public Json multiFileLibSubmit(List<FileLib> fileLibs, User user) {
        if (fileLibs.isEmpty()) {
            return Json.fail();
        }
        List<FileLib> resultList = new ArrayList<>(fileLibs.size());
            for (FileLib fileLib : fileLibs) {
                Json result = this.formSubmit(fileLib, user);
                if (result.getStatus() == Json.SUC_CODE) {
                    try {
                        FileLib lib = new FileLib(), resultFileLib = (FileLib) result.getObj();
                        BeanUtils.copyProperties(lib, resultFileLib);
                        resultList.add(lib);
                    } catch (Exception e) {
                        throw new ServiceException("更新失败！");
                    }
                } else {
                    throw new ServiceException("更新失败！");
                }

        }
        return Json.ok(resultList);
    }


}
