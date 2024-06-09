package com.minis.web;

import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor
public class DefaultObjectMapper implements ObjectMapper{
    String dateFormat = "yyyy-MM-dd";
    DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

    String decimalFormat = "#,##0.00";
    DecimalFormat decimalFormatter = new DecimalFormat(decimalFormat);

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void setDecimalFormat(String decimalFormat) {
        this.decimalFormat = decimalFormat;
        this.decimalFormatter = new DecimalFormat(decimalFormat);
    }

    // Object ---> JSON String
    public String writeValuesAsString(Object obj) {
        StringBuilder sJsonStr = new StringBuilder("{");
        Class<?> clz = obj.getClass();

        Field[] fields = clz.getDeclaredFields();
        //对返回对象中的每一个属性进行格式转换
        for (Field field : fields) {
            String sField = "";
            Object value;
            String name = field.getName();
            String strValue = "";
            field.setAccessible(true);
            value = null;
            try {
                value = field.get(obj);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
            // 如果该属性空值，手动赋值避免空指针异常
            if (value == null){
                strValue = "null";
                continue;
            }

            //针对不同的数据类型进行格式转换
            if (value instanceof Date) {
                LocalDate localDate = ((Date)value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                strValue = localDate.format(this.datetimeFormatter);
            }
            else if (value instanceof BigDecimal || value instanceof Double || value instanceof Float){
                strValue = this.decimalFormatter.format(value);
            }
            else {
                strValue = value.toString();
            }

            //拼接Json串
            if (sJsonStr.toString().equals("{")) {
                sField = "\"" + name + "\":\"" + strValue + "\"";
            }
            else {
                sField = ",\"" + name + "\":\"" + strValue + "\"";
            }

            sJsonStr.append(sField);
        }
        sJsonStr.append("}");
        return sJsonStr.toString();
    }
}