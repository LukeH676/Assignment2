package ca.on.sl.comp208.dbdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;

/**
 * Created by COMP208 on 2/7/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 23;
    public static final String DB_NAME = "schedule.db";


    private final String CREATE_COURSE_TABLE =
            "CREATE TABLE " + ScheduleContract.Course.COURSE_TABLE + "(" +
                    ScheduleContract.Course._ID + " INTEGER PRIMARY KEY," +
                    ScheduleContract.Course.COURSE_CODE + " TEXT," +
                    ScheduleContract.Course.COURSE_TITLE + " TEXT )";


    private final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE " + ScheduleContract.Schedule.SCHEDULE_TABLE
            + "(" + ScheduleContract.Schedule._ID + " INTEGER PRIMARY KEY,"
            +       ScheduleContract.Schedule.COURSE_ID + " INTEGER, "
            +       ScheduleContract.Schedule.ROOM_ID + " INTEGER, "
            +       ScheduleContract.Schedule.DAY + " TEXT, "
            +       ScheduleContract.Schedule.TIME + " TEXT,"
            +       "FOREIGN KEY(" + ScheduleContract.Schedule.COURSE_ID + ")"
            +       "REFERENCES " + ScheduleContract.Course.COURSE_TABLE
            +       "(" + ScheduleContract.Course._ID + ")"
            +   ")";

    private final String CREATE_ROOM_TABLE =
            "CREATE TABLE " + ScheduleContract.Room.ROOM_TABLE
                    + "(" + ScheduleContract.Room._ID + " INTEGER PRIMARY KEY,"
                    +       ScheduleContract.Room.ROOM_ID + " INTEGER, "
                    +       ScheduleContract.Room.SIZE + " INTEGER, "
                    +       ScheduleContract.Room.TYPE + " TEXT,"
                    +       ScheduleContract.Room.ROOM_NUM + " TEXT, "
                    +       "FOREIGN KEY(" + ScheduleContract.Room.ROOM_ID + ")"
                    +       "REFERENCES " + ScheduleContract.Schedule.SCHEDULE_TABLE
                    +       "(" + ScheduleContract.Schedule.ROOM_ID + ")"
                    +   ")";

    private final String DELETE_SCHEDULE_TABLE =
            "DROP TABLE IF EXISTS " + ScheduleContract.Schedule.SCHEDULE_TABLE;

    private final String DELETE_COURSE_TABLE =
            "DROP TABLE IF EXISTS " + ScheduleContract.Course.COURSE_TABLE;

    private final String DELETE_ROOM_TABLE =
            "DROP TABLE IF EXISTS " + ScheduleContract.Room.ROOM_TABLE;


    Context context;
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
        db.execSQL(CREATE_ROOM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_SCHEDULE_TABLE);
        db.execSQL(DELETE_COURSE_TABLE);
        db.execSQL(DELETE_ROOM_TABLE);
        onCreate(db);
    }

    /**
     *
     * @param code      Course code
     * @param title     Course title
     * @param db        Database
     */
    public void addCourse(String code, String title, SQLiteDatabase db)
    {
        ContentValues course = new ContentValues();
        course.put(ScheduleContract.Course.COURSE_CODE,code);
        course.put(ScheduleContract.Course.COURSE_TITLE,title);
        db.insert(ScheduleContract.Course.COURSE_TABLE,null,course);
    }
    public void deleteAllCourses(SQLiteDatabase db)
    {
        db.execSQL ("DELETE FROM " + ScheduleContract.Room.ROOM_TABLE);
        db.execSQL ("DELETE FROM " + ScheduleContract.Schedule.SCHEDULE_TABLE);
        db.execSQL ("DELETE FROM " + ScheduleContract.Course.COURSE_TABLE);

    }

    public Cursor getCourses(SQLiteDatabase db)
    {
        String table = ScheduleContract.Course.COURSE_TABLE;
        String[] projection = {
                ScheduleContract.Course._ID,
                ScheduleContract.Course.COURSE_CODE,
                ScheduleContract.Course.COURSE_TITLE
        };
        return db.query(table,projection,null,null,ScheduleContract.Course._ID,null,null);
    }
    public Cursor getSchedule(SQLiteDatabase db)
    {
        String table = ScheduleContract.Schedule.SCHEDULE_TABLE;
        String[] projection = {
                ScheduleContract.Course.COURSE_CODE,
                ScheduleContract.Schedule.DAY,
                ScheduleContract.Schedule.TIME,
                ScheduleContract.Room.ROOM_NUM

        };

        return db.query(table + " INNER JOIN room ON " + "schedule."+ ScheduleContract.Schedule.ROOM_ID + " = " +
                        "room."+ScheduleContract.Room._ID + " INNER JOIN course ON " + "course." + ScheduleContract.Course._ID + " = " + "schedule." + ScheduleContract.Schedule.COURSE_ID,
                         projection,null,null,null,null,null);
    }
    public Cursor getRoom(SQLiteDatabase db)
    {
        String table = ScheduleContract.Room.ROOM_TABLE;
        String[] projection = {
                ScheduleContract.Room._ID,
                ScheduleContract.Room.SIZE,
                ScheduleContract.Room.TYPE,
                ScheduleContract.Room.ROOM_NUM
        };
        return db.query(table,projection,null,null,ScheduleContract.Room._ID,null,null);
    }

    /**
     * @param code Course title
     * @param time  Scheduled time
     * @param day   day of week
     * @param db    Database
     */

    public void addScheduleItem(
            String code,
            String day,
            String time,
            String roomNum,
            SQLiteDatabase db)
    {
        String[] courseColumns = {ScheduleContract.Course._ID};
        String[] roomColumns = {ScheduleContract.Room._ID};

        String courseWhereClause = ScheduleContract.Course.COURSE_CODE + " = ?";
        String[] courseWhereArgs = new String[] {
                code
        };
        String roomWhereClause = ScheduleContract.Room.ROOM_NUM + " = ?";
        String[] roomWhereArgs = new String[] {
                roomNum
        };

        Cursor courseCursor = db.query(ScheduleContract.Course.COURSE_TABLE, courseColumns, courseWhereClause, courseWhereArgs,null, null, null);
        Cursor roomCursor = db.query(ScheduleContract.Room.ROOM_TABLE, roomColumns, roomWhereClause, roomWhereArgs,null, null, null);

        int courseCount = courseCursor.getCount();
        int roomCount = roomCursor.getCount();
        if (courseCount > 0 && roomCount > 0) {
            courseCursor.moveToFirst();
            String courseId = courseCursor.getString(0);
            roomCursor.moveToFirst();
            String roomId = roomCursor.getString(0);

            ContentValues values = new ContentValues();
            values.put(ScheduleContract.Schedule.DAY, day);
            values.put(ScheduleContract.Schedule.TIME,time);
            values.put(ScheduleContract.Schedule.COURSE_ID, courseId);
            values.put(ScheduleContract.Schedule.ROOM_ID, roomId);
            db.insert(ScheduleContract.Schedule.SCHEDULE_TABLE,null,values);
        }
    }
    public void addRoomItem(
            String roomNum,
            String size,
            String type,
            SQLiteDatabase db)
    {
        String table = ScheduleContract.Course.COURSE_TABLE;
        String[] columns = {ScheduleContract.Course._ID};

        ContentValues values = new ContentValues();
        values.put(ScheduleContract.Room.ROOM_NUM,roomNum);
        values.put(ScheduleContract.Room.SIZE, size);
        values.put(ScheduleContract.Room.TYPE,type);
        db.insert(ScheduleContract.Room.ROOM_TABLE,null,values);
    }
}
