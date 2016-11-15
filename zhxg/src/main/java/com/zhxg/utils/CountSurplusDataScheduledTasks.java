package com.zhxg.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
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
 * Comments: 统计剩余数据量定时任务
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
public class CountSurplusDataScheduledTasks {

    private static int allCount;
    private final static String DB_NAME_16_195 = "192.168.16.195";
    private final static String DB_NAME_16_196 = "192.168.16.196";
    private final static String DB_NAME_16_197 = "192.168.16.197";
    private final static String DB_NAME_17_205 = "192.168.17.205";
    private final static String DB_NAME_17_206 = "192.168.17.206";
    private final static String DB_NAME_17_207 = "192.168.17.207";
    private final static String DB_NAME_30_4 = "192.168.30.4";
    private final static String DB_NAME_30_5 = "192.168.30.5";
    private static List<String> logsList;
    private final static String WK_T_EVERYDAYDATA = "WK_T_EVERYDAYDATA";
    private final static String WK_T_VALIDATION_INFO = "WK_T_VALIDATION_INFO";
    private final static String WK_T_VALIDATION_INFOCNT = "WK_T_VALIDATION_INFOCNT";
    private final static String WK_T_VALIDATION_LOCATIONREF = "WK_T_VALIDATION_LOCATIONREF";
    private final static String WK_T_VALIDATION_REF = "WK_T_VALIDATION_REF";
    private Logger logger = LoggerFactory.getLogger(CountSurplusDataScheduledTasks.class);


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
    @Scheduled(cron = "0 11 15 ? * *")
    // 每隔5秒钟执行一次
    // @Scheduled(fixedRate = 5000)
    // 12点到6点，每隔1小时执行一次
    // @Scheduled(cron = "0 0/60 00-3 * * ?")
    public void reportCurrentTime() {
        this.logger.info("***************************数据统计定时任务开始执行**************************");
        // 校验用户信息
        this.verificationUserInfo();
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
                "SELECT u.KU_ID,u.KU_LID,u.ku_name,u.KU_DBNAME,us.KU_SAVEDAYS from yqms2.WK_T_USER u LEFT JOIN yqms2.WK_T_USERSERVICE us on u.KU_ID = us.KU_ID");
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
        if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_30_4.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user4jdbcTemplate);
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_30_5.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user5jdbcTemplate);
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_16_195.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user195jdbcTemplate);
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_16_196.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user5jdbcTemplate);
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_16_197.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user197jdbcTemplate);
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_17_205.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user205jdbcTemplate);
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_17_206.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user206jdbcTemplate);
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_17_207.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.countData(userInfo, this.user207jdbcTemplate);
        }
    }

    /**
     * 清除用户数据
     *
     * @param userInfo
     * @param jdbcTemplate
     * @throws Exception
     */
    public void countData(Map<String, Object> userInfo, JdbcTemplate jdbcTemplate) throws Exception {
        if (null != jdbcTemplate && !userInfo.isEmpty()) {
            String ku_id = userInfo.get("KU_ID").toString();
            String ku_name = userInfo.get("KU_LID").toString();
            String ku_dbName = userInfo.get("KU_DBNAME").toString();
            // 用户未如设置保存天数，则默认为7天
            int ku_saveDays = StringUtils.isNotBlank(userInfo.get("KU_SAVEDAYS").toString())
                    ? Integer.valueOf(userInfo.get("KU_SAVEDAYS").toString()) : 7;

            logsList = new ArrayList<String>();
            allCount = 0;

            // 删除WK_T_VALIDATION_REF表数据
            this.countTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_REF, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除WK_T_VALIDATION_LOCATIONREF表数据
            this.countTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_LOCATIONREF, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除WK_T_VALIDATION_INFO表数据
            this.countTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_INFO, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除WK_T_VALIDATION_INFOCNT表数据
            this.countTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_INFOCNT, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除WK_T_EVERYDAYDATA表数据
            this.countTableDataBy_KV_TIME(jdbcTemplate, WK_T_EVERYDAYDATA, ku_id, ku_name, ku_dbName, ku_saveDays);

            // 删除YQZB_T_ENGINE_INFO表数据
            this.count_YQZB_T_ENGINE_INFO(jdbcTemplate, ku_id, ku_name, ku_dbName);

            logsList.add(0, allCount + "");

            // 将用户剩余数据信息存入redis
            this.setList(ku_id, logsList);
        } else {
            this.logger.error("{}数据库链接丢失或用户信息丢失");
        }
    }

    /**
     * 删除指定表内指定天数数据
     * 根据KV_DTCTIME
     * 
     * @param jdbcTemplate
     * @param tableName
     * @param ku_id
     * @param ku_name
     * @param ku_dbName
     * @param ku_saveDays
     * @throws Exception
     */
    public void countTableDataBy_KV_DTCTIME(JdbcTemplate jdbcTemplate, String tableName, String ku_id, String ku_name,
            String ku_dbName, int ku_saveDays) throws Exception {

        StringBuffer querySQL = new StringBuffer();
        querySQL.append("SELECT count(1) FROM U");
        querySQL.append(ku_id);
        querySQL.append(".");
        querySQL.append(tableName);
        querySQL.append(" WHERE KV_DTCTIME <= '");
        querySQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -ku_saveDays)));
        querySQL.append("'");
        try {

            List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString());
            StringBuffer sb = new StringBuffer();
            sb.append("用户：");
            sb.append(ku_name);
            sb.append("在");
            sb.append(ku_dbName);
            sb.append("的");
            sb.append(tableName);
            sb.append("中还存在");
            sb.append(ku_saveDays);
            sb.append("天前的数据");
            sb.append(list.get(0).get("count(1)").toString());
            sb.append("条");
            logsList.add(sb.toString());
            allCount += Integer.valueOf(list.get(0).get("count(1)").toString());
            this.logger.info(sb.toString());
        } catch (Exception e) {
            throw new Exception("数据统计异常，{}" + e);
        }
    }

    /**
     * 删除指定表内指定天数数据
     * 根据KV_TIME
     * 
     * @param jdbcTemplate
     * @param tableName
     * @param ku_id
     * @param ku_name
     * @param ku_dbName
     * @param ku_saveDays
     * @throws Exception
     */
    public void countTableDataBy_KV_TIME(JdbcTemplate jdbcTemplate, String tableName, String ku_id, String ku_name,
            String ku_dbName, int ku_saveDays) throws Exception {

        StringBuffer querySQL = new StringBuffer();
        querySQL.append("SELECT count(1) FROM U");
        querySQL.append(ku_id);
        querySQL.append(".");
        querySQL.append(tableName);
        querySQL.append(" WHERE KV_TIME <= '");
        querySQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -7)));
        querySQL.append("'");
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString());
            StringBuffer sb = new StringBuffer();
            sb.append("用户：");
            sb.append(ku_name);
            sb.append("在");
            sb.append(ku_dbName);
            sb.append("的");
            sb.append(tableName);
            sb.append("中还存在");
            sb.append(ku_saveDays);
            sb.append("天前的数据");
            sb.append(list.get(0).get("count(1)").toString());
            sb.append("条");
            logsList.add(sb.toString());
            allCount += Integer.valueOf(list.get(0).get("count(1)").toString());
            this.logger.info(sb.toString());
        } catch (Exception e) {
            throw new Exception("数据统计异常，{}" + e);
        }
    }

    /**
     * 删除
     * YQZB_T_ENGINE_INFO表数据
     * 删除not in YQZB_T_TOPIC
     *
     * @param jdbcTemplate
     * @param ku_id
     * @param ku_name
     * @param ku_dbName
     * @throws Exception
     * @throws Exception
     */
    public void count_YQZB_T_ENGINE_INFO(JdbcTemplate jdbcTemplate, String ku_id, String ku_name, String ku_dbName)
            throws Exception {
        try {
            StringBuffer querySQL = new StringBuffer();
            querySQL.append("SELECT count(1) FROM U");
            querySQL.append(ku_id);
            querySQL.append(".YQZB_T_ENGINE_INFO WHERE KT_UUID NOT IN (");
            querySQL.append(this.getUserTopic().toString());
            querySQL.append(")");
            List<Map<String, Object>> list = jdbcTemplate.queryForList(querySQL.toString());
            StringBuffer sb = new StringBuffer();
            sb.append("用户：");
            sb.append(ku_name);
            sb.append("在");
            sb.append(ku_dbName);
            sb.append("的YQZB_T_ENGINE_INFO表中还存在：");
            sb.append(list.get(0).get("count(1)").toString());
            sb.append("条数据");
            logsList.add(sb.toString());
            allCount += Integer.valueOf(list.get(0).get("count(1)").toString());
            this.logger.info(sb.toString());
        } catch (Exception e) {
            throw new Exception("数据统计异常，{}" + e);
        }
    }

    /**
     * 获取用户话题关联表
     *
     * @return
     * @throws Exception
     */
    public StringBuffer getUserTopic() {
        List<Map<String, Object>> list = this.user2jdbcTemplate
                .queryForList("SELECT KT_UUID FROM yqms2.YQZB_T_TOPIC;");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append("'");
            sb.append(list.get(i).get("KT_UUID").toString());
            if (i == (list.size() - 1)) {
                sb.append("'");
            } else {
                sb.append("',");
            }
        }
        return sb;
    }
    
    /**
     * 将指定key赋值为指定的List
     * 
     * @param key
     * @param list
     * @throws Exception
     */
    public void setList(String key, List<String> list) {
        this.delete(key);
        String[] arr = new String[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        this.stringRedisTemplate.opsForList().rightPushAll(key, arr);
    }

    /**
     * 删除指定key，在缓存中的对应值
     * 
     * @param key
     * @throws Exception
     */
    public void delete(String key) {
        this.stringRedisTemplate.delete(key);
    }
    

}
