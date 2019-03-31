package com.my.batis.config;

import lombok.Data;

@Data
public class Mapper {
    private String sql;//sql语句
    private String resultType;//返回值类型
}
