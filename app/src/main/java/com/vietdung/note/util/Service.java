package com.vietdung.note.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.vietdung.note.R;
import com.vietdung.note.activity.MainActivity;
import com.vietdung.note.activity.UpdateNoteActivity;
import com.vietdung.note.database.DBNote;
import com.vietdung.note.model.Note;

public class Service extends android.app.Service {
    DBNote dbNote;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbNote = new DBNote(getApplicationContext());
        int id = intent.getIntExtra("abcd",-1);
        Note note = dbNote.getNoteFromId(id);


        Intent notificationIntent = new Intent(this, UpdateNoteActivity.class);
        notificationIntent.putExtra("abc",id);
        notificationIntent
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent
                .getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(note.getTitle())
                        .setContentText(note.getNote())
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());


        return START_NOT_STICKY;
    }
}
