package br.ufop.workspace.trabalho.controleacademico;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.net.URI;

/**
 * Classe para gerar notificação no Smartphone
 */
public class Notificacao extends AppCompatActivity {
    public static NotificationCompat.Builder mBuilder;
    public static NotificationManager mNotificationManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotificationManager = (NotificationManager)
                getSystemService(NotificationManager.class);

        //Setup a notification channel
        String id = "channel_id";
        CharSequence name = "channel_name";
        String description = "channel_description";

        int importance = NotificationManager.IMPORTANCE_HIGH;

        if(Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new
                    NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            mNotificationManager.
                    createNotificationChannel(mChannel);
        }

        //Intent that is gonna be executed when
        // the user clicks on the notification
        Intent it = new Intent(this,
                TelaTarefas.class);
        it.putExtra("notificacao",1);
        PendingIntent p = PendingIntent.
                getActivity(this, 0, it, 0);

        //Creates the notification
        mBuilder = new
                NotificationCompat.Builder(
                this,id);
        mBuilder.setSmallIcon(R.mipmap.logo_app1);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo_app_round));
        mBuilder.setTicker("Tarefa próxima");
        mBuilder.setContentTitle("Atenção tarefa próxima!");
        mBuilder.setContentText("Você possui uma tarefa marcada para amanhã.");
        mBuilder.setChannelId(id);
        mBuilder.setShowWhen(false);
        mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        mBuilder.setContentIntent(p); //Intent that is going to be launched when the user clicks on the notification
        //Notification n=mBuilder.build();
        //n.vibrate=new long[]{150,300,150,600};
        mNotificationManager.notify(0,
                mBuilder.build());

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }catch(Exception e){

        }

        finish();
    }
}
