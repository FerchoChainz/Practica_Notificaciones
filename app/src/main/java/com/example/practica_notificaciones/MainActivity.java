package com.example.practica_notificaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            //sendPushNotification();
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

    /*private void sendPushNotification(){
        RequestQueue myrequest= Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            String token = "fAZckT1eS4K6sG1yr6R_P2:APA91bGhkGj-zL08mg7kVPv1HgPW8sAs1lqeuGNStCkn4bvh6IT9VzSP0V-9mhvXu9U2bl3i3i4xIWduCYEq6sAcXcxIaygVU29dZHx9nqvEJqWBgW1ld41luRiylrjkHQp6gOm-DReR";
            json.put("to",token);
            JSONObject notification = new JSONObject();
            notification.put("titulo","Login");
            notification.put("detalle","Usuario logeado");
            json.put("data",notification);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","AAAAJmzS1vc:APA91bGyVzxv6iZLuvAsamhQUMpbkjl4DR3MldMjtB-YDSq9rcryo5PG1M8yUFBDHZ44XvbT0AHijS64eT9KygB5jt-xLxbNEr8U1v9gCi7eDBbL2lEinaH42guFMtXGwotxdlEH9JHb");
                    return  header;
                }
            };
            myrequest.add(request);
        }catch (JSONException e){
        }
    }*/
}