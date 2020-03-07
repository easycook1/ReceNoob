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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

    // variables datos

    private String nombre = "";
    private String correo = "";
    private String contra = "";
    private String userRegular = "R";
    private String fechaCreacion = "01/12/2020";
    private String fechaIniSusc = "01/15/2020";
    private String fechaFinSusc = "02/15/2020";

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


        //perra

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
