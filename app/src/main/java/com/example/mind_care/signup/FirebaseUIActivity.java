package com.example.mind_care.signup;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mind_care.MainActivity;
import com.example.mind_care.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class FirebaseUIActivity extends AppCompatActivity {


    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(new FirebaseAuthUIActivityResultContract(), new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
        @Override
        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
            onSignInResult(result);
        }
    });


    public void signInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(), new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setLogo(R.mipmap.ic_launcher_foreground_official).setTheme(R.style.AppTheme).build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            //Add new user to database
            if (response.isNewUser()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                createNewUserDocument(user);
            }

            // Successfully signed in
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        configureFirebaseServices();
//        AuthUI.getInstance().signOut(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            signInIntent();
        }

    }

    private void createNewUserDocument(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(new HashMap<>());
    }
}
