package com.example.youmate.adapter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface videoDao
{
  @Insert
  void insert(List<videomodel> obj);
  @Insert
  void insertone(videomodel obj);

  @Delete
  void delete(videomodel obj);

  @Query("SELECT * FROM filetable")
  List<videomodel> getall();

  @Query("DELETE  FROM filetable WHERE id = :id")
  void delete(int id);

}
