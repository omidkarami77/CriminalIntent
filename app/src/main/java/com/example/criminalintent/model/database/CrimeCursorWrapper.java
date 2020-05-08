package com.example.criminalintent.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {


        int intColUUID = getColumnIndex(CrimeDBSchema.Crime.Cols.UUID);
        String strUUID = getString(intColUUID);
        String title = getString(getColumnIndex(CrimeDBSchema.Crime.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDBSchema.Crime.Cols.DATE));
        boolean solved = getInt(getColumnIndex(CrimeDBSchema.Crime.Cols.SOLVED)) == 1;
        String suspect = getString(getColumnIndex(CrimeDBSchema.Crime.Cols.SUSPECT));


        Crime crime = new Crime(UUID.fromString(strUUID));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(solved);
        crime.setSuspect(suspect);
        return crime;

    }
}
