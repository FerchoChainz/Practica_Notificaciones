package com.example.practica_notificaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText correo,passwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = findViewById(R.id.txtCorreo);
        passwd = findViewById(R.id.txtPasswd);
    }//onCreate



    public void logIn(View view){
        if(correo.getText().toString().equals("Admin") && passwd.getText().toString().equals("1234")){
            Intent intent = new Intent(MainActivity.this, ImcActivity.class);
            startActivity(intent);
            finish();
            correo.setText("");
            passwd.setText("");
        } else if (correo.getText().toString().isEmpty() || passwd.getText().toString().isEmpty()) {
            Toast.makeText(this, "Favor de llenar todos los campos.", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}