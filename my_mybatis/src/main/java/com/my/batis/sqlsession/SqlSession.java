package com.my.batis.sqlsession;

public interface SqlSession {
    //执行
    <T> Object executor(String mapperId, Object request) throws Exception;
    //关闭资源
    void close();
}
