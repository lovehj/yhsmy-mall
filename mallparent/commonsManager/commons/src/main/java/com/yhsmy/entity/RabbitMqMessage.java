package com.yhsmy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2020/1/3 21:06
 **/
@Data
@AllArgsConstructor
public class RabbitMqMessage implements Serializable {

    private static final long serialVersionUID = -2248956057211501601L;
    /**
     * 操作的用户
     */
    private Object user;

    private boolean sendMessage = false;

    /**
     * 操作的类型,取值来源于RabbitMqOperaTypeEnum类
     */
    private int operaCtype;

    /**
     * 操作的标题
     */
    private String subTitle;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作的附件
     */
    private Object attchFile;

    /**
     * 是否需要操作数据库
     * ps.仅限更新和删除
     */
    private boolean operaDb = false;

    /**
     * 0=更新表数据（默认） 1=删除数据
     */
    private int operaSqlType;

    /**
     * 操作的表名
     */
    private String dbTableName;

    /**
     * 操作的数据库的字段
     */
    private Map<String, String> dbFieldsMap;

    /**
     * 操作数据库时的where条件
     */
    private Map<String, String> dbWhereMap;


}
