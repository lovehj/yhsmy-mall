package com.yhsmy.mapper.file;

import com.yhsmy.entity.vo.file.FileLib;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/1 17:43
 **/
public interface FileLibMapper {

    /**
     * 保存文件
     *
     * @param file 文件对象
     */
    public void addFile (FileLib file);

    /**
     * 根据ID查询附件
     *
     * @param id
     * @return FileLib
     */
    public FileLib findFileLibById (@Param("id") String id);

    /**
     * 根据来源表的ID查询附件
     * @param tableId 来源表的ID
     * @param tableName 来源于那张表
     * @param tableType  0=图片 1=附件
     * @return
     */
    public List<FileLib> findFileLibList(@Param ("tableId")String tableId, @Param ("tableName") String tableName, @Param ("tableType")int tableType);

}
