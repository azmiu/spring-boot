package com.azmiu.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MysqlDatasourceConfig {

    /**
     * 基本配置数据源
     * 获取properties中basicConfig
     * 
     * @return
     */
    @Bean(name = "basicConfigDataSource")
    @Qualifier("basicConfigDataSource")
    @ConfigurationProperties(prefix = "basicConfig")
    public DataSource getBasicConfigDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "basicConfigJdbcTemplate")
    public JdbcTemplate getBasicConfigJdbcTemplate(@Qualifier("basicConfigDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
