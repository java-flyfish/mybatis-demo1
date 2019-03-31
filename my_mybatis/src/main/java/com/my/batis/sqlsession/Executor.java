package com.my.batis.sqlsession;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.my.batis.config.Configuration;
import com.my.batis.exception.MybatisException;
import com.my.batis.parameterHandler.BaseParameterHandler;
import com.my.batis.parameterHandler.IntegerParameterHandler;
import com.my.batis.parameterHandler.ObjectParameterHandler;
import com.my.batis.parameterHandler.StringParameterHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

public class Executor {
    private Configuration configuration;
    private DruidDataSource dataSource;

    Map<String,BaseParameterHandler> parameterHandlerMap = new HashMap<>();
    {
        parameterHandlerMap.put("java.lang.Integer",new IntegerParameterHandler());
        parameterHandlerMap.put("java.lang.String", new StringParameterHandler());
    }

    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    public Executor(Configuration configuration) {
        this.configuration = configuration;
        //初始化DataSource
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(configuration.getDriverClass());
        dataSource.setUsername(configuration.getUsername());
        dataSource.setPassword(configuration.getPassword());
        dataSource.setUrl(configuration.getUrl());
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
    }

    /**
     * @param sql
     * @param returnType 返回类型
     * @param <T>
     * @return
     */
    public <T> List<T> executorQuery(String sql, List<Object> valueList, String returnType) {
        //返回数据集合
        List<T> list = new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            pst = conn.prepareStatement(sql);
            //设置pst中的值
            setPsramsToPst(pst,valueList);
            /*//注册驱动
            Class.forName(configuration.getDriverClass());
            //获得链接
            conn = DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
            //获得执行者对象
            pst = conn.prepareStatement(sql);*/
            //获得结果集
            rs = pst.executeQuery();
            //获得元数据
            ResultSetMetaData metaData = rs.getMetaData();
            //字段数
            int columnCount = metaData.getColumnCount();
            //存放字段
            List<String> columnNames = new ArrayList<>();

            //遍历字段数
            for (int i = 1; i <= columnCount; i++) {
                //获得对应的字段名
                String columnName = metaData.getColumnName(i);
                //放入到list集合
                columnNames.add(columnName);
            }
            //获得字节码文件
            Class obj = Class.forName(returnType);
            //解析结果集
            while (rs.next()) {
                //创建对象 相当于 new User();
                Object o = obj.newInstance();
                //获得User实体类中的所有方法
                Method[] methods = obj.getMethods();
                for (Method method : methods) {
                    //遍历字段
                    for (String columnName : columnNames) {
                        if (method.getName().equalsIgnoreCase("set" + columnName)) {
                            try {
                                method.invoke(o,rs.getObject(columnName));
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //放到对象集合中
                list.add((T) o);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
//            release();
        }
        return list;
    }

    public Integer executorUpdate(String sql, List<Object> valueList){
        int num = 0;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement(sql);

            setPsramsToPst(pst,valueList);

            num = pst.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    public void release() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 设置pst中的值
     * @param pst
     * @param valueList
     * @throws SQLException
     * @throws MybatisException
     */
    private void setPsramsToPst(PreparedStatement pst,List<Object> valueList) throws SQLException, MybatisException {
        if (!valueList.isEmpty()){
            int i = 1;
            for (Object value : valueList) {
                BaseParameterHandler parameterHandler = parameterHandlerMap.get(value.getClass().getCanonicalName());
                if (parameterHandler == null){
                    parameterHandler = new ObjectParameterHandler();
                }
                parameterHandler.pandlerParameter(pst,i,value);
                i ++;
            }
        }
    }
}
