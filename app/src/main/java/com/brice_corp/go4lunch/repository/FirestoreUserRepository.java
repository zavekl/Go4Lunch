package com.brice_corp.go4lunch.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 */
public class FirestoreUserRepository {

    private FirebaseFirestore mFirebaseFirestore;

    //Initialize firestore
    private void initFirestore() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    //Create current user in firestore database
    public void createCurrentUserFirestore() {
        initFirestore();

        DocumentReference mDocumentReference = mFirebaseFirestore.collection("users").document(getUser().getUid());
        final Map<String, Object> user = new HashMap<>();
        user.put("name", getName());
        user.put("email", getEmail());

        mDocumentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("FirestoreUserRepository", "onSuccess: User profile is successfully created in firestore ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });
    }

    //Get current user
    private FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //Get name of the current user
    private String getName() {
        return getUser().getDisplayName();
    }

    //Get the email of the current user
    private String getEmail() {
        return getUser().getEmail();
    }

    //TODO IMAGE
}
