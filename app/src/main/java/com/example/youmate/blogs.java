package com.example.youmate;

public class blogs {
    String title;
    String description;
    String link;
    String imgurl;
    String entry_num;

    public blogs() {
        this.title = "";
        this.description = "";
        this.link = "";
        this.imgurl = "";
        this.entry_num = "";
    }

    public blogs(String title, String description, String link, String imgurl, String entry_num) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.imgurl = imgurl;
        this.entry_num = entry_num;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getEntry_num() {
        return entry_num;
    }

    public void setEntry_num(String entry_num) {
        this.entry_num = entry_num;
    }
}
