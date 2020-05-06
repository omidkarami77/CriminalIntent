package com.example.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    // List<Crime> mCrimes;

    public static CrimeRepository getInstance(Context context) {
        if (sCrimeRepository == null) {
            sCrimeRepository = new CrimeRepository(context);
        }
        return sCrimeRepository;
    }

    private CrimeRepository(Context context) {
        //   mCrimes = new ArrayList<>();
//        for (int i = 0; i <= 5; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime " + i);
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//        }

        ;
        mContext = context.getApplicationContext();
        mDatabase = new CrimeOpenHelper(mContext).getWritableDatabase();
    }

    public void insertCrime(Crime crime) {
        //   mCrimes.add(crime);
        mDatabase.insert(CrimeDBSchema.Crime.NAME, null, getContentValues(crime));


    }

    public ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDBSchema.Crime.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeDBSchema.Crime.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeDBSchema.Crime.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeDBSchema.Crime.Cols.SOLVED, crime.isSolved() ? 1 : 0);


        return contentValues;


    }

    public void updateCrime(Crime crime) throws Exception {
//        Crime c = getCrime(crime.getId());
//        if (c == null) {
//            throw new Exception("this crime does not exist");
//        }
//        c.setTitle(crime.getTitle());
//        c.setDate(crime.getDate());
//        c.setSolved(crime.isSolved());

        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.update(CrimeDBSchema.Crime.NAME, getContentValues(crime), where, whereArgs);

    }

    public int getPosition(UUID id) {
//        Crime crime = getCrime(id);
//       return mCrimes.indexOf(crime);
        List<Crime>crimes=getCrimes();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(id)){
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
//        for (Crime crime : mCrimes) {
//            if (crime.getId().equals(uuid)) {
//                return crime;
//            }
//        }
        String[] whereArgs = new String[]{uuid.toString()};
        Cursor cursor = queryCrime(Cols.UUID + " = ?", whereArgs);


        try {
            cursor.moveToFirst();

            if (cursor == null || cursor.getCount() == 0)
                return null;

            int intColUUID = cursor.getColumnIndex(Cols.UUID);
            String strUUID = cursor.getString(intColUUID);
            String title = cursor.getString(cursor.getColumnIndex(Cols.TITLE));
            long date = cursor.getLong(cursor.getColumnIndex(Cols.DATE));
            boolean solved = cursor.getInt(cursor.getColumnIndex(Cols.SOLVED)) == 1;

            Crime crime = new Crime(UUID.fromString(strUUID));
            crime.setTitle(title);
            crime.setDate(new Date(date));
            crime.setSolved(solved);

            return crime;
        } finally {
            cursor.close();

        }

    }


    public List<Crime> getCrimes() {
        // return mCrimes;
        List<Crime> crimes = new ArrayList<>();

        Cursor cursor = queryCrime(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int intColUUID = cursor.getColumnIndex(Cols.UUID);
                String strUUID = cursor.getString(intColUUID);
                String title = cursor.getString(cursor.getColumnIndex(Cols.TITLE));
                long date = cursor.getLong(cursor.getColumnIndex(Cols.DATE));
                boolean solved = cursor.getInt(cursor.getColumnIndex(Cols.SOLVED)) == 1;

                UUID uuid = UUID.fromString(strUUID);
                Crime crime = new Crime(uuid);
                crime.setTitle(title);
                crime.setDate(new Date(date));
                crime.setSolved(solved);

                crimes.add(crime);
                cursor.moveToNext();

            }
        } finally {
            cursor.close();

        }
        return crimes;
    }

    private Cursor queryCrime(String where, String[] whereArgs) {
        return mDatabase.query(CrimeDBSchema.Crime.NAME, null, where
                , whereArgs, null, null,
                null);
    }
}
