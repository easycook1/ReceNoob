package com.example.recenoob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button   mBotonlog;
    private Button   mBotonreset;

    private String email;
    private String pass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.correoini);
        mPass = findViewById(R.id.passini);
        mBotonlog = findViewById(R.id.btnlogin);
        mBotonreset = findViewById(R.id.btnresetpass1);

        mBotonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                pass = mPass.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()){
                    loginuser();
                }else{

                    Toast.makeText(loginActivity.this,"Debe completar los campos",Toast.LENGTH_SHORT).show();
                }

            }
        });


        mBotonreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,ResetPassActivity.class));
            }
        });
    }


    private void loginuser(){

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    startActivity(new Intent(loginActivity.this,inicioActivity1.class));
                }else{
                    Toast.makeText(loginActivity.this,"No se pudo iniciar sesion",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
