package com.example.practica_notificaciones;

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

import java.util.Calendar;

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
    }


    public void registrarCita(View view){
        Toast.makeText(this, "Fecha de cita: " + diaAlarma, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Horario de cita" + horAlarma, Toast.LENGTH_SHORT).show();
        //establecer la alarma
        programarAlarmaEnSistema("Cita con nutriologo" ,horAlarma,minutoAlarma);
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
}