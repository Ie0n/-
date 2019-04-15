package com.example.feng.version1.bean;

public class User {
    private String userName;
    private int userNo;
    private boolean isManager;
    private int admin;
    private static volatile User instance=null;
    private String password;
    private User(){

    }
    public User (String userName,int userNo,String password){
        this.userNo = userNo;
        this.userName = userName;
        this.password = password;
    }
    public static User getInstance(){
        if(instance==null){
            synchronized(User .class){
                if(instance==null){
                    instance=new User ();
                }
            }
        }
        return instance;
    }

    public boolean isManager() {
        return isManager;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getuserNo() {
        return userNo;
    }

    public void setuserNo(int userNo) {
        this.userNo = userNo;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
