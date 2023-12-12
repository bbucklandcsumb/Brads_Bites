package com.example.brads_bites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.brads_bites.DB.AppDataBase;

import java.util.Date;

@Entity(tableName = AppDataBase.Items_Table)
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int mItemId;
    private String mDescription;
    private Integer mQuantity;
    private Double mPrice;

    private Date mDate;
    private int mUserId;

    public Item(String description, Integer quantity, Double price, int userId) {
        mDescription = description;
        mQuantity = quantity;
        mPrice = price;

        mDate = new Date();
        mUserId = userId;

    }

    @Override
    public String toString() {
        return "Item # " + mItemId + "\n" +
                "Description: " + mDescription + "\n" +
                "Quantity: " + mQuantity + "\n" +
                "Price: " + mPrice + "\n" +
                "Date: " + mDate + "\n" +
                "=-=-=-=-=-=-=-=-";
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Integer quantity) {
        mQuantity = quantity;
    }

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
