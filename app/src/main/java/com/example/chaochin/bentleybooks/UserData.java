package com.example.chaochin.bentleybooks;

import android.app.Application;
import java.util.ArrayList;

public class UserData extends Application{
    private String email;
    private String password;
    private String name;
    private String phone;

    public UserData(String email,String password, String name, String phone){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

