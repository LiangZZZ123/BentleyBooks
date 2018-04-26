package com.example.chaochin.bentleybooks;

import android.app.Application;
import java.util.ArrayList;

public class UserData extends Application{
    private String password;
    private String email;
    private String phone;
    private ArrayList<Book> Books;

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
}

