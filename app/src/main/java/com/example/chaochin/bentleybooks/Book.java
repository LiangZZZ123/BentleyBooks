package com.example.chaochin.bentleybooks;

/**
 * Created by chaochin on 2018/4/11.
 */

public class Book extends UserData{
    private String bookid;
    private String ISBN;
    private String condition;
    private String email = this.getEmail();
    private String price;

    public Book (String bookid, String ISBN, String condition, String price){
        this.bookid = bookid;
        this.ISBN = ISBN;
        this.condition = condition;
        this.price = price;

    }
    public String toString (){
        return "ISBN: " + this.ISBN + ",  " + this.condition + ",   $" + this.price;
    }

    public String getBookid() {return bookid; };

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

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
