package com.example.brads_bites;

import static com.example.brads_bites.MainActivity.PREFERENCES_KEY;
import static com.example.brads_bites.MainActivity.USER_ID_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.brads_bites.DB.AppDataBase;
import com.example.brads_bites.DB.ItemsDAO;
import com.example.brads_bites.databinding.ActivityMainBinding;
import com.example.brads_bites.databinding.ActivitySearchBinding;

import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private TextView mSearchDisplay;
    private ActivitySearchBinding binding;
    private List<Item> mItemList;
    private ItemsDAO mItemsDAO;
    private int mUserId;
    private User mUser;
    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getPrefs();
        getDatabase();


        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mItemsDAO.getUserByUserId(mUserId);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mSearchDisplay = binding.searchDisplay;
        refreshDisplay();

    }

    private void refreshDisplay() {
        mItemList = mItemsDAO.getItemsById(mUserId);
        if (!mItemList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Item item : mItemList) {
                sb.append(item.toString());
            }
            mSearchDisplay.setText(sb.toString());
        } else {
            mSearchDisplay.setText(R.string.no_items_yet);
        }
    }

    private void getDatabase() {
        mItemsDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.Items_Name)
                .allowMainThreadQueries()
                .build()
                .ItemsDAO();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}