package com.my.batis.sqlsession;

import com.alibaba.druid.util.StringUtils;
import com.my.batis.config.Configuration;
import com.my.batis.config.Mapper;

import java.lang.reflect.Field;
import java.util.*;

public class SqlSessionImpl implements SqlSession {
    private Configuration cfg;
    private Executor executor;

    public SqlSessionImpl(Configuration configuration) {
        this.cfg = configuration;
        this.executor = new Executor(cfg);
    }

    public Object executor(String mapperId, Object request) throws Exception {
        Mapper mapper = cfg.getMappers().get(mapperId);
        String sql = mapper.getSql();
        if (sql.toLowerCase().contains("select")){
            return executorQuery(mapperId,request);
        }
        return executorUpdate(mapperId,request);
    }

    public <T> List<T> executorQuery(String mapperId,Object request) throws Exception {
        Mapper mapper = cfg.getMappers().get(mapperId);
        String sql = mapper.getSql();
        String returnType = mapper.getResultType();

        LinkedHashMap<String,String> linkFieldMap = new LinkedHashMap<>();
        LinkedHashMap<String,Object> linkValueMap = new LinkedHashMap<>();
        List<Object> valueList = new ArrayList<>();
        sql = objectToMap(sql,request,valueList);

        return executor.executorQuery(sql,valueList,returnType);
    }

    public Integer executorUpdate(String mapperId,Object request) throws Exception {
        Mapper mapper = cfg.getMappers().get(mapperId);
        String sql = mapper.getSql();
        String returnType = mapper.getResultType();
        List<Map<String,Object>> valueMapList = new ArrayList<>();
        List<Object> valueList = new ArrayList<>();
        sql = objectToMap(sql,request,valueList);

        return executor.executorUpdate(sql,valueList);
    }

    public void close() {

        executor.release();
    }

    public  String objectToMap(String sql,Object requestParameters, List<Object> valueList) throws IllegalAccessException {
        //两个map都是以属性名作为key
        if (requestParameters == null){
            return sql;
        }

        //select * from user where id = #{id} orger by desc;
        StringBuilder sqlBuilder = new StringBuilder();
        String[] split = sql.split("#\\{");

        //如果是一个基本数据类型，则直接返回
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
            sqlBuilder.append(split[0]).append("?");
            String[] split1 = split[1].split("}");
            valueList.add(requestParameters);
            if (split1.length>1){
                sqlBuilder.append(split1[1]);
            }
            sql = sqlBuilder.toString();
            return sql;
        }

        Map<String,Object> valueMap = new HashMap<>();
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
                valueMap.put(varName,o);
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            }
        }

        for (int i=0; i<split.length; i++){
            if (i == 0){
                sqlBuilder.append(split[i]);
            }else{
                String[] names = split[i].split("}");
                sqlBuilder.append("?");
                //select * from user where id = #{id} and mane = #{name} orger by desc;
                //select * from user where id =
                //id} and mane =
                //name} orger by desc;
                valueList.add(valueMap.get(names[0]));
                if (names.length>1){
                    sqlBuilder.append(names[1]);
                }
            }
        }
        sql = sqlBuilder.toString();
        return sql;
    }

   /* public static void main(String[] args) throws IllegalAccessException {
        LinkedHashMap<String, Object> stringObjectLinkedHashMap = objectToMap(1);
        System.out.println(stringObjectLinkedHashMap);
    }
*/

    /*public void formatSqlAndBean(String sql, LinkedHashMap<String,Object> argsMap)throws Exception {
        if (argsMap.isEmpty()){
            return;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        String[] split = sql.split("#\\{");

        if (!StringUtils.isEmpty(argsMap.get("oneArg"))){
            IntegerParameterHandler parameterHandler = new IntegerParameterHandler();
            Object oneArg = parameterHandler.pandlerParameter(argsMap.get("oneArg"));
            sqlBuilder.append(split[0]).append(argsMap.get("oneArg"));
            String[] split1 = split[1].split("}");
            if (split1.length>1){
                sqlBuilder.append(split1[1]);
            }
        }

        for (int i=0; i<split.length; i++){
            if (i == 0){
                sqlBuilder.append(split[i]);
            }else{
                String[] names = split[i].split("}");
                sqlBuilder.append(argsMap.get(names[0]));
                if (names.length>1){
                    if (StringUtils.isEmpty(names[1])){
                        throw new Exception("sql格式化参数不全");
                    }
                    sqlBuilder.append(names[1]);
                }

            }
        }
        return sqlBuilder.toString();
    }*/
}
