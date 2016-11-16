package com.zhxg.utils;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * CopyRright (c)20014-2016: Azmiu
 * <p>
 * Project: zhxg
 * <p>
 * Module ID: <模块类编号可以引用系统设计中的类编号>
 * <p>
 * Comments: 线程处理类
 * <p>
 * JDK version used: JDK1.8
 * <p>
 * NameSpace: com.zhxg.test.ExecutorProcessPool.java
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
public class ExecutorProcessPool {

    private Logger logger = LoggerFactory.getLogger(ExecutorProcessPool.class);
    private ExecutorService executor;
    private static ExecutorProcessPool pool = new ExecutorProcessPool();
    private final int threadMax = 30;

    private ExecutorProcessPool() {
        this.logger.info("初始化线程池成功{}" + this.threadMax);
        this.executor = ExecutorServiceFactory.getInstance().createFixedThreadPool(this.threadMax);
        // this.executor = ExecutorServiceFactory.getInstance().createCachedThreadPool();
    }

    public static ExecutorProcessPool getInstance() {
        return pool;
    }

    /**
     * 关闭线程池，这里要说明的是：调用关闭线程池方法后，线程池会执行完队列中的所有任务才退出
     */
    public void shutdown() {
        this.executor.shutdown();
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     * 
     * @param task
     * @return
     */
    public Future<?> submit(Runnable task) {
        return this.executor.submit(task);
    }

    /**
     * 提交任务到线程池，可以接收线程返回值
     * 
     * @param task
     * @return
     */
    public Future<Integer> submit(Callable<Integer> task) {
        return this.executor.submit(task);
    }

    /**
     * 直接提交任务到线程池，无返回值
     * 
     * @param task
     */
    public void execute(Runnable task) {
        this.executor.execute(task);
    }

    /**
     * 获取线程池运行结果
     *
     * @return
     * @throws Exception
     */
    public boolean getResult() {
        return this.executor.isTerminated();
    }
}