package com.example.youmate.adapter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.text.DateFormat;
import java.util.Date;

@Entity(tableName = "filetable")
public class videomodel {

  @ColumnInfo(name = "id")
  @PrimaryKey
  @NonNull
  int id;
  @ColumnInfo
  String name;
  @ColumnInfo
  String path;
  @ColumnInfo
  String time;

  int progress;
  String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public videomodel() {
  }

  public videomodel(int id, String name, String path)
  {
    this.id = id;
    this.name = name;
    this.path = path;
    time = DateFormat.getDateTimeInstance().format(new Date());
    progress =0;
  }

  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
