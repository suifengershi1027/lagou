package com.lagou.edu.utils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author 应癫
 */
public class DruidUtils {

    private DruidUtils(){
    }

    private static DruidDataSource druidDataSource = new DruidDataSource();


    static {
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://10.38.164.94:3307/test");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("miuiservermysql");

    }

    public static DruidDataSource getInstance() {
        return druidDataSource;
    }

}
