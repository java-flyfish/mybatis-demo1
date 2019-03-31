package com.my.batis.parameterHandler;

import com.my.batis.exception.MybatisException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntegerParameterHandler implements BaseParameterHandler<Integer> {

    @Override
    public void pandlerParameter(PreparedStatement pst, int i, Integer parameter) throws MybatisException {
        try {
            pst.setInt(i,parameter);
        } catch (SQLException e) {
            throw new MybatisException(e.getMessage());
        }
    }
}
