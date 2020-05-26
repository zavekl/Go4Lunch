package com.brice_corp.go4lunch.utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.brice_corp.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.Arrays;
import java.util.List;

import static com.brice_corp.go4lunch.utils.Constants.AUTHENTICATION;

/**
 * Created by <BRICE NIATEL> on <15/04/2020>.
 */
public class AuthenticationUtils {

    //Providers list
    private List<AuthUI.IdpConfig> initProviders() {
        return Arrays.asList(
                new AuthUI.IdpConfig.FacebookBuilder().build(),     //FaceBoob builder
                new AuthUI.IdpConfig.GoogleBuilder().build()       //Google builder
        );
    }

    public void showSignInOptions(Activity activity) {
        activity.startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false,true).setAvailableProviders(initProviders()).setTheme(R.style.SignInTheme).setLogo(R.drawable.go4lunch_logo).build(), AUTHENTICATION);
    }

    //Signout
    public static void signOut(final @NonNull Context context, @NonNull OnCompleteListener onCompleteListener, @NonNull OnFailureListener onFailureListener) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
}


