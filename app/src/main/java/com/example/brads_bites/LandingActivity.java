package com.example.brads_bites;

import static com.example.brads_bites.MainActivity.USER_ID_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.brads_bites.DB.AppDataBase;
import com.example.brads_bites.DB.ItemsDAO;
import com.example.brads_bites.databinding.ActivityLandingBinding;
import com.example.brads_bites.databinding.ActivityMainBinding;

import java.util.List;

public class LandingActivity extends AppCompatActivity {
    private static final String PREFERENCES_KEY = "com.example.brads_bites.Preferences_Key";


    private User mUser;
    private TextView mTextView;
    private Button mAddItem;
    private Button mViewItem;
    private Button mRemoveItem;
    private Button mViewUsers;
    private ItemsDAO mItemsDAO;

    private ActivityLandingBinding binding;

    private SharedPreferences mPreferences = null;
    private int mUserId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        getDatabase();
        getSupportActionBar();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mItemsDAO.getUserByUserId(mUserId);


        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAddItem = binding.buttonAdd;
        mViewItem = binding.buttonViewItems;
        mRemoveItem = binding.buttonRemove;
        mViewUsers = binding.buttonViewUsers;
        mTextView = binding.textViewWelcome;


        if (mUser!=null && mUser.getUserName().equals("admin1")){
            mAddItem.setVisibility(View.VISIBLE);
            mViewItem.setVisibility(View.VISIBLE);
            mRemoveItem.setVisibility(View.VISIBLE);
            mViewUsers.setVisibility(View.VISIBLE);
            mTextView.setText("Welcome Admin");
        }else{
            mAddItem.setVisibility(View.GONE);
            mViewItem.setVisibility(View.GONE);
            mRemoveItem.setVisibility(View.GONE);
            mViewUsers.setVisibility(View.GONE);
            mTextView.setText("Welcome " + mUser.getUserName());
        }



        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });


    }


    private void loginUser(int userId) {
        //check if userID is valid
        mUser = mItemsDAO.getUserByUserId(userId);
        //check if mUser is not null
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }

    private void addUserToPreference(int userId) {
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void getDatabase(){
        mItemsDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.Items_Name)
                .allowMainThreadQueries()
                .build()
                .ItemsDAO();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mUser != null) {
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.userMenuLogout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearUserFromIntent();
                clearUserFromPref();
                mUserId = -1;
                checkForUser();
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertBuilder.create().show();
    }

    private void checkForUser() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        }

        if (mPreferences == null) {
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if (mUserId != -1) {
            return;
        }


        List<User> users = mItemsDAO.getAllUsers();
        if (users.size() <= 0) {
            User admin1 = new User("admin1", "123");
            User admin2 = new User("admin2", "456");
            mItemsDAO.insert(admin1, admin2);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private User getUserFromSharedPreferences() {
        if (mItemsDAO == null){
            getDatabase();
        }

        if (mPreferences == null) {
            getPrefs();
        }
        int userId = mPreferences.getInt(USER_ID_KEY, -1);
        return mItemsDAO.getUserByUserId(userId);
    }



    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}