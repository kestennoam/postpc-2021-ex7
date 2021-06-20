package com.example.postpc_ex7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public final String NO_SANDWICH = "not ordered yet";
    private final String CUSTOMER_NAME = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RachelApp app = RachelApp.getInstance();
        Database db = Database.getInstance();

        //todo set user name app and order
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String sandwichId = sp.getString("sandwichId", NO_SANDWICH);



        // no sandwich yet
        if (sandwichId.equals(NO_SANDWICH)) {
            System.out.println("No sandwich yet");
            moveToNewOrderActivity();
            return;
        }

        //
        Task<DocumentSnapshot> documentSnapshotTask = db.downloadSandwich(sandwichId)
                .addOnSuccessListener(res -> {
                    if (res == null) {
                        System.out.println("moved to null");
                        moveToNewOrderActivity();
                        return;
                    }
                    Sandwich sandwich = res.toObject(Sandwich.class);
                    if (sandwich == null) {
                        System.out.println("moved to sandwich null");
                        moveToNewOrderActivity();
                    } else if (sandwich.getStatus().equals("init")) {
                        System.out.println("init status");
                        moveToNewOrderActivity();
                    } else if (sandwich.getStatus().equals("waiting")) {
                        System.out.println("moved to sandwich waiting");
                        moveToEditOrderActivity();
                    } else if (sandwich.getStatus().equals("in-progress")) {
                        System.out.println("moved to sandwich in progress");
                        moveToInProgressOrderActivity();
                    } else if (sandwich.getStatus().equals("ready")) {
                        System.out.println("moved to sandwich ready");
                        moveToReadyOrderActivity();
                    } else if (sandwich.getStatus().equals("done")) {
                        System.out.println("moved to sandwich done");
                        moveToNewOrderActivity();
                    } else {
                        System.err.println("Error in status");
                    }
                })
                .addOnFailureListener(res -> System.err.println("error in reading order id"));

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
                        if (error != null) {
                            System.out.println("error on listening");
                        } else if (value == null) {
                            System.out.println("value is null");
                        } else if (!value.exists()) {
                            // handle delete

                        } else {
                            System.out.println("document was changed");
                            Sandwich sandwich = value.toObject(Sandwich.class);


                        }
                    }
                });

    }
}


