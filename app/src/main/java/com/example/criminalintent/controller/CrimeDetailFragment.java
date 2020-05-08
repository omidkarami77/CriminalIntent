package com.example.criminalintent.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
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

import com.example.criminalintent.DatePickerFragment;
import com.example.criminalintent.R;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.model.CrimeRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeDetailFragment extends Fragment {
    private static final String DATE_PICKER_FRAGMENT_TAG = "DatePicker";
    private static final int REQUEST_CODE_DATE_PICKER = 0;
    private static final int REQUEST_CODE_CONTACT = 1;
    private Crime mCrime;
    public static final String TAG = "CrimeDetailFragment";
    private EditText mEditTextTitle;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;
    public static final String ARG_CRIME_ID = "crimeId";
    private Button mButtonSuspect;
    private Button mButtonReport;
    private Intent contactIntent;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);
        mEditTextTitle = view.findViewById(R.id.edittext_title);
        mButtonDate = view.findViewById(R.id.button_date);
        mCheckBoxSolved = view.findViewById(R.id.checkbox_solved);
        mButtonReport = view.findViewById(R.id.button_report);
        mButtonSuspect = view.findViewById(R.id.button_suspect);
        mEditTextTitle.setText(mCrime.getTitle());
        mButtonDate.setText(mCrime.getDate().toString());

        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mButtonSuspect.setText(mCrime.getSuspect() == null ? getString(R.string.crime_suspect_text) : mCrime.getSuspect());

        contactIntent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getActivity().getPackageManager().resolveActivity(contactIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo == null) {
            mButtonSuspect.setEnabled(false);
        }

        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                // create parent child  between CrimeDetailFragment and DatePickerFragment
                datePickerFragment.setTargetFragment(CrimeDetailFragment.this, REQUEST_CODE_DATE_PICKER);
                datePickerFragment.show(getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
            }
        });
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

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String report = getCrimeReport();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));

                startActivity(intent.createChooser(intent, getString(R.string.send_report)));
            }
        });
        mButtonSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(contactIntent, REQUEST_CODE_CONTACT);
            }
        });
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

        try {
            CrimeRepository.getInstance(getActivity()).updateCrime(mCrime);
        } catch (Exception e) {
            Log.e(TAG, "cannot update crime", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_CRIME_DATE);
            mCrime.setDate(date);

            mButtonDate.setText(date.toString());
        } else if (requestCode == REQUEST_CODE_CONTACT) {
            Uri contactUri = data.getData();
            getSuspectFromContact(contactUri);
        }


    }

    private void getSuspectFromContact(Uri contactUri) {
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

        Cursor cursor = getActivity().getContentResolver().query(contactUri, projection,
                null, null, null);


        if (cursor == null || cursor.getCount() == 0) {
            return;
        }
        try {
            cursor.moveToFirst();
            String suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            mCrime.setSuspect(suspect);
            mButtonSuspect.setText(suspect);
        } finally {
            cursor.close();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeRepository.getInstance(getActivity()).getCrime(crimeId);


        contactIntent = new Intent(Intent.ACTION_PICK);
        contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
    }

    private String getCrimeReport() {
        String dateString = new SimpleDateFormat("yyyy/MM/dd").format(mCrime.getDate());
        String solvedString = mCrime.isSolved() ? getString(R.string.checkbox_solved) : getString(R.string.crime_report_unsolved);
        String suspectString = mCrime.getSuspect() == null ? getString(R.string.crime_report_no_suspect) : getString(R.string.crime_report_suspect, mCrime.getSuspect());

        return getString(R.string.crime_report, mCrime.getTitle(), dateString,
                solvedString,
                suspectString);
    }


}
