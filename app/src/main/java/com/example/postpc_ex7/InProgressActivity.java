package com.example.postpc_ex7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class InProgressActivity extends AppCompatActivity {
    private Database db;
    private SharedPreferences sp;
    private LiveData<Sandwich> sandwichLiveData;
    private Sandwich sandwich;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_progress);

        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        // set sandwich
        LiveData<Sandwich> sandwichLiveData = db.getLiveDataSandwich(sp.getString("sandwichId", "didn't found"));
        sandwich = sandwichLiveData.getValue();

        // ensure not any sandwich on db
        sandwichLiveData.observe(this, sandwich -> {
            if (sandwich == null) {
                Log.d("NewOrderActivity", "sandwich is not in db");
                moveToNewOrderActivity();
            } else {
                Log.d("NewOrderActivity", "sandwich is in db");
                switch (sandwich.getStatus()) {
                    case "waiting":
                        Log.d("NewOrderActivity", "status is waiting");
                        moveToEditOrderActivity();
                        break;
                    case "in-progress":
                        Log.d("NewOrderActivity", "status is in progress");
                        break;
                    case "ready":
                        Log.d("NewOrderActivity", "status is ready");
                        moveToReadyOrderActivity();
                        break;
                    case "done":
                        Log.d("NewOrderActivity", "status is done");
                        moveToNewOrderActivity();
                        break;
                }
            }
        });
    }

    private void moveToNewOrderActivity() {
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToEditOrderActivity() {
        Intent intent = new Intent(this, EditOrderActivity.class);
        startActivity(intent);
        finish();
    }


    private void moveToReadyOrderActivity() {
        Intent intent = new Intent(this, ReadyOrderActivity.class);
        startActivity(intent);
        finish();
    }
}
