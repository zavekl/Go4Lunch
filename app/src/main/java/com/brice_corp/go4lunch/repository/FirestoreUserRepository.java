package com.brice_corp.go4lunch.repository;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.brice_corp.go4lunch.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by <NIATEL Brice> on <27/05/2020>.
 */
public class FirestoreUserRepository {
    private static final String TAG = "FirestoreUserRepository";
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attributes
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Firestore
    private FirebaseFirestore mFirebaseFirestore;

    //Collections
    private CollectionReference nameNoteRef;

    //Constants
    private static final String USERS = "users";
    private static final String USER_NAME = "name";

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
        nameNoteRef = mFirebaseFirestore.collection(USERS);
    }

    //Create current user in firestore database
    public void createCurrentUserFirestore() {
        DocumentReference mDocumentReference = mFirebaseFirestore.collection("users").document(getUser().getUid());
        final Map<String, Object> user = new HashMap<>();
        user.put("name", getCurrentUserName());
        user.put("email", getCurrentUserEmail());

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
    private String getCurrentUserName() {
        return getUser().getDisplayName();
    }

    //Get the email of the current user
    private String getCurrentUserEmail() {
        return getUser().getEmail();
    }

    //Get the call of query
    public Query getQuery() {
        //Query firestore
        return nameNoteRef.orderBy(USER_NAME, Query.Direction.ASCENDING);
    }

//TODO IMAGE
}
