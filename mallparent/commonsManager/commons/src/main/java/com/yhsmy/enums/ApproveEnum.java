package com.yhsmy.enums;

import com.yhsmy.entity.Approve;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义工作流的类型
 *
 * @auth 李正义
 * @date 2019/12/20 14:39
 **/
public enum ApproveEnum {

    /**
     * 请假流
     */
    LEAVEBILL ("leaveBill", "请假流", "smy_leave", "leaveId"),

    /**
     * 商品审批流
     */
    ITEMAUDIT ("itemAudit", "商品审批流", "smy_item", "itemId"),

    /**
     * 内容审批
     */
    CONTENT ("contentAudit", "内容审批", "smy_content", "contentId");

    private String key;

    private String value;

    /**
     * 操作的业务表ID字段
     */
    private String tableIdFields;

    /**
     * 操作的业务表
     */
    private String tableName;

    private ApproveEnum (String key, String value, String tableName, String tableIdFields) {
        this.key = key;
        this.value = value;
        this.tableName = tableName;
        this.tableIdFields = tableIdFields;
    }

    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public String getTableIdFields () {
        return tableIdFields;
    }

    public void setTableIdFields (String tableIdFields) {
        this.tableIdFields = tableIdFields;
    }

    public String getTableName () {
        return tableName;
    }

    public void setTableName (String tableName) {
        this.tableName = tableName;
    }

    public static String getValueByKey (String key) {
        for (ApproveEnum approve : values ()) {
            if (approve.getKey ().equals (StringUtils.trimToEmpty (key))) {
                return approve.getValue ();
            }
        }
        return "";
    }

    public static String getTableIdFieldsByKey (String key) {
        for (ApproveEnum approve : values ()) {
            if (approve.getKey ().equals (StringUtils.trimToEmpty (key))) {
                return approve.getTableIdFields ();
            }
        }
        return "";
    }

    public static String getTableNameByKey (String key) {
        for (ApproveEnum approve : values ()) {
            if (approve.getKey ().equals (StringUtils.trimToEmpty (key))) {
                return approve.getTableName ();
            }
        }
        return "";
    }

    public static List<Approve> getList () {
        List<Approve> approveList = new ArrayList<> (1);
        for (ApproveEnum approve : values ()) {
            approveList.add (new Approve (approve.getKey (), approve.getValue ()));
        }
        return approveList;
    }
}
