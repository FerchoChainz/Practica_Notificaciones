package com.example.practica_notificaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ImcActivity extends AppCompatActivity {

     private EditText peso, altura;
    private PendingIntent pendingIntentSi;
    private PendingIntent pendingIntentNo;
    private final static String CHANNEL_ID = "NOTIFICACION";
    //Constante para canal de notificacion

    public final static int NOTIFICACION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);


        peso = findViewById(R.id.txtPeso);
        altura = findViewById(R.id.txtAltura);
    }

    public void pesoIdeal(View view){
        float IMC = 0, est = 0, pesoIdeal =0;
        int pes = 0;
        
        //validacion de los campos
        if(peso.getText().toString().isEmpty() || altura.getText().toString().isEmpty()){
            Toast.makeText(this, "Debe llenar todos los campos.", Toast.LENGTH_SHORT).show();
        }else{
            pes = Integer.parseInt(peso.getText().toString());
            est = Float.parseFloat(altura.getText().toString());

            //Realizar calculo
            IMC = pes / (est * est);

            pesoIdeal = IMC * (est * est);
            Toast.makeText(this, "Tu peso ideal es: "
                    + pesoIdeal, Toast.LENGTH_SHORT).show();

            //Validacion de sobrepeso
            if(IMC > 25){
                Toast.makeText(this, "Tienes Sobrepeso", Toast.LENGTH_SHORT).show();
                setPendingIntentSi();
                setPendingIntentNo();
                crearCanalNotificacion();
                crearNotificacion();
            }else{
                Toast.makeText(this, "Tienes Buena Salud", Toast.LENGTH_SHORT).show();
                peso.setText(" ");
                altura.setText(" ");
                peso.requestFocus();
            }
        }
    }

    private void setPendingIntentSi() {
        //crear la instancia del Intent para cambiar de una activity a otra
        Intent intent = new Intent(ImcActivity.this, FormularioActivity.class);
        //Instancia para acceder al historial de navegacion de MainActivity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //Establecer que la relacion de FormularioActivity con MainActivity
        stackBuilder.addParentStack(FormularioActivity.class);
        //agregar el intent para enviar la activity especificada
        stackBuilder.addNextIntent(intent);

        pendingIntentSi = stackBuilder.getPendingIntent(1,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }//setPendingIntentSi

    private void setPendingIntentNo() {
        //crear la instancia del intent para cambiar la activity a otra
        Intent intent = new Intent(ImcActivity.this, activity_info.class);
        //Instancia para acceder al historial de navegacion de MainActivity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //establecer relacion entre infoActivity con MainActivity
        stackBuilder.addParentStack(activity_info.class);
        //Agregar el intent para enviar la activity especificada
        stackBuilder.addNextIntent(intent);
        //relacionar el historial con el objeto con la variable
        pendingIntentNo = stackBuilder.getPendingIntent(1,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }//setPendinIntentNo

    private void crearCanalNotificacion() {
        //validar si es version android superior a O (API >= 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //nombre del canal
            CharSequence name = "Notificacion";

            //Instancia para gestionar el canal y el servicio de la notificacion
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID
                    , name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService((NOTIFICATION_SERVICE));
            notificationManager.createNotificationChannel(notificationChannel);
        }//if
    }//crearCanalNotificacion

    public void crearNotificacion() {
        //Instancia para generar la notificacion, especificando el contexto de la app
        //y el canal de comunicacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID);
        //Caracteristicas a incluir en la notificacion
        builder.setSmallIcon(R.mipmap.health);
        builder.setContentTitle("Servicios de Salud.");
        builder.setContentText("¿Programar cita con nutriologo?");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.RED, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        //especificar la activity que aparece al momento de elegir la notificacion
        builder.setContentIntent(pendingIntentSi);

        //se agregan las opciones que aparecen en la notificacion
        builder.addAction(R.mipmap.notification, "si", pendingIntentSi);
        builder.addAction(R.mipmap.notification, "No", pendingIntentNo);

        //Instancia que gestiona la notificacion con el dispositivo

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());

    }//crearNotificacion

}