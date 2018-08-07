package com.vietdung.note.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.vietdung.note.R;
import com.vietdung.note.adapter.NoteAdapter;
import com.vietdung.note.database.DBNote;
import com.vietdung.note.model.Note;
import com.vietdung.note.util.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UpdateNoteActivity extends AppCompatActivity {
    Toolbar tb_Note;
    TextView tv_Note_Time, tv_Alarm;
    EditText et_Note, et_Note_Title;
    ImageView iv_Image, iv_X;
    Spinner spinner_Date, spinner_Time;
    ArrayList<String> dateArrayList;
    ArrayList<String> timeArrayList;
    BottomNavigationView menuBottom;
    List<Note> noteList;

    Calendar calendar;
    DBNote dbNote;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    String date;
    String time;
    String uri;
    String nowtime;
    String notetitle;
    String mnote;
    int id;
    int position;
    int year, month, day, hour, minute;
    private int Request_Code_Choose_Photo = 123;

    Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        initView();
        addEvents();
    }
    private void addEvents() {
        getData();
        setToolbar();
        setTimeCreate();
        setSpinerDate();
        setSpinerTime();
        setClick();

    }



    private void getData() {
        Intent i = getIntent();
        id = i.getIntExtra(NoteAdapter.rq_itent_id, 1);
        position =i.getIntExtra(NoteAdapter.rq_itent_position,0);
        note = dbNote.getNoteFromId(id);
        Log.d("id cua intent",""+id);
        loadData();
    }
    private void loadData() {
        tv_Note_Time.setText(note.getDateCreate());
        et_Note_Title.setText(note.getTitle());
        et_Note.setText(note.getNote());
        uri = note.getUri();
        date = note.getDate();
        time = note.getTime();


        if (note!=null && !uri.isEmpty()) {
            iv_Image.setImageURI(Uri.parse(uri));
            Log.d("uri", "getData: " + uri);
            setImage();
            loadData();
        }
    }

    private void setClick() {
        tv_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_Alarm.setVisibility(View.INVISIBLE);
                spinner_Date.setVisibility(View.VISIBLE);
                spinner_Time.setVisibility(View.VISIBLE);
                iv_X.setVisibility(View.VISIBLE);
            }
        });
        iv_X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_Alarm.setVisibility(View.VISIBLE);
                spinner_Date.setVisibility(View.INVISIBLE);
                spinner_Time.setVisibility(View.INVISIBLE);
                iv_X.setVisibility(View.INVISIBLE);
            }
        });
        menuBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnDelete:
                        dbNote.delelteNote(id);
                        Intent i = new Intent(UpdateNoteActivity.this,MainActivity.class);
                        startActivity(i);
                        break;
                    case R.id.mnNext:
                        if(position<noteList.size()-1){
                            position =position+1;
                            note = noteList.get(position);
                            loadData();
                        }
                        break;
                    case R.id.mnBack:
                        if(position>0){
                            position =position-1;
                            note = noteList.get(position);
                            loadData();
                        }
                        break;

                }
                return false;
            }
        });
    }

    private void setSpinerTime() {
        timeArrayList.add("09:00");
        timeArrayList.add("13:00");
        timeArrayList.add("17:00");
        timeArrayList.add("20:00");
        timeArrayList.add(time);
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeArrayList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Time.setAdapter(timeAdapter);
        if(time.equals("09:00")){
            spinner_Time.setSelection(0);
        }else if(time.equals("13:00")){
            spinner_Time.setSelection(1);
        }else if(time.equals("17:00")){
            spinner_Time.setSelection(2);
        }else if(time.equals("20:00")){
            spinner_Time.setSelection(3);
        }else{
            spinner_Time.setSelection(4);
        }

        spinner_Time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String seletedItem = adapterView.getItemAtPosition(i).toString();
                if (seletedItem.equals("09:00")) {
                    time = "09:00";
                    Log.d("9time", time);
                    String other = adapterView.getItemAtPosition(4).toString();
                    setOther(other);

                } else if (seletedItem.equals("13:00")) {
                    time = "13:00";
                    Log.d("13time", time);
                    String other = adapterView.getItemAtPosition(4).toString();
                    setOther(other);

                } else if (seletedItem.equals("17:00")) {
                    time = "17:00";
                    Log.d("17time", time);
                    String other = adapterView.getItemAtPosition(4).toString();
                    setOther(other);

                } else if (seletedItem.equals("20:00")) {
                    time = "20:00";
                    Log.d("20time", time);
                    String other = adapterView.getItemAtPosition(4).toString();
                    setOther(other);
                }else if (seletedItem.equals("Other...")) {
                    ChooseTime();
                }

            }

            private void setOther(String other) {
                if (!other.equals("Other...")) {
                    int position = spinner_Time.getSelectedItemPosition();
                    Log.d("VI tri",""+position);
                    timeAdapter.remove(timeArrayList.get(4));
                    timeAdapter.insert("Other...", 4);
                    timeAdapter.notifyDataSetChanged();
                    spinner_Time.setAdapter(timeAdapter);
                    spinner_Time.setSelection(position);
                }
            }

            private void ChooseTime() {
                final Calendar calendar = Calendar.getInstance();
                int hour1 = calendar.get(Calendar.HOUR_OF_DAY);
                int minute1 = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        int position = spinner_Time.getSelectedItemPosition();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendar.set(0, 0, 0, i, i1);
                        hour = i;
                        minute = i1;
                        time = simpleDateFormat.format(calendar.getTime());
                        Log.d("timechoose", time);
                        timeAdapter.remove(timeArrayList.get(position));
                        timeAdapter.insert(time, position);
                        timeAdapter.notifyDataSetChanged();
                        spinner_Time.setAdapter(timeAdapter);
                        spinner_Time.setSelection(position);

                    }
                }, hour1, minute1, true);
                timePickerDialog.show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setSpinerDate() {
        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 7);
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        final String nextday = "Next " + df.format(calendar.getTime());
        final String other = "Other...";
        dateArrayList.add("Today");
        dateArrayList.add("Tommorow");
        dateArrayList.add(nextday);
        dateArrayList.add(date);

        final ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateArrayList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Date.setAdapter(dateAdapter);
        spinner_Date.setSelection(3);
        spinner_Date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String seletedItem = adapterView.getItemAtPosition(i).toString();
                if (seletedItem.equals("Today")) {
                    calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    date = df.format(calendar.getTime());
                    Log.d("date1:", "" + date);
                    String select = adapterView.getItemAtPosition(3).toString();
                    setOther(select);
                } else if (seletedItem.equals("Tommorow")) {

                    getDay(1);
                    String select = adapterView.getItemAtPosition(3).toString();
                    setOther(select);

                } else if (seletedItem.equals(nextday)) {
                    getDay(7);
                    String select = adapterView.getItemAtPosition(3).toString();
                    setOther(select);

                } else if (seletedItem.equals((other))) {
                    Log.d("date3:", "" + date);
                    ChooseDate();

                }
            }

            private void getDay(int nextday) {
                calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, nextday);
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                date = df.format(calendar.getTime());
            }

            private void setOther(String select) {
                if (!select.equals("Other...")) {
                    int position = spinner_Date.getSelectedItemPosition();
                    dateAdapter.remove(dateArrayList.get(3));
                    dateAdapter.insert("Other...", 3);
                    dateAdapter.notifyDataSetChanged();
                    spinner_Date.setAdapter(dateAdapter);
                    spinner_Date.setSelection(position);
                }
            }

            private void ChooseDate() {
                final Calendar calendar = Calendar.getInstance();
                final int year1 = calendar.get(Calendar.YEAR);
                int month1 = calendar.get(Calendar.MONTH);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int position = spinner_Date.getSelectedItemPosition();
                        calendar.set(i, i1, i2);
                        year = i;
                        month = i1;
                        day = i2;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        date = simpleDateFormat.format(calendar.getTime());
                        dateAdapter.remove(dateArrayList.get(position));
                        dateAdapter.insert(date, position);
                        dateAdapter.notifyDataSetChanged();
                        spinner_Date.setAdapter(dateAdapter);
                        spinner_Date.setSelection(position);
                    }
                }, year1, month1, day1);
                datePickerDialog.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setTimeCreate() {
        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(calendar.getTime());
        tv_Note_Time.setText(formattedDate);
    }

    private void setToolbar() {
        setSupportActionBar(tb_Note);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnSaveUpdate:
                setTimeCreate();
                nowtime = tv_Note_Time.getText().toString().trim();
                notetitle = et_Note_Title.getText().toString();
                mnote = et_Note.getText().toString();
                if (notetitle.isEmpty() && mnote.isEmpty() && uri.isEmpty()) {
                    Intent intent = new Intent(UpdateNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Note note = new Note();
                    note.setId(id);
                    note.setDateCreate(nowtime);
                    note.setDate(date);
                    note.setTime(time);
                    note.setNote(mnote);
                    note.setTitle(notetitle);
                    note.setUri(uri);
                    dbNote.update(note);

                    if (!spinner_Date.getSelectedItem().equals(day) && !spinner_Time.getSelectedItem().equals(time)){
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        // Cancel pending intent
                        Intent intenta = new Intent(NoteActivity.getAppContext(), AlarmReceiver.class);
                        pendingIntent = PendingIntent.getBroadcast(NoteActivity.getAppContext(), id, intenta, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        //pendingIntent.cancel();

                        // Create new pending intent
                        intenta = new Intent(UpdateNoteActivity.this, AlarmReceiver.class);
                        intenta.putExtra(NoteActivity.Request_Code_Intent,id);
                        pendingIntent = PendingIntent.getBroadcast(UpdateNoteActivity.this, id, intenta, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }

                    Intent intent = new Intent(UpdateNoteActivity.this, MainActivity.class);
                    startActivity(intent);


                }
                break;
            case R.id.mnChooseUpdate:
                break;
            case R.id.mnCameraUpdate:
                showDialogPhoto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogPhoto() {
        final Dialog dialog = new Dialog(UpdateNoteActivity.this);
        dialog.setTitle(R.string.insertPicture);
        dialog.setContentView(R.layout.dialog_photograph);
        dialog.setCanceledOnTouchOutside(false);

        TextView tv_take = dialog.findViewById(R.id.tvTakePhoto);
        TextView tv_choose = dialog.findViewById(R.id.tvChoose);

        tv_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose photo"), Request_Code_Choose_Photo);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Code_Choose_Photo) {
            if (resultCode == Activity.RESULT_OK) {
                uri = getRealPath(this, data.getData());

                iv_Image.setImageURI(data.getData());
                Log.d("URi dep troi`", "" + uri);
                iv_Image.setVisibility(View.VISIBLE);
                iv_Image.getLayoutParams().height = 300;
                iv_Image.getLayoutParams().width = 300;
                iv_Image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv_Image.setScaleType(ImageView.ScaleType.FIT_XY);
                iv_Image.requestLayout();

            }
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPath(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    private void setImage() {
        iv_Image.setVisibility(View.VISIBLE);
        iv_Image.getLayoutParams().height = 300;
        iv_Image.getLayoutParams().width = 300;
        iv_Image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv_Image.setScaleType(ImageView.ScaleType.FIT_XY);
        iv_Image.requestLayout();
    }

    private void initView() {
        tv_Alarm = findViewById(R.id.tvAlarmUpdate);
        tb_Note = findViewById(R.id.tbNoteUpdate);
        et_Note = findViewById(R.id.etNoteUpdate);
        tv_Note_Time = findViewById(R.id.tvNoteTimeUpdate);
        et_Note_Title = findViewById(R.id.etNoteTitleUpdate);
        iv_Image = findViewById(R.id.ivImageUpdate);
        menuBottom = findViewById(R.id.bottomNavigation);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        spinner_Date = findViewById(R.id.spinerDateUpdate);
        spinner_Time = findViewById(R.id.spinerTimeUpdate);
        iv_X = findViewById(R.id.ivXUpdate);
        spinner_Date.setVisibility(View.INVISIBLE);
        spinner_Time.setVisibility(View.INVISIBLE);
        iv_X.setVisibility(View.INVISIBLE);
        dateArrayList = new ArrayList<>();
        timeArrayList = new ArrayList<>();
        dbNote = new DBNote(getApplicationContext());
        noteList = dbNote.noteList();

    }
}

