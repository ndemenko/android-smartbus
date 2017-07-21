package com.test.testgreen.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.testgreen.logger.Logger;
import com.test.testgreen.model.Route;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //--parametrs of the table
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "busesDB";
    private static final String TABLE_BUSES = "Buses";
    //--rows of the table
    private static final String KEY_ID = "_id";
    private static final String KEY_NUMB_ROUTE = "numb";
    private static final String KEY_PRICE = "price";
    private static final String KEY_FROM_DATE = "from_date";
    private static final String KEY_FROM_TIME = "from_time";
    private static final String KEY_FROM_INFO = "from_info";
    private static final String KEY_TO_DATE = "to_date";
    private static final String KEY_TO_TIME = "to_time";
    private static final String KEY_TO_INFO = "to_info";
    private static final String KEY_INFO = "info";
    private static final String KEY_BUS_ID = "bus_id";
    private static final String KEY_RESERVATION_COUNT = "reservation_count";
    private static final String KEY_TO = "go_to";
    private static final String KEY_FROM = "go_from";
    private static final String KEY_ID_TO = "id_to";
    private static final String KEY_ID_FROM = "id_from";
    private static DBHelper instance;
    String s = "create table " + TABLE_BUSES + "(" + KEY_ID + " integer primary key," + KEY_NUMB_ROUTE + " text," +
            KEY_FROM + " text," + KEY_ID_FROM + " text)";
    String id, routeId, price, fromDate, fromTime, fromInfo, toDate, toTime, toInfo,
            info, busId, reservationCount, to, idTo, from, idFrom;//for accessing data
    StringBuilder stringBuilder = new StringBuilder();
    ArrayList<String> queryList = new ArrayList<>();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }//--constructor

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BUSES + "(" + KEY_ID + " integer primary key AUTOINCREMENT,"
                + KEY_NUMB_ROUTE + " text," + KEY_FROM + " text," + KEY_ID_FROM + " text," + KEY_TO + " text,"
                + KEY_ID_TO + " text," + KEY_PRICE + " text," + KEY_FROM_DATE + " text," + KEY_FROM_TIME + " text,"
                + KEY_FROM_INFO + " text," + KEY_TO_DATE + " text," + KEY_TO_TIME + " text," + KEY_TO_INFO + " text,"
                + KEY_INFO + " text," + KEY_BUS_ID + " text," + KEY_RESERVATION_COUNT + " text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table if exists " + TABLE_BUSES);//deleting old database
        onCreate(database);//creating new table
    }

    public void dataParsedSuccessful() {
        System.out.print(stringBuilder.toString());
        this.getWritableDatabase().beginTransaction();
        Logger.d("TAG", "Table is ready!");
        //inserting here
        String string = "SELECT COUNT(*) FROM " + TABLE_BUSES;
        Cursor c = this.getWritableDatabase().rawQuery(string, null);
        for (String itemQuery : queryList) {
            this.getWritableDatabase().execSQL(itemQuery);
        }
        this.getWritableDatabase().setTransactionSuccessful();
        this.getWritableDatabase().endTransaction();
        queryList.clear();
        //counting size of db
        try {
            c.moveToFirst();
            Logger.d("TAG", "count is  = " + c.getString(0));
        } finally {
            c.close();
        }
    }

    public void addRoute(Route route) {
        //getting values for inserting
        routeId = route.getRouteId();
        price = route.getPrice();
        fromDate = route.getFromDate();
        fromTime = route.getFromTime();
        fromInfo = route.getFromInfo();
        toDate = route.getToDate();
        toTime = route.getToTime();
        toInfo = route.getToInfo();
        info = route.getInfo();
        busId = route.getBusId();
        reservationCount = route.getReservationCount();
        //genereting insert query
        queryList.add("INSERT INTO " + TABLE_BUSES + " ( " + KEY_ID + ", " + KEY_NUMB_ROUTE + ", " + KEY_FROM + ", " +
                KEY_ID_FROM + ", " + KEY_TO + ", " + KEY_ID_TO + ", " + KEY_PRICE + ", " + KEY_FROM_DATE + "," + KEY_FROM_TIME +
                ", " + KEY_FROM_INFO + ", " + KEY_TO_DATE + ", " + KEY_TO_TIME + ", " + KEY_TO_INFO + ", " + KEY_INFO + ", " +
                KEY_BUS_ID + ", " + KEY_RESERVATION_COUNT + ") VALUES ( " + id + " , '" + routeId + "' , '" + from + "' , '" + idFrom +
                "' , '" + to + "' , '" + idTo + "' , '" + price + "' , '" + fromDate + "' , '" + fromTime + "' , '" + fromInfo + "' , '" + toDate + "' , '" +
                toTime + "' , '" + toInfo + "' , '" + info + "' , '" + busId + "' , '" + reservationCount + "' );\n");
        //stringBuilder.append("\n");
    }

    public List<Route> getAllRoutes() {
        List<Route> routeList = new ArrayList<Route>();
        String selectQuery = "SELECT  * FROM " + TABLE_BUSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Route route = new Route();
                route.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                route.setRouteId(cursor.getString(cursor.getColumnIndex(KEY_NUMB_ROUTE)));
                route.setPrice(cursor.getString(cursor.getColumnIndex(KEY_PRICE)));
                route.setFromDate(cursor.getString(cursor.getColumnIndex(KEY_FROM_DATE)));
                route.setFromTime(cursor.getString(cursor.getColumnIndex(KEY_FROM_TIME)));
                route.setFromInfo(cursor.getString(cursor.getColumnIndex(KEY_FROM_INFO)));
                route.setToDate(cursor.getString(cursor.getColumnIndex(KEY_TO_DATE)));
                route.setToTime(cursor.getString(cursor.getColumnIndex(KEY_TO_TIME)));
                route.setToInfo(cursor.getString(cursor.getColumnIndex(KEY_TO_INFO)));
                route.setInfo(cursor.getString(cursor.getColumnIndex(KEY_INFO)));
                route.setBusId(cursor.getString(cursor.getColumnIndex(KEY_BUS_ID)));
                route.setReservationCount(cursor.getString(cursor.getColumnIndex(KEY_RESERVATION_COUNT)));
                routeList.add(route);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return routeList;
    }
}//--class DBHelper