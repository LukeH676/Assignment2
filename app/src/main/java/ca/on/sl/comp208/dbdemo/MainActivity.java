package ca.on.sl.comp208.dbdemo;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataBase();

        View.OnClickListener cl =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button btn = (Button) view;
                        if (btn.getText().equals("Room")) {
                            showRoom();
                        } else if (btn.getText().equals("Courses")) {
                            showCourses();
                        } else if (btn.getText().equals("Schedule")) {
                            showSchedule();
                        }


                    }
                };
        ((Button) findViewById(R.id.rooms)).setOnClickListener(cl);
        ((Button) findViewById(R.id.course)).setOnClickListener(cl);
        ((Button) findViewById(R.id.schedule)).setOnClickListener(cl);
    }

    void initDataBase() {
        DBHelper helper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.deleteAllCourses(db);
        helper.addCourse("COMP 208", "Android Programming", db);
        helper.addCourse("COMP 74", "Web Services", db);
        helper.addCourse("COMP 35", "CICS COBOL Programming", db);
        helper.addCourse("COMP 62", "Field Placement", db);

        helper.addRoomItem("11410", "50", "Lab", db);
        helper.addRoomItem("12000", "40", "Lecture", db);
        helper.addRoomItem("12040", "40", "Lecture", db);
        helper.addRoomItem("11290", "40", "Lab", db);
        helper.addRoomItem("12050", "30", "Lecture", db);
        helper.addRoomItem("99999", "X", "Field", db);

        helper.addScheduleItem("COMP 208", "Monday", "11:30", "11410", db);
        helper.addScheduleItem("COMP 74", "Monday", "12:30","12040", db);
        helper.addScheduleItem("COMP 208", "Tuesday", "13:30","11410", db);
        helper.addScheduleItem("COMP 74", "Tuesday", "15:30","11410", db);
        helper.addScheduleItem("COMP 208", "Wednesday", "10:30","12040", db);
        helper.addScheduleItem("COMP 35", "Wednesday", "11:30","11290", db);
        helper.addScheduleItem("COMP 35", "Wednesday", "16:30","12050", db);
        helper.addScheduleItem("COMP 62", "Thursday", "8:30","99999", db);
        helper.addScheduleItem("COMP 62", "Thursday", "8:30","99999", db);
    }

    private void showCourses() {
        TextView txtView = (TextView) findViewById(R.id.txtResults);
        txtView.setText("");
        DBHelper helper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCourses(db);
        while (cursor.moveToNext()) {
            String output =
                    String.format("%-10s %s\n", cursor.getString(1), cursor.getString(2));
            txtView.append(output);
        }

    }

    private void showRoom() {
        TextView txtView = (TextView) findViewById(R.id.txtResults);
        txtView.setText("");
        DBHelper helper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getRoom(db);

        while (cursor.moveToNext()) {
            String output =
                    String.format("%-10s %s\n", cursor.getString(1) +"   " + cursor.getString(2),
                            cursor.getString(3));
            txtView.append(output);

        }

    }

    private void showSchedule() {
        TextView txtView = (TextView) findViewById(R.id.txtResults);
        txtView.setText("");

        String[] projection = {
                ScheduleProviderContract.Course.COURSE_CODE,
                ScheduleProviderContract.Schedule.DAY,
                ScheduleProviderContract.Schedule.TIME,
                ScheduleProviderContract.Room.ROOM_NUM

        };

        Uri uri = ScheduleProviderContract.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, projection, null, null,null);

        while (cursor.moveToNext()) {
            String output =
                    String.format("%-5s %s\n", cursor.getString(0) + "   " +cursor.getString(1), cursor.getString(2) + "   " +
                            cursor.getString(3));
            Log.i("String", cursor.getString(1));
            Log.i("String", cursor.getString(2));
            Log.i("String", cursor.getString(3));

            txtView.append(output);
        }

    }

}
