package com.yhsmy.entity.file;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auth 李正义
 * @date 2019/12/1 17:38
 **/
@Data
public class FileLib implements Serializable {
    private static final long serialVersionUID = -7793971175504324439L;

    private String id;

    private String userId;

    private String tableId;

    private String tableName;

    private int tableType;

    private String fileName;

    private String filePath;

    private long fileSize;

    private String contentType;

    private int state;

    private String remark;

    private String creator;

    private LocalDateTime createTime;
}
