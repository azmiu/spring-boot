package com.zhxg.utils;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <p>
 * CopyRright (c)2014-2016: Azmiu
 * <p>
 * Project: zhxg
 * <p>
 * Module ID: <模块类编号可以引用系统设计中的类编号>
 * <p>
 * Comments: 数据源配置
 * <p>
 * JDK version used: JDK1.8
 * <p>
 * NameSpace: com.zhxg.DataSourceConfig.java
 * <p>
 * Author: azmiu
 * <p>
 * Create Date: 2016年11月8日
 * <p>
 * Modified By: <修改人中文名或拼音缩写>
 * <p>
 * Modified Date: <修改日期>
 * <p>
 * Why & What is modified: <修改原因描述>
 * <p>
 * Version: v1.0
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "user305JdbcTemplate")
    public JdbcTemplate five(@Qualifier("user305DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "user305DataSource")
    @Qualifier("user305DataSource")
    @ConfigurationProperties(prefix = "userinfo.five")
    public DataSource fiveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user304JdbcTemplate")
    public JdbcTemplate four(@Qualifier("user304DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "user304DataSource")
    @Qualifier("user304DataSource")
    @ConfigurationProperties(prefix = "userinfo.four")
    public DataSource fourDataSource() {
        return DataSourceBuilder.create().build();
    }

    

    @Bean(name = "user302DataSource")
    @Qualifier("user302DataSource")
    @ConfigurationProperties(prefix = "userinfo.two")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user302JdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("user302DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "303DataSource")
    @Qualifier("303DataSource")
    @Primary
    @ConfigurationProperties(prefix = "userinfo.three")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user303JdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("303DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
