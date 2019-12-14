package com.yhsmy.entity.vo.sys;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类用来组装mybatis在更新或删除数据时组拼条件或值
 *
 * @auth 李正义
 * @date 2019/11/24 14:10
 **/
public class UpdateMap {
    private String table;

    private List<Field> fields = new ArrayList<> ();

    private List<Field> where = new ArrayList<> ();

    public UpdateMap (String table) {
        super ();
        this.table = table;
    }

    public void addFieldNull (String name) {
        fields.add (new Field (name, true));
    }

    public void addField (String name, LocalDate localDate) {
        fields.add (new Field (name, localDate));
    }

    public void addField (String name, LocalTime localTime) {
        fields.add (new Field (name, localTime));
    }

    public void addField (String name, LocalDateTime localDateTime) {
        fields.add (new Field (name, localDateTime));
    }

    public void addField (String name, int value) {
        fields.add (new Field (name, String.valueOf (value)));
    }

    public void addField (String name, float value) {
        fields.add (new Field (name, String.valueOf (value), null));
    }

    public void addField (String name, String value) {
        fields.add (new Field (name, value));
    }

    public void addField (String name, String operator, int num) {
        fields.add (new Field (name, null, operator, num));
    }

    public void addField (String name, String operator, String numStr) {
        fields.add (new Field (name, null, operator, numStr));
    }

    public void addField (String name, String value, boolean sqlFrag) {
        fields.add (new Field (name, value, sqlFrag));
    }

    public void addWhere (String name, String value) {
        where.add (new Field (name, value, "="));
    }

    public void addWhere (String name, int value) {
        where.add (new Field (name, String.valueOf (value), "="));
    }

    public void addWhere (String name, String value, String operator) {
        where.add (new Field (name, value, operator));
    }

    public void addWhere (String name, int value, String operator) {
        where.add (new Field (name, String.valueOf (value), operator));
    }

    public static class Field {
        private String name;

        private String value;

        private LocalDate localDateValue;

        private LocalDateTime localDateTimeValue;

        private LocalTime localTimeValue;

        private String operator;

        private String numStr;

        private int num;

        private float floatNum;

        private boolean setNull = false;

        private boolean sqlFrag = false;

        public Field (String name, LocalDate localDate) {
            this.name = name;
            this.localDateValue = localDate;
        }

        public Field (String name, LocalDateTime localDateTime) {
            this.name = name;
            this.localDateTimeValue = localDateTime;
        }

        public Field (String name, LocalTime localTime) {
            this.name = name;
            this.localTimeValue = localTime;
        }

        public Field (String name, boolean setNull) {
            this.name = name;
            this.setNull = setNull;
        }

        public Field (String name, String value, boolean sqlFrag) {
            this.name = name;
            this.value = value;
            this.sqlFrag = sqlFrag;
        }

        public Field (String name, String value, String operator) {
            this.name = name;
            this.value = value;
            this.operator = operator;
        }

        public Field (String name, String value, String operator, int num) {
            this.name = name;
            this.value = value;
            this.operator = operator;
            this.num = num;
        }

        public Field (String name, String value, String operator, float num) {
            this.name = name;
            this.value = value;
            this.operator = operator;
            this.floatNum = num;
        }

        public Field (String name, String value, String operator, String numStr) {
            this.name = name;
            this.value = value;
            this.operator = operator;
            this.numStr = numStr;
        }

        public Field (String name, String value) {
            super ();
            this.name = name;
            this.value = value;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public String getValue () {
            return value;
        }

        public void setValue (String value) {
            this.value = value;
        }

        public LocalDate getLocalDateValue () {
            return localDateValue;
        }

        public void setLocalDateValue (LocalDate localDateValue) {
            this.localDateValue = localDateValue;
        }

        public LocalDateTime getLocalDateTimeValue () {
            return localDateTimeValue;
        }

        public void setLocalDateTimeValue (LocalDateTime localDateTimeValue) {
            this.localDateTimeValue = localDateTimeValue;
        }

        public LocalTime getLocalTimeValue () {
            return localTimeValue;
        }

        public void setLocalTimeValue (LocalTime localTimeValue) {
            this.localTimeValue = localTimeValue;
        }

        public String getOperator () {
            return operator;
        }

        public void setOperator (String operator) {
            this.operator = operator;
        }

        public int getNum () {
            return num;
        }

        public void setNum (int num) {
            this.num = num;
        }

        public boolean isSetNull () {
            return setNull;
        }

        public void setSetNull (boolean setNull) {
            this.setNull = setNull;
        }

        public boolean isSqlFrag () {
            return sqlFrag;
        }

        public void setSqlFrag (boolean sqlFrag) {
            this.sqlFrag = sqlFrag;
        }

        public String getNumStr () {
            return numStr;
        }

        public void setNumStr (String numStr) {
            this.numStr = numStr;
        }

        public float getFloatNum () {
            return floatNum;
        }

        public void setFloatNum (float floatNum) {
            this.floatNum = floatNum;
        }
    }

    public String getTable () {
        return table;
    }

    public void setTable (String table) {
        this.table = table;
    }

    public List<Field> getFields () {
        return fields;
    }

    public void setFields (List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getWhere () {
        return where;
    }

    public void setWhere (List<Field> where) {
        this.where = where;
    }
}
