package com.example.postpc_ex7;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.UUID;

public class NewOrderActivity extends AppCompatActivity {
    public final String NO_SANDWICH = "not ordered yet";
    public final String NO_NAME = "noname";

    private static final int MAX_PICKLES = 10;

    Database db;
    SharedPreferences sp;
    LiveData<Sandwich> sandwichLiveData;
    Sandwich sandwich;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);


        sp.edit().putString("sandwichId", NO_SANDWICH).apply(); //ensure cleaning db
        sandwich = new Sandwich();

        // set sandwich
        sandwichLiveData = db.getLiveDataSandwich(sandwich.getId());
        // ensure not any sandwich on db
        sandwichLiveData.observe(this, sandwich -> {
            if (sandwich == null) {
                Log.d("NewOrderActivity", "sandwich is not in db");
            } else {
                Log.d("NewOrderActivity", "sandwich is in db");
                sp.edit().putString("sandwichId", sandwich.getId()).apply();

                // move to other activity
                Intent intent = new Intent(this, EditOrderActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // set ui components
        TextView costumerIdTitle = findViewById(R.id.costumerIdTextView);
        EditText costumerIdEditTextView = findViewById(R.id.costumerIdEditTextView);

        Button picklesIncreaseButton = findViewById(R.id.increase);
        Button picklesDecreaseButton = findViewById(R.id.decrease);
        picklesDecreaseButton.setEnabled(false);
        TextView picklesTextView = findViewById(R.id.integer_number);

        Switch tahiniSwitch = findViewById(R.id.tahiniSwitch);
        Switch hummusSwitch = findViewById(R.id.hummusSwitch);

        EditText commentEditText = findViewById(R.id.commentEditText);

        ImageButton addSandwichButton = findViewById(R.id.addButton);
//        addSandwichButton.setEnabled(false);


        // costumer
        String costumerName = sp.getString("costumerName", NO_NAME);
        if (!costumerName.equals(NO_NAME)) {
            costumerIdEditTextView.setVisibility(View.GONE);
            costumerIdTitle.setText("Welcome " + costumerName);
        }
        costumerIdEditTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    System.out.println("111" + costumerIdEditTextView.getText().toString());
                    if (costumerIdEditTextView.getText().toString().trim().length() > 0) {
                        costumerIdTitle.setText("Welcome " + costumerIdEditTextView.getText().toString());
                        costumerIdEditTextView.setVisibility(View.GONE);
                        sp.edit().putString("costumerName", costumerIdEditTextView.getText().toString()).apply();
                        System.out.println("check check " + sp.getString("costumerName", "nooo"));
                    }
                }
                addSandwichButton.setEnabled(costumerIdEditTextView.getVisibility() == View.GONE);
            }
        });

        costumerIdTitle.setOnClickListener(v -> {
                addSandwichButton.setEnabled(costumerIdEditTextView.getVisibility() == View.GONE);
            if (costumerIdEditTextView.getVisibility() == View.GONE) {
                costumerIdTitle.setText("Edit Your Name");
                costumerIdEditTextView.setVisibility(View.VISIBLE);

            }
        });


        // pickles
        picklesIncreaseButton.setOnClickListener(v -> {
            int pickles = Integer.parseInt(picklesTextView.getText().toString());
            ++pickles;
            // check limit
            if (pickles == MAX_PICKLES) {
                picklesIncreaseButton.setEnabled(false);
            }
            picklesDecreaseButton.setEnabled(true);
            picklesTextView.setText(Integer.toString(pickles));
        });

        picklesDecreaseButton.setOnClickListener(v -> {
            int pickles = Integer.parseInt(picklesTextView.getText().toString());
            --pickles;
            // check limit
            if (pickles == 0) {
                picklesIncreaseButton.setEnabled(false);
            }
            picklesIncreaseButton.setEnabled(true);
            picklesTextView.setText(Integer.toString(pickles));
        });

        // add button
        addSandwichButton.setOnClickListener(v -> {
            Log.d("NewOrderActivity", "Add button was pressed");
            sandwich.setCustomerName(costumerIdEditTextView.getText().toString());
            sandwich.setComment(commentEditText.getText().toString());
            sandwich.setTahini(tahiniSwitch.isChecked());
            sandwich.setHummus(hummusSwitch.isChecked());
            sandwich.setPickles(Integer.parseInt(picklesTextView.getText().toString()));
            sandwich.setStatus("waiting");
            db.addSandwich(sandwich);
            addSandwichButton.setEnabled(false);
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

    private void moveToInProgressOrderActivity() {
        Intent intent = new Intent(this, InProgressActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToReadyOrderActivity() {
        Intent intent = new Intent(this, ReadyOrderActivity.class);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


