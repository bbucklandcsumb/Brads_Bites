package com.example.brads_bites.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.brads_bites.Item;
import com.example.brads_bites.User;

@Database(entities = {Item.class, User.class}, version = 3)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    public static final String Items_Name = "Items.db";
    public static final String Items_Table = "items_table";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String CART_TABLE = "CART_TABLE";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract ItemsDAO ItemsDAO();

    public static AppDataBase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class,
                            Items_Name).build();
                }
            }

        }
        return instance;
    }

}
