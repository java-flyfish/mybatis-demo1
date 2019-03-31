package com.my.batis;

import com.alibaba.druid.util.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class MainTest {

    public static LinkedHashMap<String,Object> objectToMap(Object requestParameters) throws IllegalAccessException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if (requestParameters == null){
            return map;
        }
        //获取f对象对应类中的所有属性域
        Field[] fields = requestParameters.getClass().getDeclaredFields();
        /*if (fields.length <1){
            map.put("oneArg",requestParameters +"");
            return map;
        }*/
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            // 获取原来的访问控制权限
            boolean accessFlag = fields[i].isAccessible();
            // 修改访问控制权限
            fields[i].setAccessible(true);
            // 获取在对象f中属性fields[i]对应的对象中的变量
            Object o = fields[i].get(requestParameters);
            if (o != null && !StringUtils.isEmpty(o.toString().trim())) {
                map.put(varName, o.toString().trim());
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            }
        }
        return map;
    }

    public static void main(String[] args) throws IllegalAccessException {
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = objectToMap(1);
        System.out.println(stringObjectLinkedHashMap);
    }
}
