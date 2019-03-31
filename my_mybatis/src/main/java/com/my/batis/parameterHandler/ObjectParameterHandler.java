package com.my.batis.parameterHandler;

import com.my.batis.exception.MybatisException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ObjectParameterHandler implements BaseParameterHandler<Object> {

    @Override
    public void pandlerParameter(PreparedStatement pst, int i, Object parameter) throws MybatisException {
        try {
            pst.setObject(i,parameter);
        } catch (SQLException e) {
            throw new MybatisException(e.getMessage());
        }
    }
}