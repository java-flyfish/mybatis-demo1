package com.my.batis.parameterHandler;

import com.my.batis.exception.MybatisException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringParameterHandler  implements BaseParameterHandler<String> {

    @Override
    public void pandlerParameter(PreparedStatement pst, int i, String parameter) throws MybatisException {
        try {
            pst.setString(i,parameter);
        } catch (SQLException e) {
            throw new MybatisException(e.getMessage());
        }
    }
}
