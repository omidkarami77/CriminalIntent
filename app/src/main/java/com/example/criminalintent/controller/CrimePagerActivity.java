package com.example.criminalintent.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewParent;

import com.example.criminalintent.R;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.CrimeRepository;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewpager;
    private List<Crime> mCrimes;
    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.controller.crimeId";

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, uuid);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mCrimes = CrimeRepository.getInstance().getCrimes();
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        int position = CrimeRepository.getInstance().getPosition(id);
        mViewpager = findViewById(R.id.viewpager_crime);
        mViewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                CrimeDetailFragment fragment = CrimeDetailFragment.newInstance(mCrimes.get(position).getId());
                return fragment;
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        mViewpager.setCurrentItem(position);


    }
}
