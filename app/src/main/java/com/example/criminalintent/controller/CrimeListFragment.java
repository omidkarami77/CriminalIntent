package com.example.criminalintent.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
                   Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
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
        List<Crime> crimes = CrimeRepository.getInstance().getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();

        }

    }


}

