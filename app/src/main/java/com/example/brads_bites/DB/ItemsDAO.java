package com.example.brads_bites.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.brads_bites.Item;
import com.example.brads_bites.User;

import java.util.List;

@Dao
public interface ItemsDAO {

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item items);

    @Query("SELECT * FROM " + AppDataBase.Items_Table + " ORDER BY mDate DESC")
    List<Item> getAllItems();

    @Query("SELECT * FROM " + AppDataBase.Items_Table + " WHERE mItemId = :itemId")
    List<Item> getItemsById(int itemId);

    @Query("SELECT * FROM " + AppDataBase.Items_Table + " WHERE mUserId = :userId ORDER BY mDate DESC")
    List<Item> getItemsByUserId(int userId);

    @Insert
    void insert (User... users);

    @Update
    void update (User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :username ")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId ")
    User getUserByUserId(int userId);

}
