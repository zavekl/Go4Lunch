package com.brice_corp.go4lunch.repository;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private ArrayList<String> mList = new ArrayList<>();
    //Firestore
    private FirebaseFirestore mFirebaseFirestore;

    //Collections
    private CollectionReference nameNoteRef;

    //Constants collection
    private static final String USERS = "users";

    //Constants documents
    private static final String USER_NAME = "name";

    private MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();

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

    private void getUsersNameFirestore() {
        nameNoteRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    mList.add(Objects.requireNonNull(document.getData().get(USER_NAME)).toString());
                }
                data.setValue(mList);
                Log.i("FirestoreUserRepository", "getUserFirestore 1 " + mList);
                Log.i("FirestoreUserRepository", "livedata " + data.getValue());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });
    }

    public MutableLiveData<ArrayList<String>> getUsersName() {
        new GetUsers(this).execute();

        //        getUsersNameFirestore();
//        MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
//        data.setValue(mList);

        return data;
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







    private static class GetUsers extends AsyncTask<Void, Void, Void> {
        private WeakReference<FirestoreUserRepository> activityReference;

        GetUsers(FirestoreUserRepository firestoreUserRepository) {
            activityReference = new WeakReference<>(firestoreUserRepository);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirestoreUserRepository activity = activityReference.get();
            if (activity != null) activity.getUsersNameFirestore();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            FirestoreUserRepository activity = activityReference.get();
            if (activity != null) {
                activity.data.setValue(activity.mList);
            }
        }
    }








//TODO IMAGE
}
