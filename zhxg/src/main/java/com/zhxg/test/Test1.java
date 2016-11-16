package com.zhxg.test;

public class Test1 {

    // public static void main(String args[]) throws InterruptedException {
    // ExecutorService exe = Executors.newFixedThreadPool(50);
    // for (int i = 1; i <= 5; i++) {
    // exe.execute(new SubThread(i));
    // }
    // exe.shutdown();
    // while (true) {
    // if (exe.isTerminated()) {
    // System.out.println("结束了！");
    // break;
    // }
    // Thread.sleep(200);
    // }
    // }

    // Calendar ncalendar = Calendar.getInstance();
    // //小时
    // System.out.println(ncalendar.get(Calendar.HOUR_OF_DAY));
    // //分
    // System.out.println(ncalendar.get(Calendar.MINUTE));
    // // hour minute
    // SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Date date1 = sim1.parse(sim1.format(new Date()));
    // if((date1.before(sim1.parse("2016-11-08 17:00:00")) && date1.after(sim1.parse("2016-11-08 08:00:00")))){ //不是白班
    // System.out.println("白天");
    // }else {
    // System.out.println("夜晚");
    // }
}
