package com.my.batis.sqlsession;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory builder(InputStream inputStream){
        return  new SqlSessionFactory(inputStream);
    }

    public SqlSessionFactory builder(String path){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        return  new SqlSessionFactory(inputStream);
    }

    public SqlSessionFactory builder(){
        String path = "Configuration.xml";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        return  new SqlSessionFactory(inputStream);
    }
}
