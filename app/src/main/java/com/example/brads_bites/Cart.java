package com.example.brads_bites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.brads_bites.DB.AppDataBase;

import java.util.Date;

@Entity(tableName = AppDataBase.CART_TABLE)
public class Cart {

    @PrimaryKey(autoGenerate = true)
    private int mCartId;

    private int mUserId;
    private int mItemId;
    private int mQuantity;
    private Date mDate;

    public Cart(int quantity, Date date) {
        mQuantity = quantity;
        mDate = date;
    }

    public int getCartId() {
        return mCartId;
    }

    public void setCartId(int cartId) {
        mCartId = cartId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
