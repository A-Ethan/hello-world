package com.example.demo;



public class Helloworld implements  HelloworldMBean {
    private String appname="gary_hello_world" ;
    private int app_counter =0;
    private int request_counter =0;
    private int success_counter = 0;

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getAppCounter() {
        return app_counter;
    }

    public void setAppCounter(int app_counter) {
        this.app_counter = app_counter;
    }

    public int getRequestCounter() {
        return request_counter;
    }

    public void setRequestCounter(int request_counter) {
        this.request_counter = request_counter;
    }

    public int getSuccessCounter() {
        return success_counter;
    }

    public void setSuccessCounter(int success_counter) {
        this.success_counter = success_counter;
    }


}
