package com.azmiu.init;

import org.springframework.stereotype.Component;

import com.azmiu.bean.AjedisPool;
import com.azmiu.bean.AmysqlDatasource;

@Component
public interface Init {

    public void initMysqlDataSource(AmysqlDatasource aMysqlDatasource);

    public void initRedisPool(AjedisPool aJedisPool);

    public void reload();

}
