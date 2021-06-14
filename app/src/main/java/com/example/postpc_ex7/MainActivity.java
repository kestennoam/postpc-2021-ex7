package com.example.postpc_ex7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_rachel_sandwich);
        View textViewRachelSandwich = findViewById(R.id.textViewRachelSandwich);

//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//        firestore.collection("colors").get()
//                .addOnSuccessListener(result -> {
//                    List<DocumentSnapshot> documents = new ArrayList<>(result.getDocuments());
//                    List<Sandwich> sandwiches = new ArrayList<>();
//                    for (DocumentSnapshot document : documents) {
//                        Sandwich sandwich = document.toObject(Sandwich.class);
//                        if (sandwich != null) {
//                            sandwiches.add(sandwich);
//                        }
//                    }
//                    System.out.println(sandwiches);
//                })
//                .addOnFailureListener((err) -> System.out.println("Error:"));
    }














    private void downloadSandwich(String id) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("sandwiches").document(id).get()
                .addOnSuccessListener(System.out::println)
                .addOnFailureListener(res -> System.out.println("error"));


    }

    private void uploadAddSandwich(Sandwich sandwich) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String newId = UUID.randomUUID().toString();
        firestore.collection("sandwiches").document(newId).set(sandwich)
                .addOnSuccessListener(res -> System.out.println(sandwich + " added successfully"))
                .addOnFailureListener(res -> System.out.println("error"));

    }

    private void uploadUpdateSandwich(Sandwich sandwich) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String newId = UUID.randomUUID().toString();
        firestore.collection("sandwiches").document(newId).set(sandwich)
                .addOnSuccessListener(res -> System.out.println(sandwich + " added successfully"))
                .addOnFailureListener(res -> System.out.println("error"));

    }

    private void uploadDeleteSandwich(String collection, String idToDelete) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection(collection).document(idToDelete).delete()
                .addOnSuccessListener(res -> System.out.println("Deleted successfully"))
                .addOnFailureListener(res -> System.out.println("error while deleting"));
    }


    private void listenToChangesOnDocument(String collection, String documentId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // at the end should do listener.remove()
        ListenerRegistration listener = firestore.collection(collection).document(documentId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            System.out.println("error on listening");
                        }
                        else if (value == null){
                            System.out.println("value is null");
                        }
                        else if (!value.exists()){
                            // handle delete

                        }
                        else{
                            System.out.println("document was changed");
                            Sandwich sandwich = value.toObject(Sandwich.class);


                        }
                    }
                });

    }
}


