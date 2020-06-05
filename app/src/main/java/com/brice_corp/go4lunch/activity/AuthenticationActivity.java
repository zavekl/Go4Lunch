package com.brice_corp.go4lunch.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.utils.AuthenticationUtils;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.brice_corp.go4lunch.utils.Constants.AUTHENTICATION;
import static com.brice_corp.go4lunch.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        if (checkPermission()) {
            displaySignIn();
        } else {
            getLocationPermission();
        }
    }

    //Get the result of the Location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displaySignIn();
            }
        }
    }

    //Get the mail of the user and start the new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHENTICATION) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //Set user in firestore
                ((MyApplication) getApplication()).getContainerDependencies().getFirestoreUserRepository().checkIfUserAlreadyCreated();

                //Show email
                Log.e("onActivityResult", "onActivityResult: " + user.getEmail());

                //Start the new activity
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
            } else {
                Log.e("onActivityResult", "onActivityResult: " + response.getError().getMessage());
            }
        }
    }

    //Build the API display signin
    private void displaySignIn() {
        //Instanciate the AuthenticationUtils class
        AuthenticationUtils authenticationUtils = new AuthenticationUtils();

        //Display signin
        authenticationUtils.showSignInOptions(this);
    }

    //Make the request Location permission
    private void getLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    //Check if the permission is granted
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
