package com.example.chaochin.bentleybooks;

/**
 * Created by chaochin on 2018/4/11.
 */

/*set the attributes for a book object*/
public class Book {
    private String bookid;
    private String ISBN;
    private String condition;
    private String price;
    private String email;
    private String seller;

    public Book (String bookid, String ISBN, String condition, String price, String seller, String email ){
        this.bookid = bookid;
        this.ISBN = ISBN;
        this.condition = condition;
        this.price = price;
        this.seller = seller;
        this.email = email;

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

    public String getEmail() {
        return email;
    }

    public String getseller() {
        return seller;
    }
}
