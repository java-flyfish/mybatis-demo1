package com.mybatis;

import com.mybatis.bean.User;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class MainTest {

    public static LinkedHashMap<String,Object> objectToMap(Object requestParameters) throws IllegalAccessException {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if (requestParameters == null){
            return map;
        }

        Class clzz = requestParameters.getClass();
        if (clzz.equals(java.lang.Integer.class) ||
                clzz.equals(java.lang.Byte.class) ||
                clzz.equals(java.lang.Long.class) ||
                clzz.equals(java.lang.Double.class) ||
                clzz.equals(java.lang.Float.class) ||
                clzz.equals(java.lang.Character.class) ||
                clzz.equals(java.lang.Short.class) ||
                clzz.equals(java.lang.Boolean.class) ||
                clzz.equals(java.lang.String.class)) {
            //只是一个基本数据类型
            map.put(clzz.getCanonicalName(),requestParameters);
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
        User user = new User();
        user.setName("掌声");
        user.setAge(12);
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = objectToMap("1");
        System.out.println(stringObjectLinkedHashMap);
    }
}
