package com.vietdung.note.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.vietdung.note.activity.NoteActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Ngay dep gioi`", "Hello");
        int id =intent.getIntExtra(NoteActivity.Request_Code_Intent,-1);
        Intent i = new Intent(context,Service.class);
        i.putExtra("abcd",id);
        context.startService(i);
    }
}
