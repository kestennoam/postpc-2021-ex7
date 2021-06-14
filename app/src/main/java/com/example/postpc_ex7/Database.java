package com.example.postpc_ex7;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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

    ;

    public static Database getInstance() {
        return instance;
    }

    public void delete(Sandwich sandwich) {
        sandwiches.remove(sandwich.getId());
        mutableLiveData.setValue(new ArrayList<>(sandwiches.values()));
        firestore.collection("sandwiches").document(sandwich.getId()).delete();
    }

    public void add(Sandwich sandwich) {
        String newId = UUID.randomUUID().toString();
        sandwiches.put(newId, sandwich);
        mutableLiveData.setValue(new ArrayList<>());
        firestore.collection("sandwiches").document(sandwich.getId()).set(sandwich);
    }

    public LiveData<List<Sandwich>> getSandwichesLiveData() {
        return mutableLiveData;
    }


}
