package com.zhxg.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zhxg.utils.BeanTools;
import com.zhxg.utils.DateUtil;

/**
 * <p>
 * CopyRright (c)20014-2016: Azmiu
 * <p>
 * Project: zhxg
 * <p>
 * Module ID: <模块类编号可以引用系统设计中的类编号>
 * <p>
 * Comments: 测试类
 * <p>
 * JDK version used: JDK1.8
 * <p>
 * NameSpace: com.zhxg.test.DThread2.java
 * <p>
 * Author: azmiu
 * <p>
 * Create Date: 2016年11月14日
 * <p>
 * Modified By: <修改人中文名或拼音缩写>
 * <p>
 * Modified Date: <修改日期>
 * <p>
 * Why & What is modified: <修改原因描述>
 * <p>
 * Version: v1.0
 */
public class DThread2 implements Runnable {

    private final static String DB_NAME_30_4 = "192.168.30.4";
    private final static String DB_NAME_30_5 = "192.168.30.5";

    private static List<String> logsList;
    private static int allCount;
    private final static String LIMIT = "LIMIT 20000";
    private final static String WK_T_EVERYDAYDATA = "WK_T_EVERYDAYDATA";
    private final static String WK_T_VALIDATION_INFO = "WK_T_VALIDATION_INFO";

    private final static String WK_T_VALIDATION_INFOCNT = "WK_T_VALIDATION_INFOCNT";
    private final static String WK_T_VALIDATION_LOCATIONREF = "WK_T_VALIDATION_LOCATIONREF";
    private final static String WK_T_VALIDATION_REF = "WK_T_VALIDATION_REF";
    private Logger logger = LoggerFactory.getLogger(DThread2.class);


    private List<Map<String, Object>> userinfos;
    private StringRedisTemplate stringRedisTemplate;

    public DThread2(List<Map<String, Object>> userInfos, StringRedisTemplate stringRedisTemplate) {
        this.userinfos = userInfos;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void run() {
        try {

            this.verificationUserInfo(this.userinfos);

            Thread.sleep(1000);

        } catch (Exception e) {
            this.logger.error("线程:" + Thread.currentThread().getName() + "执行 异常" + e);
        }
    }

    /**
     * 校验用户信息
     *
     * @return
     * @throws Exception
     */
    public void verificationUserInfo(List<Map<String, Object>> userInfos) {
        List<Map<String, Object>> userinfoList = userInfos;
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
            this.clearData(userInfo, this.getJdbcTemplate("user4JdbcTemplate"));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_30_5.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.clearData(userInfo, this.getJdbcTemplate("user5JdbcTemplate"));
        }
        // else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
        // && DB_NAME_16_195.equals(userInfo.get("KU_DBNAME").toString())) {
        // // 删除用户数据 or 备份用户数据
        // this.clearData(userInfo, this.user195jdbcTemplate);
        // } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
        // && DB_NAME_16_196.equals(userInfo.get("KU_DBNAME").toString())) {
        // // 删除用户数据 or 备份用户数据
        // this.clearData(userInfo, this.user5jdbcTemplate);
        // } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
        // && DB_NAME_16_197.equals(userInfo.get("KU_DBNAME").toString())) {
        // // 删除用户数据 or 备份用户数据
        // this.clearData(userInfo, this.user197jdbcTemplate);
        // } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
        // && DB_NAME_17_205.equals(userInfo.get("KU_DBNAME").toString())) {
        // // 删除用户数据 or 备份用户数据
        // this.clearData(userInfo, this.user205jdbcTemplate);
        // } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
        // && DB_NAME_17_206.equals(userInfo.get("KU_DBNAME").toString())) {
        // // 删除用户数据 or 备份用户数据
        // this.clearData(userInfo, this.user206jdbcTemplate);
        // } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
        // && DB_NAME_17_207.equals(userInfo.get("KU_DBNAME").toString())) {
        // // 删除用户数据 or 备份用户数据
        // this.clearData(userInfo, this.user207jdbcTemplate);
        // }
    }

    /**
     * 清除用户数据
     *
     * @param userInfo
     * @param jdbcTemplate
     * @throws Exception
     */
    public void clearData(Map<String, Object> userInfo, JdbcTemplate jdbcTemplate) throws Exception {
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
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_REF, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除WK_T_VALIDATION_LOCATIONREF表数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_LOCATIONREF, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除YQZB_T_YJXX表数据，删除7天前数据
            // this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, YQZB_T_YJXX, ku_id, ku_name, ku_dbName, 7);

            // 删除WK_T_VALIDATION_INFO表数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_INFO, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除WK_T_VALIDATION_INFOCNT表数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_INFOCNT, ku_id, ku_name, ku_dbName,
                    ku_saveDays);

