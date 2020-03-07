package com.example.recenoob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    private EditText mEmailR;
    private Button mBotonR;
    private String email = "";
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        mEmailR = findViewById(R.id.emailreset);
        mBotonR = findViewById(R.id.btnresetpass);

        mBotonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = mEmailR.getText().toString();

                if(!email.isEmpty()){
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetpassword();
                }else{
                    Toast.makeText(ResetPassActivity.this,"ingresar correo", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }



    private void resetpassword(){

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResetPassActivity.this,"Se a enviado un mensaje a tu cortreo", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetPassActivity.this,"No se puedo reestablecer la contraseña", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
            }
        });
    }
}
