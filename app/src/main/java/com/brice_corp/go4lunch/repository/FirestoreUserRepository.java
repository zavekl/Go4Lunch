package com.brice_corp.go4lunch.repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Created by <NIATEL Brice> on <27/05/2020>.
 */
public class FirestoreUserRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attributes
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String TAG = "FirestoreUserRepository";

    //Collections
    private CollectionReference mNameNoteRef;

    //Document Reference
    private DocumentReference mDocumentReference;

    //Constants
    private static final String USERS = "users";
    private static final String USER_NAME = "name";
    private String CURRENT_USER_ID;
    public static User mUserShared;

    //Livedata
    private MutableLiveData<Boolean> mLikeLivedata = new MutableLiveData<>();
    private MutableLiveData<String> mEatTodayLivedata = new MutableLiveData<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //Constructor
    public FirestoreUserRepository() {
        mNameNoteRef = FirebaseFirestore.getInstance().collection(USERS);
        CURRENT_USER_ID = getCurrentUser().getUid();
    }

    //Check if current user is already created in database, if not create it
    public void checkIfUserAlreadyCreated() {
        mNameNoteRef.document(CURRENT_USER_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null) {
                    if (task.getResult().exists()) {
                        Log.i("FirestoreUserRepository", "The user is already created in database");
                    } else {
                        createCurrentUserFirestore();
                    }
                } else {
                    Log.e(TAG, "onComplete: result of task = null");
                }
            }
        }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirestoreUserRepository", "onFailure: ", e);
                    }
                });
    }

    //Initialize document reference
    private void setDocumentReference() {
        mDocumentReference = mNameNoteRef.document(CURRENT_USER_ID);
    }

    //Create current user in firestore database
    private void createCurrentUserFirestore() {
        setDocumentReference();

        final Map<String, Object> user = new HashMap<>();
        user.put("name", getUser().getName());
        user.put("email", getUser().getEmail());
        user.put("image", getUser().getImage());

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
    public User getUser() {
        FirebaseUser user = getCurrentUser();
        mUserShared = new User(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), "");
        return mUserShared;
    }

    //Get name of the current user
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //Get the query of all the database
    public Query getQueryWorkmates() {
        return mNameNoteRef.orderBy(USER_NAME, Query.Direction.ASCENDING);
    }

    //Get the query of the actual restaurant
    public Query getQueryDescription(@NonNull String idREstaurant) {
        return mNameNoteRef.orderBy(USER_NAME, Query.Direction.ASCENDING).whereEqualTo("eatToday", idREstaurant);
    }

    //Set the true like in firestore
    public void setUserLikeRestaurantTrue(@NonNull String id) {
        setDocumentReference();
        final Map<String, Object> user = new HashMap<>();
        user.put(id, true);

        mDocumentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FirestoreUserRepository", "onSuccess: User profile is successfully update with the id to true");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });
    }

    //Set the false like in firestore
    public void setUserLikeRestaurantFalse(@NonNull String id) {
        setDocumentReference();
        final Map<String, Object> user = new HashMap<>();
        user.put(id, false);

        mDocumentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FirestoreUserRepository", "onSuccess: User profile is successfully update with the id to false ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });
    }

    //Get the like boolean from firestore
    public MutableLiveData<Boolean> getTheLikeRestaurant(final String id) {
        if (id != null) {
            setDocumentReference();

            mNameNoteRef.document(CURRENT_USER_ID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e == null) {
                        if (documentSnapshot != null) {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.get(id) == null) {
                                    setUserLikeRestaurantFalse(id);
                                    Log.i(TAG, "getTheLikeRestaurant: first creation of like restaurant");
                                    mLikeLivedata.setValue(false);
                                } else {
                                    mLikeLivedata.setValue(Boolean.valueOf(Objects.requireNonNull(documentSnapshot.get(id)).toString()));
                                    Log.i(TAG, "getTheLikeRestaurant: result" + mLikeLivedata.getValue());
                                }
                            } else {
                                Log.i(TAG, "getTheLikeRestaurant : document don't exist");
                            }
                        }
                    } else {
                        Log.e(TAG, "getTheLikeRestaurant error :", e);
                    }
                }
            });
            return mLikeLivedata;
        } else {
            mLikeLivedata.setValue(false);
            return mLikeLivedata;
        }

    }

    //Get the eat today boolean from firestore
    public MutableLiveData<String> getTheEatToday() {
        mNameNoteRef.document(CURRENT_USER_ID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    if (documentSnapshot != null) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.get("eatToday") == null) {
                                setEatTodayRestaurantFalse();
                                Log.i(TAG, "getTheEatToday: first creation of like restaurant");
                                mEatTodayLivedata.setValue("");
                            } else {
                                mEatTodayLivedata.setValue(Objects.requireNonNull(documentSnapshot.get("eatToday")).toString());
                                Log.i(TAG, "getTheEatToday: result" + mEatTodayLivedata.getValue());
                            }
                        } else {
                            Log.i(TAG, "getTheEatToday : document don't exist");
                        }
                    } else {
                        Log.i(TAG, "getTheEatToday : documentSnapshot = null");
                    }
                } else {
                    Log.e(TAG, "getTheEatToday error :", e);
                }
            }
        });
        return mEatTodayLivedata;
    }

    //Set the true eat today in firestore
    public void setUserEatTodayRestaurantTrue(@NonNull String id, @NonNull String name) {
        setDocumentReference();
        final Map<String, Object> user = new HashMap<>();
        user.put("eatToday", id);
        user.put("eatTodayName", name);

        mDocumentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FirestoreUserRepository", "onSuccess: User profile is successfully update with restaurant where the user eat today on  true");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });
    }

    //Set the false eat today in firestore
    public void setEatTodayRestaurantFalse() {
        setDocumentReference();
        final Map<String, Object> user = new HashMap<>();
        user.put("eatToday", "");
        user.put("eatTodayName", "");

        mDocumentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("FirestoreUserRepository", "onSuccess: User profile is successfully update with restaurant where the user eat today on false");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreUserRepository", "onFailure: ", e);
            }
        });
    }

    public Task<QuerySnapshot> getUsersDocuments() {
        return mNameNoteRef.get();
    }
}
