package com.example.brads_bites;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.brads_bites.DB.AppDataBase;
import com.example.brads_bites.DB.ItemsDAO;
import com.example.brads_bites.databinding.ActivityMainBinding;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.brads_bites.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.brads_bites.Preferences_Key";
    private ActivityMainBinding binding;

    private TextView mMainDisplay;

    private EditText mDescription;
    private EditText mQuantity;
    private EditText mPrice;

    private TextView mAdmin;

    private Button mSubmit;

    private ItemsDAO mItemsDAO;

    private List<Item> mItemList;
    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private User mUser;

    private Menu mOptionsMenu;
    private MenuItem mItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar();

        getDatabase();

        checkForUser();

        loginUser(mUserId);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mMainDisplay = binding.mainItemDisplay;
        mDescription = binding.mainDescriptionEditText;
        mQuantity = binding.mainQuantityEditText;
        mPrice = binding.mainPriceEditText;
        mAdmin = binding.textViewAdmin;
        mSubmit = binding.mainSubmitButton;

        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());


        refreshDisplay();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = getValuesFromDisplay();
                submitItem();
                refreshDisplay();
            }
        });

        //Admin functionality
        /*
        if (mUser!=null && mUser.getUserName().equals("drew")){
            mAdmin.setVisibility(View.VISIBLE);
        }else{
            mAdmin.setVisibility(View.GONE);
        }*/

    }// end of onCreate

    private void getDatabase() {
        mItemsDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.Items_Name)
                .allowMainThreadQueries()
                .build()
                .ItemsDAO();
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
            User defaultUser = new User("bbuck", "buck");
            User altUser = new User("buck", "buck123");
            mItemsDAO.insert(defaultUser);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void loginUser(int userId) {
        //check if userID is valid
        mUser = mItemsDAO.getUserByUserId(userId);
        //check if mUser is not null
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mUser != null) {
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void addUserToPreference(int userId) {
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void submitItem() {
        String description = mDescription.getText().toString();
        int quantity = Integer.parseInt(mQuantity.getText().toString());
        double price = Double.parseDouble(mPrice.getText().toString());

        Item item = new Item(description, quantity, price, mUserId);
        mItemsDAO.insert(item);
    }

    private void refreshDisplay() {
        mItemList = mItemsDAO.getItemsById(mUserId);
        if (!mItemList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Item item : mItemList) {
                sb.append(item.toString());
            }
            mMainDisplay.setText(sb.toString());
        } else {
            mMainDisplay.setText(R.string.no_items_yet);
        }
    }



    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }

    private Item getValuesFromDisplay() {
        String description = getString(R.string.nothing_found);
        int quantity = 0;
        double price = 0.0;

        description = mDescription.getText().toString();

        try {
            quantity = Integer.parseInt((mQuantity.getText().toString()));
        } catch (NumberFormatException e) {
            Log.d("ITEM", "Couldn't convert quantity");
        }

        try {
            price = Double.parseDouble(mPrice.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("Item", "Couldn't convert price");
        }

        return new Item(description, quantity, price, mUserId);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
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

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
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

    /*private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY, -1);
    }*/
}