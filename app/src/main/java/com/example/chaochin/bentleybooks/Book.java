package com.example.chaochin.bentleybooks;

/**
 * Created by chaochin on 2018/4/11.
 */

public class Book extends UserData{
    private String ISBN;
    private String condition;
    private String email = this.getEmail();


    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
