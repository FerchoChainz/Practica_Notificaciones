package com.example.practica_notificaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormularioActivity extends AppCompatActivity {

    public final static int NOTIFICACION_ID = 0;

    //identificador de alarma
    private int idAlarma = 1;
    //valores para la alarma
    private int diaAlarma = 0;
    private int horAlarma = 0;
    private int minutoAlarma = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICACION_ID);

        FirebaseMessaging.getInstance().subscribeToTopic("Enviar a todos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FormularioActivity.this, "Suscrito al enviar a todos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void registrarCita(View view){
        Toast.makeText(this, "Fecha de cita: " + diaAlarma, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Horario de cita" + horAlarma, Toast.LENGTH_SHORT).show();
        //establecer la alarma
        programarAlarmaEnSistema("Cita con nutriologo" ,horAlarma,minutoAlarma);
        sendPushNotification();
    }

    public void setFechaAlarma(View view){
        //instancia para calnedario
        Calendar horarioHoy = Calendar.getInstance();

        //obtener los valores actuales del sistema
        int anioActual = horarioHoy.get(Calendar.YEAR);
        int mesActual = horarioHoy.get(Calendar.MONTH);
        int diaActual = horarioHoy.get(Calendar.DAY_OF_MONTH);

        //fecha para elegir la cita
        DatePickerDialog datePickerDialog = new DatePickerDialog(FormularioActivity.this, new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        diaAlarma = i2;
                    }
                },anioActual,mesActual,diaActual);
        datePickerDialog.setTitle("Fecha de la cita");
        datePickerDialog.show();
    }//setfechaAlarma


    public void setHorarioAlarma(View view){
        //Instancia para calendario
        Calendar horarioHoy = Calendar.getInstance();
        horarioHoy.setTimeInMillis(System.currentTimeMillis());

        //Obtener los valores actuales del sistema
        int horaActual = horarioHoy.get(Calendar.HOUR_OF_DAY);
        int minutoActual = horarioHoy.get(Calendar.MINUTE);

        //Horario para elegir la cita
        TimePickerDialog timePickerDialog = new TimePickerDialog(FormularioActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                horAlarma = i;
                minutoAlarma = i1;
            }
        },horaActual, minutoActual,true);
        //definir titulo
        timePickerDialog.setTitle("Horario de cita.");
        timePickerDialog.show();
    }//setHorarioAlarma

    public void programarAlarmaEnSistema(String mensaje, int hora, int minuto){
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, mensaje)
                .putExtra(AlarmClock.EXTRA_HOUR, hora)
                .putExtra(AlarmClock.EXTRA_MINUTES, minuto);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }//alarmaEnSistema

    private void sendPushNotification(){
        RequestQueue myrequest= Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try {
            String token = "fAZckT1eS4K6sG1yr6R_P2:APA91bGhkGj-zL08mg7kVPv1HgPW8sAs1lqeuGNStCkn4bvh6IT9VzSP0V-9mhvXu9U2bl3i3i4xIWduCYEq6sAcXcxIaygVU29dZHx9nqvEJqWBgW1ld41luRiylrjkHQp6gOm-DReR";
            json.put("to",token);
            JSONObject notification = new JSONObject();
            notification.put("titulo","Alarma");
            notification.put("detalle","Alarma Registrada.");
            json.put("data",notification);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAJmzS1vc:APA91bGyVzxv6iZLuvAsamhQUMpbkjl4DR3MldMjtB-YDSq9rcryo5PG1M8yUFBDHZ44XvbT0AHijS64eT9KygB5jt-xLxbNEr8U1v9gCi7eDBbL2lEinaH42guFMtXGwotxdlEH9JHb");
                    return  header;
                }
            };
            myrequest.add(request);
        }catch (JSONException e){
        }
    }
}