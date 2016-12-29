package com.add.toeic.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.add.toeic.R;
import com.add.toeic.adapter.VocabularyRemindAdapter;
import com.add.toeic.database.MyDatabaseHelper;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.Word;
import com.add.toeic.model.WordInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemindFragment extends Fragment implements VocabularyRemindAdapter.OnRemindButtonClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<WordInfo> mArrList = null;
    private VocabularyRemindAdapter mVocabularyRemindAdapter = null;
    private ListView mListView = null;
    private Context mContext;
    private View view;
    private Button mBtnDel;
    private Button mBtnRunAuto;
    private Button mBtnDelAll;
    private Button mBtnDone;

    LayoutInflater inflater;
    private LinearLayout mLinearLayout;

    private OnFragmentInteractionListener mListener;

    public RemindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemindFragment newInstance(String param1, String param2) {
        RemindFragment fragment = new RemindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("duynq", "RemindFragment : onCreateView");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_remind, container, false);
        initRemindList();

        return view;
    }

    public void initRemindList() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(mContext);

        mLinearLayout = (LinearLayout) view.findViewById(R.id.layout1);
//        final View menuLayout = inflater.inflate(R.layout.delete_run_button_layout, mLinearLayout, true);

        // anh duy dep trai


        mBtnDel = (Button) view.findViewById(R.id.delete_btn);
        mBtnRunAuto = (Button) view.findViewById(R.id.run_auto_btn);

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayout.removeAllViews();
                View menuLayout1 = inflater.inflate(R.layout.deleteall_done_button_layout, null);
                mLinearLayout.addView(menuLayout1);
                mBtnDelAll = (Button) view.findViewById((R.id.delete_all_btn));
                mBtnDone = (Button) view.findViewById((R.id.done_btn));
                Log.i("duy.nq", "mBtnDone = " + mBtnDone);
                mBtnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLinearLayout.removeAllViews();
                        View menuLayout1 = inflater.inflate(R.layout.delete_run_button_layout, null);
                        mLinearLayout.addView(menuLayout1);
                    }
                });
            }
        });

        Log.i("duy.nq", "mBtnDone1 = " + mBtnDone);
        if (mBtnDone != null) {
            mBtnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLinearLayout.removeAllViews();
                    View menuLayout1 = inflater.inflate(R.layout.delete_run_button_layout, null);
                    mLinearLayout.addView(menuLayout1);
                }
            });
        }


        mListView = (ListView) view.findViewById(R.id.list_remind);

        mArrList = new ArrayList<>();
        mVocabularyRemindAdapter = new VocabularyRemindAdapter(mContext, R.layout.word_item_layout_for_remind, mArrList);
        mVocabularyRemindAdapter.setOnRemindButtonClickListener(this);
        mListView.setAdapter(mVocabularyRemindAdapter);

        initLoadData();
    }

    public void initLoadData() {
        AsyncTask<Void, Void, List<WordInfo>> loadDataTask = new AsyncTask<Void, Void, List<WordInfo>>() {
//            private ProgressDialog progress = null;

            @Override
            protected void onPreExecute() {
//                progress = ProgressDialog.show(getContext(), null, "Loading application info...");
                super.onPreExecute();
            }

            @Override
            protected List<WordInfo> doInBackground(Void... params) {
                return getListWords();
            }

            @Override
            protected void onPostExecute(List<WordInfo> listWords) {
                try {
//                    progress.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                mVocabularyRemindAdapter.addAll(listWords);
                mListView.setAdapter(mVocabularyRemindAdapter);
                super.onPostExecute(listWords);
            }
        };

        loadDataTask.execute();
    }

    public List<WordInfo> getListWords() {
        List<WordInfo> listWord = new ArrayList<WordInfo>();

        MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
        db.createDefaultNotesIfNeed();


        ArrayList<Word> arr = db.getAllWords();
        for (int i = 0; i < arr.size(); i++) {
            Log.i("duy.pq", "item=" + arr.get(i).toString());
            WordInfo wordInfo = new WordInfo();
            wordInfo.setEnglish(arr.get(i).getName());
            wordInfo.setVietnamese(arr.get(i).getName_key());
            listWord.add(wordInfo);
        }


//        try {
//            arr = WordUtils.readAllData(mContext);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < 12; i++) {
//            WordInfo wordInfo = new WordInfo();
//
//            wordInfo.setEnglish(arr.get(1 * 12 + i).getName());
//            wordInfo.setVietnamese(arr.get(1 * 12 + i).getName_key());
//
//            listWord.add(wordInfo);
//        }

        return listWord;
    }

    @Override
    public void onDestroyView() {
        Log.i("duynq", "RemindFragment : onDestroyView");
        super.onDestroyView();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onHandleRemindButtonClick() {
        Toast.makeText(mContext, "ImageButton clicked", Toast.LENGTH_SHORT).show();
    }
}
