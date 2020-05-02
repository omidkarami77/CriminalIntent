package com.example.criminalintent.controller;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

public class CrimeListActivity extends SingleFragmentActivity {


    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, CrimeListActivity.class);
        intent.putExtra("crimeId", id);


        return intent;
    }

    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}

