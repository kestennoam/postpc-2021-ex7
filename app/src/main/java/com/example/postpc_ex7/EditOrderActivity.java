package com.example.postpc_ex7;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

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

public class EditOrderActivity extends AppCompatActivity {

    private static final int MAX_PICKLES = 10;
    public final String NO_NAME = "noname";

    private Database db;
    private SharedPreferences sp;
    private LiveData<Sandwich> sandwichLiveData;
    private Sandwich sandwich;

    private TextView costumerIdTitle;
    private EditText costumerIdEditTextView;
    private Button picklesIncreaseButton;
    private Button picklesDecreaseButton;
    private TextView picklesTextView;
    private EditText commentEditText;
    private Switch tahiniSwitch;
    private Switch hummusSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

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
            } else {
                Log.d("NewOrderActivity", "sandwich is in db");
                switch (sandwich.getStatus()) {
                    case "waiting":
                        Log.d("NewOrderActivity", "status is waiting");
                        break;
                    case "in-progress":
                        Log.d("NewOrderActivity", "status is in progress");
                        moveToInProgressOrderActivity();
                        break;
                    case "ready":
                        Log.d("NewOrderActivity", "status is ready");
//                        moveToReadyActivity();
                        break;
                    case "done":
                        Log.d("NewOrderActivity", "status is done");
//                        moveToDoneActivity();
                        break;

                }
            }
        });

        // set ui components
        costumerIdTitle = findViewById(R.id.costumerIdTextView);
        costumerIdEditTextView = findViewById(R.id.costumerIdEditTextView);

        picklesIncreaseButton = findViewById(R.id.increase);
        picklesDecreaseButton = findViewById(R.id.decrease);
        picklesDecreaseButton.setEnabled(false);
        picklesTextView = findViewById(R.id.integer_number);

        tahiniSwitch = findViewById(R.id.tahiniSwitch);
        hummusSwitch = findViewById(R.id.hummusSwitch);

        commentEditText = findViewById(R.id.commentEditText);

        ImageButton editButton = findViewById(R.id.editButton);
        ImageButton saveButton = findViewById(R.id.saveButton);
        ImageButton deleteButton = findViewById(R.id.deleteButton);

        // set sandwich fields
        updateSandwichFields();

        // set configuration
        editButton.setOnClickListener(v -> {
            Log.d("editActivity", "edit button was clicked");
            changeEditMode(true);
            saveButton.setEnabled(true);
            checkBoundsPickles(sandwich.getPickles());
        });

        saveButton.setOnClickListener(v -> {
            Log.d("NewOrderActivity", "Add button was pressed");
            sandwich.setCustomerName(costumerIdEditTextView.getText().toString());
            sandwich.setComment(commentEditText.getText().toString());
            sandwich.setTahini(tahiniSwitch.isChecked());
            sandwich.setHummus(hummusSwitch.isChecked());
            sandwich.setPickles(Integer.parseInt(picklesTextView.getText().toString()));
            sandwich.setStatus("waiting");
            db.addSandwich(sandwich);
            saveButton.setEnabled(false);
            changeEditMode(false);
        });

        deleteButton.setOnClickListener(v -> {
            db.deleteSandwich(sandwich);
            Intent intent = new Intent(this, NewOrderActivity.class);
            startActivity(intent);
            finish();
        });


        //todo costumer name and change plus button to true
        //todo costumer name and change plus button to true
        String costumerName = sp.getString("costumerName", NO_NAME);
        System.out.println("costuemerName: " + costumerName);
        if (!costumerName.equals(NO_NAME)) {
            costumerIdEditTextView.setVisibility(View.GONE);
            costumerIdTitle.setText("Welcome " + costumerName);
        }
        costumerIdEditTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    System.out.println("111" + costumerIdEditTextView.getText().toString());
                    if (costumerIdEditTextView.getText().toString().trim().length() > 0) {
                        costumerIdTitle.setText("Welcome " + costumerIdEditTextView.getText().toString());
                        costumerIdEditTextView.setVisibility(View.GONE);
                        sp.edit().putString("costumerName", costumerIdEditTextView.getText().toString());
                    }
                }
                saveButton.setEnabled(costumerIdEditTextView.getVisibility() == View.GONE);
            }
        });

        costumerIdTitle.setOnClickListener(v -> {
            saveButton.setEnabled(costumerIdEditTextView.getVisibility() == View.GONE);
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
                picklesDecreaseButton.setEnabled(false);
            }
            picklesIncreaseButton.setEnabled(true);
            picklesTextView.setText(Integer.toString(pickles));
        });


    }

    private void updateSandwichFields() {
        hummusSwitch.setChecked(sandwich.isHummus());
        tahiniSwitch.setChecked(sandwich.isTahini());
        picklesTextView.setText(Integer.toString(sandwich.getPickles()));
        commentEditText.setText(sandwich.getComment());
        changeEditMode(false);
    }

    private void changeEditMode(boolean bool) {
        costumerIdEditTextView.setEnabled(bool);
        hummusSwitch.setClickable(bool);
        tahiniSwitch.setClickable(bool);
        picklesDecreaseButton.setEnabled(bool);
        picklesIncreaseButton.setEnabled(bool);
        commentEditText.setEnabled(bool);
        costumerIdEditTextView.setEnabled(bool);
        costumerIdTitle.setEnabled(bool);
    }

    private void checkBoundsPickles(int pickles) {
        System.out.println("1212");
        if (pickles <= 0) {
            System.out.println("Noam");
            picklesDecreaseButton.setEnabled(false);
        }
        if (pickles >= MAX_PICKLES) {
            picklesIncreaseButton.setEnabled(false);
        }
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


