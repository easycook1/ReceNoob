package com.example.recenoob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText regName;
    private EditText regcorreo;
    private EditText regpass;
    private Button botonreg;
    private Button botonini;
    private Button botongoogle;

    // variables datos

    private String nombre = "";
    private String correo = "";
    private String contra = "";
    private String userRegular = "R";
    private String fechaCreacion = "01/12/2020";
    private String fechaIniSusc = "01/15/2020";
    private String fechaFinSusc = "02/15/2020";
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;



    FirebaseAuth mAuth;
    DatabaseReference mdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference();



        regName = findViewById(R.id.nombrereg);
        regcorreo = findViewById(R.id.correoreg);
        regpass = findViewById(R.id.passreg);
        botonreg = findViewById(R.id.btnreg);
        botonini = findViewById(R.id.blogin);
        botongoogle = findViewById(R.id.btngoogle);

//codigo login google

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]




        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        botongoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
//codigo login google -fin

        botonini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,loginActivity.class));
            }
        });

        botonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = regName.getText().toString();
                correo = regcorreo.getText().toString();
                contra = regpass.getText().toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !contra.isEmpty()){

                    if(contra.length() > 6){
                        registrarusu();
                    }else{
                        Toast.makeText(MainActivity.this,"El password debe ser mauyor a 6 caracteres",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(MainActivity.this,"Debe completar los campos",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    //codigo login google

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("loginG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("logonGid", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("loginG1", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this,inicioActivity1.class));
                            finish();

                            regName.setText(user.getDisplayName());
                            regcorreo.setText(user.getEmail());


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login2", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this,"No se pudo logear con google",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    //codigo login google - fin

    private void registrarusu(){


     //   Log.d("nombre", "nombre: "+nombre);


        mAuth.createUserWithEmailAndPassword(correo,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("name",nombre);
                    map.put("email",correo);
                    map.put("password",contra);

                    map.put("tipoUsuario",userRegular);
                    map.put("fechaCreacion", fechaCreacion);
                    map.put("FechaIniSuscri",fechaIniSusc);
                    map.put("FechaFinSuscrip",fechaFinSusc);

                    String id = mAuth.getCurrentUser().getUid();
                    mdatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(MainActivity.this,inicioActivity1.class));
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this,"Erros crear datos",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Log.e("error", "onComplete: Failed=" + task.getException().getMessage());
                    Toast.makeText(MainActivity.this,"Erros db",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,inicioActivity1.class));
            finish();
        }
    }
}
