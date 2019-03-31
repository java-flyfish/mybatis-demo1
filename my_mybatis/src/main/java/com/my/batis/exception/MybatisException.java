package com.my.batis.exception;

public class MybatisException extends Exception {

    String message;

    public MybatisException(){
    }

    public MybatisException(String message){
        super(message);
    }
}
