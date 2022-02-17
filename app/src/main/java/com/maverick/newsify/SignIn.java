package com.maverick.newsify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn extends AppCompatActivity {

    SignInButton login_with_google;

    GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Integer> launcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initComponents();
        login_with_google = findViewById(R.id.login_with_google);
    }

    private void initComponents(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
        launcher = registerForActivityResult(new GetGoogleSignInDetails(), result -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                CreateFirebaseUserFromGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(SignIn.this, "Failed Authentication", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        login_with_google.setOnClickListener(v->{
            launcher.launch(12);
        });
    }

    private void CreateFirebaseUserFromGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                FirebaseUser firebaseUser = task.getResult().getUser();

            }else{
                Toast.makeText(SignIn.this, "Successfully Authenticated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class GetGoogleSignInDetails extends ActivityResultContract<Integer, Intent>{
        @NonNull
        @Override
        public  Intent createIntent(@NonNull Context context, Integer input){
            return googleSignInClient.getSignInIntent();
        }
        @Override
        public  Intent parseResult(int resultCode, @Nullable Intent intent){
            if(resultCode!= Activity.RESULT_OK || intent == null){
                return null;
            }
            return intent;
        }
    }



}