package com.example.criminalintent.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.criminalintent.R;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.CrimeRepository;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private Boolean mIsSubtitleVisible = false;
    public static final String KEY_IS_SUBTITLE_VISIBLE = "isSubtitleVisible";

    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view_crime);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }


    private class CrimeHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private ImageView mImageViewSolved;
        private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewDate = itemView.findViewById(R.id.txtview_list_item_date);
            mTextViewTitle = itemView.findViewById(R.id.txtview_list_item_title);
            mImageViewSolved = itemView.findViewById(R.id.imageview_solved);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   Intent intent = CrimeDetailActivity.newIntent(getActivity(), mCrime.getId());
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                    startActivity(intent);
                }
            });

        }

        public void bindCrime(Crime crime) {
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mCrime = crime;


        }


    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> mCrimes) {
            this.mCrimes = mCrimes;
        }

        public void setCrimes(List<Crime> mCrimes) {
            this.mCrimes = mCrimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);

            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
//holder.mTextViewTitle.setText(mCrimes.get(position).getTitle());
//holder.mTextViewDate.setText(mCrimes.get(position).getDate().toString());
            holder.bindCrime(mCrimes.get(position));

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private void updateUI() {
        List<Crime> crimes = CrimeRepository.getInstance(getActivity()).getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();

        }
        updateSubtitle();
    }

    private void updateSubtitle() {
        int nuumber = CrimeRepository.getInstance(getActivity()).getCrimes().size();
        String str = nuumber + " crimes";
        String subtitle = getString(R.string.subtitle_format, nuumber, "CriminalIntent");
        if (!mIsSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem item = menu.findItem(R.id.menu_item_subtitle);
        if (mIsSubtitleVisible)
            item.setTitle(R.string.hide_subtitle);
        else
            item.setTitle(R.string.show_subtitle);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_add_crime:
                Crime crime = new Crime();
                CrimeRepository.getInstance(getActivity()).insertCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                Toast.makeText(getActivity(), "add crime", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (savedInstanceState != null)
            mIsSubtitleVisible = savedInstanceState.getBoolean(KEY_IS_SUBTITLE_VISIBLE);

    }
}

