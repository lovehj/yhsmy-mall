package com.yhsmy.service.file;

import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.file.FileLib;
import com.yhsmy.entity.vo.sys.User;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/1 17:44
 **/
public interface FileLibServiceI {

    /**
     * 编辑文件对象
     *
     * @param file 文件对象
     * @param user 当前操作的用户
     * @return
     */
    public Json formSubmit(FileLib file, User user);

    public void updateTableInfo(String id, String tableId, String tableName);

    /**
     * 保存多个文件对象
     *
     * @param fileLibs
     * @param user
     * @return
     */
    public Json multiFileLibSubmit(List<FileLib> fileLibs, User user);
}