            // 删除WK_T_EVERYDAYDATA表数据
            this.deleteTableDataBy_KV_TIME(jdbcTemplate, WK_T_EVERYDAYDATA, ku_id, ku_name, ku_dbName, ku_saveDays);

            // 删除YQZB_T_ENGINE_INFO表数据
            this.delete_YQZB_T_ENGINE_INFO(jdbcTemplate, ku_id, ku_name, ku_dbName);

            logsList.add(0, "用户：" + ku_name + "一共剩余" + allCount + "条未删除数据");

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
    public void deleteTableDataBy_KV_DTCTIME(JdbcTemplate jdbcTemplate, String tableName, String ku_id, String ku_name,
            String ku_dbName, int ku_saveDays) throws Exception {
        StringBuffer deleteSQL = new StringBuffer();
        deleteSQL.append("DELETE FROM U");
        deleteSQL.append(ku_id);
        deleteSQL.append(".");
        deleteSQL.append(tableName);
        deleteSQL.append(" WHERE KV_DTCTIME <= '");
        deleteSQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -ku_saveDays)));
        deleteSQL.append("'");
        deleteSQL.append(LIMIT);
        try {

            int count = jdbcTemplate.update(deleteSQL.toString());
            this.logger.info(
                    "用户：" + ku_name + "，从" + ku_dbName + "上删除【" + tableName + "】表中" + +ku_saveDays + "天前的数据："
                            + count + "条");
            StringBuffer querySQL = new StringBuffer();
            querySQL.append("SELECT count(1) FROM U");
            querySQL.append(ku_id);
            querySQL.append(".");
            querySQL.append(tableName);
            querySQL.append(" WHERE KV_DTCTIME <= '");
            querySQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -ku_saveDays)));
            querySQL.append("'");
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
        } catch (Exception e) {
            throw new Exception("{}删除表数据异常" + e);
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
    public void deleteTableDataBy_KV_TIME(JdbcTemplate jdbcTemplate, String tableName, String ku_id, String ku_name,
            String ku_dbName, int ku_saveDays) throws Exception {
        StringBuffer deleteSQL = new StringBuffer();
        deleteSQL.append("DELETE FROM U");
        deleteSQL.append(ku_id);
        deleteSQL.append(".");
        deleteSQL.append(tableName);
        deleteSQL.append(" WHERE KV_TIME <= '");
        deleteSQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -7)));
        deleteSQL.append("'");
        deleteSQL.append(LIMIT);
        try {
            int count = jdbcTemplate.update(deleteSQL.toString());
            this.logger.info(
                    "用户：" + ku_name + "，从" + ku_dbName + "上删除【" + tableName + "】表中" + +ku_saveDays + "天前的数据："
                            + count + "条");
            StringBuffer querySQL = new StringBuffer();
            querySQL.append("SELECT count(1) FROM U");
            querySQL.append(ku_id);
            querySQL.append(".");
            querySQL.append(tableName);
            querySQL.append(" WHERE KV_TIME <= '");
            querySQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -7)));
            querySQL.append("'");
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
        } catch (Exception e) {
            throw new Exception("{}删除表数据异常" + e);
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
    public void delete_YQZB_T_ENGINE_INFO(JdbcTemplate jdbcTemplate, String ku_id, String ku_name, String ku_dbName)
            throws Exception {
        StringBuffer deleteSQL = new StringBuffer();
        // querySQL.append("SELECT count(*) FROM U");
        deleteSQL.append("DELETE FROM U");
        deleteSQL.append(ku_id);
        deleteSQL.append(".");
        deleteSQL.append("YQZB_T_ENGINE_INFO");
        deleteSQL.append(" WHERE KT_UUID NOT IN (");
        deleteSQL.append(this.getUserTopic().toString());
        deleteSQL.append(")");
        deleteSQL.append(LIMIT);
        try {
            int count = jdbcTemplate.update(deleteSQL.toString());
            this.logger.info(
                    "用户：" + ku_name + "，从" + ku_dbName + "上删除【YQZB_T_ENGINE_INFO】表中数据："
                            + count + "条");
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
        } catch (Exception e) {
            throw new Exception("{}删除表数据异常" + e);
        }
    }

    /**
     * 获取用户话题关联表
     *
     * @return
     * @throws Exception
     */
    public StringBuffer getUserTopic() {
        List<Map<String, Object>> list = this.getJdbcTemplate("user2JdbcTemplate")
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
     * 获取jdbc链接实例
     * 根据jdbc beanName
     *
     * @param beanName
     * @return
     * @throws Exception
     */
    public JdbcTemplate getJdbcTemplate(String beanName) {
        return (JdbcTemplate) BeanTools.getBean(beanName);
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
