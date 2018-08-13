package com.nhatran.mybudgetmanagemen.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nhatran.mybudgetmanagemen.Payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PaymentDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PaymentManagement";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Payment";

    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_MONEY = "money";
    private static final String KEY_UNIT = "unit";
    private static final int ID_SPECIAL = -1;

    public PaymentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_students_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_TIME, KEY_MONEY, KEY_UNIT);
        sqLiteDatabase.execSQL(create_students_table);
    }


    public void addPayment(Payment payment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TIME,payment.getTimeAsString());
        contentValues.put(KEY_MONEY, String.valueOf(payment.getMoneyAmount()));
        contentValues.put(KEY_UNIT, payment.getPaymentUnit());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void saveTotalAndDate(long total, String fromDate, String toDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, ID_SPECIAL);
        contentValues.put(KEY_TIME, String.valueOf(total));
        contentValues.put(KEY_MONEY, fromDate);
        contentValues.put(KEY_UNIT,toDate);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void updateTotalAndDate(long total, String fromDate, String toDate){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, String.valueOf(total));
        values.put(KEY_MONEY, fromDate);
        values.put(KEY_UNIT, toDate);
        database.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(ID_SPECIAL) });
        database.close();
    }

    public long getTotal(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID+" = ?", new String[] {String.valueOf(ID_SPECIAL)},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (cursor.moveToFirst()){
            return Long.valueOf(cursor.getString(1));
        }
        return 0;
    }

    public Date getFromDate(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID+" = ?", new String[] {String.valueOf(ID_SPECIAL)},null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.moveToFirst()){
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");
            try {
                return dateFormat.parse(cursor.getString(2));


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    public Date getToDate(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID+" = ?", new String[] {String.valueOf(ID_SPECIAL)},null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.moveToFirst()){
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");
            try {
                return dateFormat.parse(cursor.getString(3));


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Payment getPayment(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID+" = ?", new String[] {String.valueOf(id)},null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(cursor.getString(1));
            return new Payment(date, Long.valueOf(cursor.getString(2)), cursor.getString(3));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Payment> getAllPayments(){
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false){
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");
            try {
                Date date = dateFormat.parse(cursor.getString(1));
                payments.add(new Payment(cursor.getInt(0), date, Long.valueOf(cursor.getString(2)), cursor.getString(3)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cursor.moveToNext();
        }
        return payments;
    }

    public void updatePayment(Payment payment){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, payment.getTimeAsString());
        values.put(KEY_MONEY, String.valueOf(payment.getMoneyAmount()));
        values.put(KEY_UNIT, payment.getPaymentUnit());
        database.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(payment.getId()) });
        database.close();
    }

    public void deletePayment(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME,KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteTotalAndDate(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME,KEY_ID + " = ?", new String[]{String.valueOf(ID_SPECIAL)});
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        sqLiteDatabase.execSQL(drop_students_table);

        onCreate(sqLiteDatabase);
    }
}
