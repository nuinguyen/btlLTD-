package com.example.mohinhbtl.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mohinhbtl.User;

import java.util.List;

@Dao

public interface UserDAO {

    @Insert
    void insert (User user);

    @Query("SELECT * FROM user")
    List<User> getListUser();

    @Query("select * from user where username= :username")
    List<User> checkUser(String username);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM user")
    void deleteAllUser();

    @Query("SELECT * FROM user where cccd like '%' ||  :name || '%' or address like '%' ||  :name || '%' or status like '%' ||  :name || '%' " )
    List<User> searchUser(String name);



//    @Query("select count(id) from user where :checkdate=date")
//    String countUser(String checkdate);
    @Query("select count(id) from user where date =:checkdate ")
    String countDate(String checkdate);

    @Query("select count(id) from user where cccd = :check or address like '%' || :check || '%' or status like '%' || :check || '%'  ")
    String countUser(String check);

    @Query("select count(id) from user")
    String countUserAll();

    @Query("SELECT * FROM user where date like '%' ||  :date || '%' ")
    List<User> searchDate(String date);
}
