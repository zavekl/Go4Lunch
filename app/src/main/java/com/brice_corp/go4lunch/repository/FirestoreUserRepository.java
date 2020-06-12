package com.brice_corp.go4lunch.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.brice_corp.go4lunch.model.Restaurant;
import com.brice_corp.go4lunch.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by <NIATEL Brice> on <27/05/2020>.
 */
public class FirestoreUserRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attributes
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Firestore
    private FirebaseFirestore mFirebaseFirestore;

    //Collections
    private CollectionReference mNameNoteRef;

    //Constants
    private static final String USERS = "users";
    private static final String USER_NAME = "name";
    private String CURRENT_USER_ID;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Constructor
    public FirestoreUserRepository() {
        initFirestore();
    }

    //Initialize firestore
    private void initFirestore() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mNameNoteRef = mFirebaseFirestore.collection(USERS);
        CURRENT_USER_ID = getCurrentUser().getUid();
    }

    //Check if current user is already created in database, if not create it
    public void checkIfUserAlreadyCreated() {
        mNameNoteRef.document(CURRENT_USER_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    Log.i("FirestoreUserRepository", "The user is already created in database");
                } else {
                    createCurrentUserFirestore();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });
    }

    //Create current user in firestore database
    private void createCurrentUserFirestore() {
        DocumentReference mDocumentReference = mNameNoteRef.document(CURRENT_USER_ID);

        final Map<String, Object> user = new HashMap<>();
        user.put("name", getUser().getName());
        user.put("email", getUser().getmEmail());

        mDocumentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FirestoreUserRepository", "onSuccess: User profile is successfully created in firestore ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });

    }

    //Get current user
    private User getUser() {
        FirebaseUser user = getCurrentUser();
        //TODO lier l'ID du resto
        Restaurant restaurant = new Restaurant("","Chez Hans", "Allemand", "42 rue du Panzer", "Ouvre bientôt");
        return new User(user.getDisplayName(), user.getEmail(), restaurant.getId());
    }

    //Get name of the current user
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //Get the call of query
    public Query getQuery() {
        return mNameNoteRef.orderBy(USER_NAME, Query.Direction.ASCENDING);
    }
}
