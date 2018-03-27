package ca.on.sl.comp208.dbdemo;

import android.net.Uri;
import android.provider.BaseColumns;


public class ScheduleContract {
    // Prevent Schedule Contract from being instantiated.

    private ScheduleContract() {
    }

    // Define columns for course table
    // Note: BaseColumns adds an _ID column
    public static class Course implements BaseColumns
    {
        public static final String COURSE_TABLE = "course";
        public static final String COURSE_CODE  = "course_code";
        public static final String COURSE_TITLE = "course_title";
    }

    // Define column names for Schedule Table
    // Note: BaseColumns adds an _ID column
    public static class Schedule implements BaseColumns
    {
        public static final String SCHEDULE_TABLE   = "schedule";
        public static final String COURSE_ID        = "course_ID";
        public static final String ROOM_ID          = "room_ID";
        public static final String TIME             = "time";
        public static final String DAY              = "day";
    }

    public static class Room implements BaseColumns
    {
        public static final String ROOM_TABLE       = "room";
        public static final String ROOM_ID          = "room_ID";
        public static final String ROOM_NUM         = "roomNum";
        public static final String SIZE             = "size";
        public static final String TYPE             = "type";

    }
}
