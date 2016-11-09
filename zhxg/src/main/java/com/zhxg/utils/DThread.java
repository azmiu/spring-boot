package com.zhxg.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public class DThread implements Runnable {

    private final static String DB_NAME_30_4 = "192.168.30.4";
    private final static String DB_NAME_30_5 = "192.168.30.5";
    private final static String LIMIT = "10000";
    private final static String WK_T_EVERYDAYDATA = "WK_T_EVERYDAYDATA";
    private final static String WK_T_VALIDATION_INFO = "WK_T_VALIDATION_INFO";

    private final static String WK_T_VALIDATION_INFOCNT = "WK_T_VALIDATION_INFOCNT";
    private final static String WK_T_VALIDATION_LOCATIONREF = "WK_T_VALIDATION_LOCATIONREF";
    private final static String WK_T_VALIDATION_REF = "WK_T_VALIDATION_REF";
    private final static String YQZB_T_YJXX = "YQZB_T_YJXX";
    private Logger logger = LoggerFactory.getLogger(DThread.class);
    @Autowired
    @Qualifier("user302JdbcTemplate")
    JdbcTemplate user302jdbcTemplate;
    @Autowired
    @Qualifier("user303JdbcTemplate")
    JdbcTemplate user303jdbcTemplate;
    @Autowired
    @Qualifier("user304JdbcTemplate")
    JdbcTemplate user304jdbcTemplate;

    @Autowired
    @Qualifier("user305JdbcTemplate")
    JdbcTemplate user305jdbcTemplate;

    private Map<String, Object> userinfo;


    public DThread(Map<String, Object> userinfo) {
        this.userinfo = userinfo;
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
            this.clearData(userInfo, new JdbcTemplate((DataSource) BeanTools.getBean("user304DataSource")));
        } else if (StringUtils.isNotBlank(userInfo.get("KU_DBNAME").toString())
                && DB_NAME_30_5.equals(userInfo.get("KU_DBNAME").toString())) {
            // 删除用户数据 or 备份用户数据
            this.clearData(userInfo, new JdbcTemplate((DataSource) BeanTools.getBean("user304DataSource")));
        }
        // ....
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
            String ku_lid = userInfo.get("KU_LID").toString();
            String ku_dbName = userInfo.get("KU_DBNAME").toString();
            // 用户未如设置保存天数，则默认为7天
            int ku_saveDays = StringUtils.isNotBlank(userInfo.get("KU_SAVEDAYS").toString())
                    ? Integer.valueOf(userInfo.get("KU_SAVEDAYS").toString()) : 7;

            // 删除WK_T_VALIDATION_REF表数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_REF, ku_id, ku_lid, ku_dbName, ku_saveDays);
            // 删除WK_T_VALIDATION_LOCATIONREF表数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_LOCATIONREF, ku_id, ku_lid, ku_dbName,
                    ku_saveDays);
            // 删除YQZB_T_YJXX表数据，删除7天前数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, YQZB_T_YJXX, ku_id, ku_lid, ku_dbName, 7);
            // 删除YQZB_T_ENGINE_INFO表数据

            // 删除WK_T_VALIDATION_INFO表数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_INFO, ku_id, ku_lid, ku_dbName,
                    ku_saveDays);
            // 删除WK_T_VALIDATION_INFOCNT表数据
            this.deleteTableDataBy_KV_DTCTIME(jdbcTemplate, WK_T_VALIDATION_INFOCNT, ku_id, ku_lid, ku_dbName,
                    ku_saveDays);
            // 删除WK_T_EVERYDAYDATA表数据
            this.deleteTableDataBy_KV_TIME(jdbcTemplate, WK_T_EVERYDAYDATA, ku_id, ku_lid, ku_dbName, ku_saveDays);
            // 删除YQZB_T_ENGINE_INFO表数据
            this.delete_YQZB_T_ENGINE_INFO(jdbcTemplate, ku_id, ku_lid, ku_dbName);

        } else {
            this.logger.error("{}数据库链接丢失或用户信息丢失");
        }
    }

    /**
     * 删除
     * YQZB_T_ENGINE_INFO表数据
     * 删除not in YQZB_T_TOPIC
     *
     * @throws Exception
     */
    public void delete_YQZB_T_ENGINE_INFO(JdbcTemplate jdbcTemplate, String ku_id, String ku_name, String ku_dbName)
            throws Exception {
        StringBuffer querySQL = new StringBuffer();
        // querySQL.append("SELECT count(*) FROM U");
        querySQL.append("DELETE FROM U");
        querySQL.append(ku_id);
        querySQL.append(".");
        querySQL.append("YQZB_T_ENGINE_INFO");
        querySQL.append(" WHERE KT_UUID NOT IN (");
        querySQL.append(this.getUserTopic().toString());
        querySQL.append(")");
        querySQL.append("limit ");
        querySQL.append(LIMIT);
        try {
            // List<?> list = jdbcTemplate.queryForList(querySQL.toString());
            int count = jdbcTemplate.update(querySQL.toString());

            // new Thread(new ScheduledTasks(jdbcTemplate, querySQL.toString()));
            this.logger.info(
                    "用户：" + ku_name + "，从" + ku_dbName + "上删除【YQZB_T_ENGINE_INFO】表中数据："
                            + count + "条");
        } catch (Exception e) {
            throw new Exception("{}删除表数据异常" + e);
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
        StringBuffer querySQL = new StringBuffer();
        // querySQL.append("SELECT count(*) FROM U");
        querySQL.append("DELETE FROM U");
        querySQL.append(ku_id);
        querySQL.append(".");
        querySQL.append(tableName);
        querySQL.append(" WHERE KV_DTCTIME <= '");
        querySQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -ku_saveDays)));
        querySQL.append("'");
        querySQL.append("limit ");
        querySQL.append(LIMIT);
        try {
            // List<?> list = jdbcTemplate.queryForList(querySQL.toString());
            int count = jdbcTemplate.update(querySQL.toString());

            // new Thread(new ScheduledTasks(jdbcTemplate, querySQL.toString()));
            this.logger.info(
                    "用户：" + ku_name + "，从" + ku_dbName + "上删除【" + tableName + "】表中" + +ku_saveDays + "天前的数据："
                            + count + "条");
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
        StringBuffer querySQL = new StringBuffer();
        // querySQL.append("SELECT count(*) FROM U");
        querySQL.append("DELETE FROM U");
        querySQL.append(ku_id);
        querySQL.append(".");
        querySQL.append(tableName);
        querySQL.append(" WHERE KV_TIME <= '");
        querySQL.append(DateUtil.dateToyyyyMMdd(DateUtil.addDay(new Date(), -7)));
        querySQL.append("'");
        querySQL.append("limit ");
        querySQL.append(LIMIT);
        try {
            // List<?> list = jdbcTemplate.queryForList(querySQL.toString());
            int count = jdbcTemplate.update(querySQL.toString());

            // new Thread(new ScheduledTasks(jdbcTemplate, querySQL.toString()));
            this.logger.info(
                    "用户：" + ku_name + "，从" + ku_dbName + "上删除【" + tableName + "】表中" + +ku_saveDays + "天前的数据："
                            + count + "条");
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
        List<Map<String, Object>> list = this.user302jdbcTemplate
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

    @Override
    public void run() {
        try {
            this.checkDataSource(this.userinfo);
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
    public void verificationUserInfo(List<Map<String, Object>> userinfoList) {
        // List<Map<String, Object>> userinfoList = this.getUserInfo();
        for (int i = 0; i < userinfoList.size(); i++) {
            try {
                this.checkDataSource(userinfoList.get(i));
            } catch (Exception e) {
                this.logger.error("第" + (i + 1) + "个用户信息数据异常！" + e);
                continue;
            }
        }
    }

}
