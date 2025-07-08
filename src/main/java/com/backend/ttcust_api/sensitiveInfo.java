package com.backend.ttcust_api;

public class sensitiveInfo {
    private String jdbcURL = "jdbc:datadirect:openedge://192.168.12.35:15020;databaseName=tmm10;";
    private String username = "sysprogress";
    private String password = "sysprogress";

    public sensitiveInfo(){

    }

    public String getUrl(){
        return jdbcURL;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}