package com.my.batis.parameterHandler;

import com.my.batis.exception.MybatisException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BaseParameterHandler<T> {
    void pandlerParameter(PreparedStatement pst,int i, T parameter) throws SQLException, MybatisException;
}
