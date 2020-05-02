package com.example.criminalintent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeRepository {
    public static CrimeRepository sCrimeRepository;

    List<Crime> mCrimes;

    public static CrimeRepository getInstance() {
        if (sCrimeRepository == null) {
            sCrimeRepository = new CrimeRepository();
        }
        return sCrimeRepository;
    }

    private CrimeRepository() {
        mCrimes = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime " + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }

    public void insertCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void updateCrime(Crime crime) throws Exception {
        Crime c = getCrime(crime.getId());
        if (c == null) {
            throw new Exception("this crime does not exist");
        }
        c.setTitle(crime.getTitle());
        c.setDate(crime.getDate());
        c.setSolved(crime.isSolved());
    }

    public void deleteCrime(Crime crime) throws Exception {
        Crime c = getCrime(crime.getId());
        if (c == null) {
            throw new Exception("this crime does not exist");
        }
        mCrimes.remove(c);
    }

    public int getPosition(UUID id){
        Crime crime = getCrime(id);
       return mCrimes.indexOf(crime);
    }

    public Crime getCrime(UUID uuid) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }

    public void setCrimes(List<Crime> mCrimes) {
        this.mCrimes = mCrimes;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }
}
