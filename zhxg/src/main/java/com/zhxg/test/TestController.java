package com.zhxg.test;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * CopyRright (c)2014-2016: Azmiu
 * <p>
 * Project: zhxg
 * <p>
 * Module ID: <模块类编号可以引用系统设计中的类编号>
 * <p>
 * Comments: web测试demo
 * <p>
 * JDK version used: JDK1.8
 * <p>
 * NameSpace: com.zhxg.TestController.java
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
@RestController
@RequestMapping("/home")
public class TestController {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    JdbcTemplate jdbcTemplate1;
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate2;

    @Autowired
@Qualifier("thirdJdbcTemplate")
JdbcTemplate jdbcTemplate3;

    @RequestMapping("/test1")
    public String test1() {
        List<Map<String, Object>> list = this.jdbcTemplate1.queryForList(
                "SELECT u.KU_ID,u.KU_LID,u.ku_name,u.KU_DBNAME,us.KU_SAVEDAYS from yqms2.WK_T_USER u LEFT JOIN yqms2.WK_T_USERSERVICE us on u.KU_ID = us.KU_ID;");
        return list.toString();
    }

    @RequestMapping("/test2")
    public String test2() {
        List<Map<String, Object>> list = this.jdbcTemplate2.queryForList("SHOW DATABASES");
        return list.toString();
    }

    @RequestMapping("/test3")
    public String test3() {
        List<Map<String, Object>> list = this.jdbcTemplate3.queryForList("SHOW DATABASES");
        return list.toString();
    }

    @RequestMapping("/test4")
    public String test4() {
        List<Map<String, Object>> list = this.jdbcTemplate1.queryForList("Select * from user;");
        this.jdbcTemplate1.execute("INSERT INTO `azmiu`.`user` (`id`, `name`, `password`) VALUES ('1', '张小凡', '1');");
        return list.toString();
    }
}
