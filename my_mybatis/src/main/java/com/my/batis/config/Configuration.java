package com.my.batis.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Configuration {
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private Map<String, Mapper> mappers = new HashMap<>();
}
