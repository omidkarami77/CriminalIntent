package com.example.criminalintent;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class CrimeDetailActivity extends SingleFragmentActivity {
    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.crimeId";

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, CrimeDetailActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, id);

        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
       CrimeDetailFragment crimeDetailFragment = CrimeDetailFragment.newInstance(id);

        return crimeDetailFragment;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

    }

}


