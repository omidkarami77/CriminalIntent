package com.example.criminalintent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.CrimeRepository;

import java.util.UUID;

import static com.example.criminalintent.CrimeDetailActivity.EXTRA_CRIME_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeDetailFragment extends Fragment {
    private Crime mCrime;
    public static final String TAG="CrimeDetailFragment";
    private EditText mEditTextTitle;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;
    public static final String ARG_CRIME_ID = "crimeId";


    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    public static CrimeDetailFragment newInstance(UUID id) {
        
        Bundle args = new Bundle();

        CrimeDetailFragment fragment = new CrimeDetailFragment();

        args.putSerializable(ARG_CRIME_ID, id);
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public void onPause() {
        super.onPause();

        try {
            CrimeRepository.getInstance().updateCrime(mCrime);
        } catch (Exception e) {
            Log.e(TAG,"cannot update crime",e);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//
//        UUID crimeId = (UUID) getActivity()
//                .getIntent()
//                .getSerializableExtra(CrimeDetailActivity.EXTRA_CRIME_ID);


        UUID crimeId= (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeRepository.getInstance().getCrime(crimeId);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);
        mEditTextTitle = view.findViewById(R.id.edittext_title);
        mButtonDate = view.findViewById(R.id.button_date);
        mCheckBoxSolved = view.findViewById(R.id.checkbox_solved);
        mEditTextTitle.setText(mCrime.getTitle());
        mButtonDate.setText(mCrime.getDate().toString());
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mButtonDate.setClickable(false);
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
}
