package com.example.akanksha.todolist;

public class Item {

    private long id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String category;
    private int mark;
    private int check;

    public Item(String title,String  description,String date,String time,String category,int mark ,int check) {

        this.title = title;
        this.description= description;
        this.date=date;
        this.time=time;
        this.category=category;
        this.mark=mark;
        this.check=check;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
