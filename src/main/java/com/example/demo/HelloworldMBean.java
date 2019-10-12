package com.example.demo;

import javax.management.*;

public interface HelloworldMBean {
    public String getAppname() ;

    public void setAppname(String appname);

    public int getAppCounter() ;

    public void setAppCounter(int app_counter) ;

    public int getRequestCounter() ;

    public void setRequestCounter(int request_counter);

    public int getSuccessCounter() ;

    public void setSuccessCounter(int success_counter) ;
}