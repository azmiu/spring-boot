package com.zhxg.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 * CopyRright (c)2014-2016: Azmiu
 * <p>
 * Project: zhxg
 * <p>
 * Module ID: <模块类编号可以引用系统设计中的类编号>
 * <p>
 * Comments: 删除数据定时任务
 * <p>
 * JDK version used: JDK1.8
 * <p>
 * NameSpace: com.zhxg.ScheduledTasks.java
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
@Component
public class DeleteDataScheduledTasks {

    private final static int TABLE_SAVE_DAY = 7;
    private final static String DB_NAME_16_195 = "192.168.16.195";
    private final static String DB_NAME_16_196 = "192.168.16.196";
    private final static String DB_NAME_16_197 = "192.168.16.197";
    private final static String DB_NAME_17_205 = "192.168.17.205";
    private final static String DB_NAME_17_206 = "192.168.17.206";
    private final static String DB_NAME_17_207 = "192.168.17.207";
    private final static String DB_NAME_30_4 = "192.168.30.4";
    private final static String DB_NAME_30_5 = "192.168.30.5";
    private Logger logger = LoggerFactory.getLogger(DeleteDataScheduledTasks.class);

    @Autowired
    @Qualifier("user195JdbcTemplate")
    JdbcTemplate user195jdbcTemplate;

    @Autowired
    @Qualifier("user196JdbcTemplate")
    JdbcTemplate user196jdbcTemplate;
    
    @Autowired
    @Qualifier("user197JdbcTemplate")
    JdbcTemplate user197jdbcTemplate;
    
    @Autowired
    @Qualifier("user205JdbcTemplate")
    JdbcTemplate user205jdbcTemplate;
    
    @Autowired
    @Qualifier("user206JdbcTemplate")
    JdbcTemplate user206jdbcTemplate;
    
    @Autowired
    @Qualifier("user207JdbcTemplate")
    JdbcTemplate user207jdbcTemplate;
    
    @Autowired
    @Qualifier("user2JdbcTemplate")
    JdbcTemplate user2jdbcTemplate;
    
    @Autowired
    @Qualifier("user3JdbcTemplate")
    JdbcTemplate user3jdbcTemplate;

    @Autowired
    @Qualifier("user4JdbcTemplate")
    JdbcTemplate user4jdbcTemplate;

    @Autowired
    @Qualifier("user5JdbcTemplate")
    JdbcTemplate user5jdbcTemplate;

    /**
     * 定时任务
     *
     * @throws Exception
     */
    // 17.52执行一次
    // @Scheduled(cron = "0 00 17 ? * *")
    // 每隔5秒钟执行一次
    // @Scheduled(fixedRate = 5000)
    // 12点到3点，每隔10分钟执行一次
    @Scheduled(cron = "0 0/20 00-6 * * ?")
    public void reportCurrentTime() {
        this.logger.info("**************************数据删除定时任务开始执行**************************");
        // 校验用户信息
        this.verificationUserInfo();
        // 校验表信息
        this.verificationTableInfo();
    }
    
    /**
     * 校验用户信息
     *
     * @return
     * @throws Exception
     */
    public void verificationUserInfo() {
        List<Map<String, Object>> userinfoList = this.getUserInfo();
        for (int i = 0; i < userinfoList.size(); i++) {
            try {
                this.checkDataSource(userinfoList.get(i));
            } catch (Exception e) {
                this.logger.error("第" + (i + 1) + "个用户信息数据异常！" + e);
                continue;
            }
        }
    }
    
    /**
     * 获取用户信息
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getUserInfo() {
        return this.user2jdbcTemplate.queryForList(
                "SELECT u.KU_ID,u.KU_LID,u.ku_name,u.KU_DBNAME,us.KU_SAVEDAYS,us.KU_ISSAVEOVERDUEDATA from yqms2.WK_T_USER u LEFT JOIN yqms2.WK_T_USERSERVICE us on u.KU_ID = us.KU_ID");
    }

    /**
     * 根据用户配置区分操作数据源
     *
     * @param ku_id
     * @param ku_name
     * @param ku_dbName
     * @param ku_saveDays
     * @throws Exception
     */
    public void checkDataSource(Map<String, Object> userInfo) throws Exception {
        ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
        if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_30_4.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user4jdbcTemplate));
            // DThread dt = new DThread(userInfo, this.user4jdbcTemplate);
            // Thread th = new Thread(dt, "定时任务线程NO.1");
            // th.start();
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_30_5.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user5jdbcTemplate));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_16_195.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user195jdbcTemplate));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_16_196.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user196jdbcTemplate));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_16_197.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user197jdbcTemplate));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_17_205.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user205jdbcTemplate));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_17_206.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user206jdbcTemplate));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_17_207.equals(userInfo.get("KU_DBNAME").toString())) {
            pool.execute(new DThread(userInfo, this.user207jdbcTemplate));
        }
    }

    /**
     * 校验匹配表
     * 按表名生成的规则删除7天前的表
     * WK_T_VALIDATION_INFO_20161105
     * WK_T_VALIDATION_INFOCNT_20161105
     *
     * @throws Exception
     */
    public void verificationTableInfo() {
        StringBuffer sbINFO = new StringBuffer();
        sbINFO.append("SHOW TABLES LIKE '");
        sbINFO.append("WK_T_VALIDATION_INFO_2%'");
        StringBuffer sbINFOCNT = new StringBuffer();
        sbINFOCNT.append("SHOW TABLES LIKE '");
        sbINFOCNT.append("WK_T_VALIDATION_INFOCNT_2%'");
        List<Map<String, Object>> infoList = this.user3jdbcTemplate.queryForList(sbINFO.toString());
        for (int i = 0; i < infoList.size(); i++) {
            String tableCreateTime = infoList.get(i).get("Tables_in_yqms2 (WK_T_VALIDATION_INFO_2%)").toString()
                    .substring(
                            21, infoList.get(i).get("Tables_in_yqms2 (WK_T_VALIDATION_INFO_2%)").toString().length());
            // 判断表生成的时间是否在7天前
            if (tableCreateTime.compareTo(DateUtil.dateToString(DateUtil.addDay(new Date(), -TABLE_SAVE_DAY))) < 0) {
                this.dropTable(infoList.get(i).get("Tables_in_yqms2 (WK_T_VALIDATION_INFO_2%)").toString());
            }
        }
        List<Map<String, Object>> infocntList = this.user3jdbcTemplate.queryForList(sbINFOCNT.toString());
        for (int i = 0; i < infocntList.size(); i++) {
            String tableCreateTime = infocntList.get(i).get("Tables_in_yqms2 (WK_T_VALIDATION_INFOCNT_2%)").toString()
                    .substring(
                            24,
                            infocntList.get(i).get("Tables_in_yqms2 (WK_T_VALIDATION_INFOCNT_2%)").toString().length());
            // 判断表生成的时间是否在7天前
            if (tableCreateTime.compareTo(DateUtil.dateToString(DateUtil.addDay(new Date(), -TABLE_SAVE_DAY))) < 0) {
                this.dropTable(infocntList.get(i).get("Tables_in_yqms2 (WK_T_VALIDATION_INFOCNT_2%)").toString());
            }
        }
    }

    /**
     * 根据表名删除相应的表
     *
     * @param tableName
     * @throws Exception
     */
    public void dropTable(String tableName) {
        if (StringUtils.isNotBlank(tableName)) {
            StringBuffer sb = new StringBuffer();
            sb.append("DROP TABLE ");
            sb.append(tableName);
            this.logger.info("要删除的表名是：" + tableName);
            this.user3jdbcTemplate.execute(sb.toString());
        }
    }
}
