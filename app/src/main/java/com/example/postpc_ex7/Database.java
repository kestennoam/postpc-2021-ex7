package com.example.postpc_ex7;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Database {
    private static Database instance;
    private Map<String, Sandwich> sandwiches = new HashMap<>();
    private MutableLiveData<List<Sandwich>> mutableLiveData = new MutableLiveData<>();
    private FirebaseFirestore firestore;


    // sandwiches in firestore: sandwiches/


    private Database() {
        mutableLiveData.setValue(new ArrayList<>());
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("sandwiches").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // handle error
                } else if (value == null) {
                    // handle empty
                } else {
                    List<DocumentSnapshot> documents = value.getDocuments();
                    sandwiches.clear();
                    for (DocumentSnapshot document : documents) {
                        Sandwich sandwich = document.toObject(Sandwich.class);
                        sandwiches.put(sandwich.getId(), sandwich);
                    }
                }
                mutableLiveData.setValue(new ArrayList<>(sandwiches.values()));
            }

        });
    }

    // instance
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }


    // sandwiches
    public Task<DocumentSnapshot> downloadSandwich(String sandwichId) {
        return firestore.collection("sandwiches").document(sandwichId).get();
    }

    public void deleteSandwich(Sandwich sandwich) {
        sandwiches.remove(sandwich.getId());
        mutableLiveData.setValue(new ArrayList<>(sandwiches.values()));
        firestore.collection("sandwiches").document(sandwich.getId()).delete();
    }

    public void addSandwich(Sandwich sandwich) {
        String newId = UUID.randomUUID().toString();
        sandwiches.put(newId, sandwich);
        mutableLiveData.setValue(new ArrayList<>());
        firestore.collection("sandwiches").document(sandwich.getId()).set(sandwich);
    }

    public LiveData<List<Sandwich>> getSandwichesLiveData() {
        return mutableLiveData;
    }

    public LiveData<Sandwich> getLiveDataSandwich(String sandwichId) {
        System.out.println("1");
        MutableLiveData<Sandwich> mutableLiveDataSandwich = new MutableLiveData<>(sandwiches.get(sandwichId));
        System.out.println("2");
//        mutableLiveDataSandwich.setValue();
        firestore.collection("sandwiches").document(sandwichId).addSnapshotListener((v, err) -> {
        System.out.println("3");
            if (err != null) {
                Log.e(Database.class.toString(), "failure" + sandwichId + " from db due to the err: " + err);
            } else {
                System.out.println("4");
                assert v != null;
                Sandwich sandwich = v.toObject(Sandwich.class);
                if (sandwich == null) {
                    Log.e(Database.class.toString(), "Fail to extract " + sandwichId);
                }
                System.out.println("5");
                mutableLiveDataSandwich.setValue(sandwich);
                System.out.println("6");
                Log.d(Database.class.toString(), "Sandwich extracted successfully");
                System.out.println("7");
            }
        });
        return mutableLiveDataSandwich;
    }


}
