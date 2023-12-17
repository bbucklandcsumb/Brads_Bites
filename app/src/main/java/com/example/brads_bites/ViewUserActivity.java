package com.example.brads_bites;

import static com.example.brads_bites.MainActivity.PREFERENCES_KEY;
import static com.example.brads_bites.MainActivity.USER_ID_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.brads_bites.DB.AppDataBase;
import com.example.brads_bites.DB.ItemsDAO;
import com.example.brads_bites.databinding.ActivitySearchBinding;
import com.example.brads_bites.databinding.ActivityViewUserBinding;

import java.util.List;

public class ViewUserActivity extends AppCompatActivity {

    private TextView mViewUserDisplay;
    private ActivityViewUserBinding binding;
    private List<User> mUserList;
    private ItemsDAO mItemsDAO;
    private int mUserId;
    private User mUser;
    private SharedPreferences mPreferences = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        getPrefs();
        getDatabase();

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mItemsDAO.getUserByUserId(mUserId);

        binding = ActivityViewUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewUserDisplay = binding.viewUserDisplay;
        refreshDisplay();

    }

    private void refreshDisplay() {
        mUserList = mItemsDAO.getAllUsers();
        if (!mUserList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (User user : mUserList) {
                sb.append(user.toString());
            }
            mViewUserDisplay.setText(sb.toString());
        } else {
            mViewUserDisplay.setText(R.string.no_items_yet);
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
        Intent intent = new Intent(context, ViewUserActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}