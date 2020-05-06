package com.example.criminalintent.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.criminalintent.model.database.CrimeDBSchema.*;

public class CrimeOpenHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public CrimeOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Crime.NAME +
                "(" +
                Crime.Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Crime.Cols.UUID + ", " +
                Crime.Cols.TITLE + ", " +
                Crime.Cols.DATE + ", " +
                Crime.Cols.SOLVED  +
                " )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
