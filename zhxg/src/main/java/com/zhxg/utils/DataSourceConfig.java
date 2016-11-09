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
    
    /**
     * 获取
     * 192.168.30.2
     * 数据源
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "user2DataSource")
    @Qualifier("user2DataSource")
    @ConfigurationProperties(prefix = "userinfo.2")
    public DataSource getDataSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user2JdbcTemplate")
    public JdbcTemplate getTemplate2(@Qualifier("user2DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    /**
     * 获取
     * 192.168.30.3
     * 数据源
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "user3DataSource")
    @Qualifier("user3DataSource")
    @Primary
    @ConfigurationProperties(prefix = "userinfo.3")
    public DataSource getDataSource3() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user3JdbcTemplate")
    public JdbcTemplate getJdbcTemplate3(@Qualifier("user3DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.30.4
     * 数据源
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "user4DataSource")
    @Qualifier("user4DataSource")
    @ConfigurationProperties(prefix = "userinfo.4")
    public DataSource getDataSource4() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "user4JdbcTemplate")
    public JdbcTemplate getTemplate4(@Qualifier("user4DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.30.5
     * 数据源
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "user5DataSource")
    @Qualifier("user5DataSource")
    @ConfigurationProperties(prefix = "userinfo.5")
    public DataSource getDataSource5() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user5JdbcTemplate")
    public JdbcTemplate getTemplate5(@Qualifier("user5DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.16.195
     * 数据源
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "user195DataSource")
    @Qualifier("user195DataSource")
    @ConfigurationProperties(prefix = "userinfo.195")
    public DataSource getDataSource195() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user195JdbcTemplate")
    public JdbcTemplate getTemplate195(@Qualifier("user195DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.16.196
     * 数据源
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "user196DataSource")
    @Qualifier("user196DataSource")
    @ConfigurationProperties(prefix = "userinfo.196")
    public DataSource getDataSource196() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user196JdbcTemplate")
    public JdbcTemplate getTemplate196(@Qualifier("user196DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.16.197
     * 数据源
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "user197DataSource")
    @Qualifier("user197DataSource")
    @ConfigurationProperties(prefix = "userinfo.197")
    public DataSource getDataSource197() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user197JdbcTemplate")
    public JdbcTemplate getTemplate197(@Qualifier("user197DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.17.205
     * 数据源
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "user205DataSource")
    @Qualifier("user205DataSource")
    @ConfigurationProperties(prefix = "userinfo.205")
    public DataSource getDataSource205() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user205JdbcTemplate")
    public JdbcTemplate getTemplate205(@Qualifier("user205DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.17.206
     * 数据源
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "user206DataSource")
    @Qualifier("user206DataSource")
    @ConfigurationProperties(prefix = "userinfo.206")
    public DataSource getDataSource206() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user206JdbcTemplate")
    public JdbcTemplate getTemplate206(@Qualifier("user206DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 获取
     * 192.168.17.207
     * 数据源
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "user207DataSource")
    @Qualifier("user207DataSource")
    @ConfigurationProperties(prefix = "userinfo.207")
    public DataSource getDataSource207() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "user207JdbcTemplate")
    public JdbcTemplate getTemplate207(@Qualifier("user207DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
