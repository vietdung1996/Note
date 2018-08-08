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
import com.vietdung.note.database.DBNote;
import com.vietdung.note.model.Note;
import com.vietdung.note.util.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NoteActivity extends AppCompatActivity {
    Toolbar tb_Note;
    TextView tv_Note_Time, tv_Alarm;
    EditText et_Note, et_Note_Title;
    ImageView ivImage, iv_X;
    Spinner spinner_Date, spinner_Time;
    ArrayList<String> dateArrayList;
    ArrayList<String> timeArrayList;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    Calendar calendar;
    DBNote dbNote;


    public String date;
    public String time;
    public String nowtime;
    public String mnote = "";
    public String notetitle = "";
    public String uri = "";
    int year, month, day, hour, minute;
    private static Context context;
    private int Request_Code_Choose_Photo = 123;
    public static final String  Request_Code_Intent= "678";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initView();
        addEvents();
    }

    private void addEvents() {
        setToolbar();
        setClick();
        setTimeCreate();
        setSpinerDate();
        setSpinerTime();
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
    }

    private void setSpinerTime() {
        timeArrayList.add("09:00");
        timeArrayList.add("13:00");
        timeArrayList.add("17:00");
        timeArrayList.add("20:00");
        timeArrayList.add("Other...");
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeArrayList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Time.setAdapter(timeAdapter);
        spinner_Time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String seletedItem = adapterView.getItemAtPosition(i).toString();
                if (seletedItem.equals("Other...")) {
                    ChooseTime();
                } else if (seletedItem.equals("09:00")) {
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
                }

            }

            private void setOther(String other) {
                if (!other.equals("Other...")) {
                    int position = spinner_Time.getSelectedItemPosition();
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(NoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        int position = spinner_Time.getSelectedItemPosition();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        calendar.set(0, 0, 0, i, i1);
                        hour = i;
                        minute=i1;
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
        dateArrayList.add(other);

        final ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateArrayList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Date.setAdapter(dateAdapter);
        spinner_Date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String seletedItem = adapterView.getItemAtPosition(i).toString();
                if (seletedItem.equals("Today")) {
                    calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    date = df.format(calendar.getTime());
                    df = new SimpleDateFormat("dd");
                    day = Integer.parseInt(df.format(calendar.getTime()));
                    df = new SimpleDateFormat("MM");
                    month = Integer.parseInt(df.format(calendar.getTime()));
                    df = new SimpleDateFormat("yyyy");
                    year =Integer.parseInt(df.format(calendar.getTime()));
                    Log.d("day and month", ""+day+month);


                    String select = adapterView.getItemAtPosition(3).toString();
                    setOther(select);
                } else if (seletedItem.equals("Tommorow")) {

                    getDay(1);
                    String select = adapterView.getItemAtPosition(3).toString();
                    setOther(select);

                } else if (seletedItem.equals(nextday)) {
//                    calendar = new GregorianCalendar();
//                    calendar.add(Calendar.DATE, 7);
//                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                    date = df.format(calendar.getTime());
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
                int year1 = calendar.get(Calendar.YEAR);
                final int month1 = calendar.get(Calendar.MONTH);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int position = spinner_Date.getSelectedItemPosition();
                        calendar.set(i, i1, i2);
                        year  = i;
                        month =i1;
                        day =i2;
                        Log.d("day and month", ""+day+" "+month+" "+year);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tb_Note.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnSave:
                setTimeCreate();
                nowtime = tv_Note_Time.getText().toString().trim();
                notetitle = et_Note_Title.getText().toString();
                mnote = et_Note.getText().toString();
                if (notetitle.isEmpty() && mnote.isEmpty() && uri.isEmpty()) {
                    Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Note note = new Note();
                    note.setDateCreate(nowtime);
                    note.setDate(date);
                    note.setTime(time);
                    note.setNote(mnote);
                    note.setTitle(notetitle);
                    note.setUri(uri);
                    dbNote.addNote(note);
                    Note a =dbNote.getNoteFromDateCreat();
                    int id = a.getId();

                                     // int requestCode = Integer.parseInt(nowtime);
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,day);
                    calendar.set(Calendar.HOUR_OF_DAY,hour);
                    calendar.set(Calendar.MINUTE,minute);

                    Intent intenta = new Intent(NoteActivity.this, AlarmReceiver.class);
                    intenta.putExtra(Request_Code_Intent,id);
                    pendingIntent = PendingIntent.getBroadcast(NoteActivity.this,id , intenta, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.mnChoose:
                break;
            case R.id.mnCamera:
                showDialogPhoto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogPhoto() {
        final Dialog dialog = new Dialog(NoteActivity.this);
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

                ivImage.setImageURI(data.getData());
                Log.d("URi dep troi`", "" + uri);
                ivImage.setVisibility(View.VISIBLE);
                ivImage.getLayoutParams().height = 300;
                ivImage.getLayoutParams().width = 300;
                ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
                ivImage.requestLayout();


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



    public static Context getAppContext(){
        return NoteActivity.context;
    }


    private void initView() {
        tv_Alarm = findViewById(R.id.tvAlarm);
        tb_Note = findViewById(R.id.tbNote);
        et_Note = findViewById(R.id.etNote);
        tv_Note_Time = findViewById(R.id.tvNoteTime);
        et_Note_Title = findViewById(R.id.etNoteTitle);
        ivImage = findViewById(R.id.ivImage);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        spinner_Date = findViewById(R.id.etDate);
        spinner_Time = findViewById(R.id.etTime);
        iv_X = findViewById(R.id.ivX);
        spinner_Date.setVisibility(View.INVISIBLE);
        spinner_Time.setVisibility(View.INVISIBLE);
        iv_X.setVisibility(View.INVISIBLE);
        dateArrayList = new ArrayList<>();
        timeArrayList = new ArrayList<>();
        NoteActivity.context = getApplicationContext();

        dbNote = new DBNote(getApplicationContext());


    }
}
