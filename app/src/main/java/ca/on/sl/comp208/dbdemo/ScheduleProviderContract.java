package ca.on.sl.comp208.dbdemo;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shogo on 2018-03-22.
 */

public class ScheduleProviderContract {

    public static final String AUTHORITY = "ca.on.sl.COMP208.ScheduleProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final class Schedule implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ScheduleProviderContract.CONTENT_URI, "Schedule");
        public static final String COURSE_ID = "course_ID";
        public static final String ROOM_ID = "room_ID";
        public static final String TIME = "time";
        public static final String DAY = "day";

        public static final String[] ALL_COLUMNS =
                {_ID, COURSE_ID, ROOM_ID, TIME, DAY};
    }

    public static final class Room implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ScheduleProviderContract.CONTENT_URI, "Room");
        public static final String ROOM_ID          = "room_ID";
        public static final String ROOM_NUM         = "roomNum";
        public static final String SIZE             = "size";
        public static final String TYPE             = "type";

        public static final String[] ALL_COLUMNS =
                {_ID, ROOM_ID, ROOM_NUM, SIZE,TYPE};


    }

    public static final class Course implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ScheduleProviderContract.CONTENT_URI, "Course");
        public static final String COURSE_CODE  = "course_code";
        public static final String COURSE_TITLE = "course_title";

        public static final String[] ALL_COLUMNS =
                {_ID, COURSE_CODE, COURSE_TITLE};


    }

}
