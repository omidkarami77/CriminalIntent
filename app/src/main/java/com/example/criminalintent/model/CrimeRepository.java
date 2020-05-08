package com.example.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.criminalintent.model.database.CrimeCursorWrapper;
import com.example.criminalintent.model.database.CrimeDBSchema;
import com.example.criminalintent.model.database.CrimeDBSchema.Crime.Cols;
import com.example.criminalintent.model.database.CrimeOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeRepository {
    public static CrimeRepository sCrimeRepository;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static CrimeRepository getInstance(Context context) {
        if (sCrimeRepository == null) {
            sCrimeRepository = new CrimeRepository(context);
        }
        return sCrimeRepository;
    }

    private CrimeRepository(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeOpenHelper(mContext).getWritableDatabase();
    }

    public void insertCrime(Crime crime) {
        mDatabase.insertOrThrow(CrimeDBSchema.Crime.NAME,
                null, getContentValues(crime));


    }

    public ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDBSchema.Crime.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeDBSchema.Crime.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeDBSchema.Crime.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeDBSchema.Crime.Cols.SOLVED,
                crime.isSolved() ? 1 : 0);
        contentValues.put(Cols.SUSPECT, crime.getSuspect());

        return contentValues;


    }

    public void updateCrime(Crime crime) throws Exception {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.update(CrimeDBSchema.Crime.NAME, getContentValues(crime),
                where, whereArgs);

    }

    public int getPosition(UUID id) {
        List<Crime> crimes = getCrimes();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(id)) {
                return i;
            }
        }
        return 0;
    }

    public void deleteCrime(Crime crime) throws Exception {
        Crime c = getCrime(crime.getId());
        if (c == null) {
            throw new Exception("this crime does not exist");
        }
        //  mCrimes.remove(c);
    }

    public Crime getCrime(UUID uuid) {
        String[] whereArgs = new String[]{uuid.toString()};
        CrimeCursorWrapper cursor = queryCrime(Cols.UUID + " = ?", whereArgs);


        try {

            if (cursor == null || cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();

            return cursor.getCrime();
        } finally {
            cursor.close();

        }

    }


    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrime(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();

            }

        } finally {

            cursor.close();

        }
        return crimes;
    }

    private CrimeCursorWrapper queryCrime(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeDBSchema.Crime.NAME, null, where
                , whereArgs, null, null,
                null);

        return new CrimeCursorWrapper(cursor);
    }
}
