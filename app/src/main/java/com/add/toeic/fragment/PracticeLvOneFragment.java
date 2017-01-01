package com.add.toeic.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.add.toeic.R;
import com.add.toeic.activity.PracticeToiecActivity;
import com.add.toeic.adapter.PracticeLevelAdapter;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.WordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { PracticeLvOneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PracticeLvOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PracticeLvOneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<WordInfo> mWordInfoArrayList;
    private PracticeLevelAdapter mPracticeLevelAdapter;
    private ListView mListView;
    private View view;
    private Context mContext;
    private List<WordInfo> wordInfoList;

    private OnFragmentInteractionListener mListener;

    public PracticeLvOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PracticeLvOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PracticeLvOneFragment newInstance() {
        PracticeLvOneFragment fragment = new PracticeLvOneFragment();
        Bundle args = new Bundle();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_practice_lv_one, container, false);
        initViewList();
        // Inflate the layout for this fragment
        return view;
    }

    private void initViewList() {

        mListView = (ListView) view.findViewById(R.id.lv_practice_lv_one);
        mWordInfoArrayList = new ArrayList<>();
        mPracticeLevelAdapter = new PracticeLevelAdapter(mContext, R.layout.practice_lv_item, mWordInfoArrayList);
        mListView.setAdapter(mPracticeLevelAdapter);

        initLoadData();

        initControl();
    }

    private void initControl() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(mContext, "Photo", Toast.LENGTH_SHORT).show();
                }

                WordInfo info = wordInfoList.get(position);
                Intent intent = new Intent(getActivity(), PracticeToiecActivity.class);
                intent.putExtra("keyTitle", info.getEnglsih());
                startActivity(intent);

            }
        });
    }

    private void initLoadData() {
        AsyncTask<Void, Void, List<WordInfo>> loadDataPracticeTask = new AsyncTask<Void, Void, List<WordInfo>>() {
            @Override
            protected List<WordInfo> doInBackground(Void... params) {
                return getListWord();
            }

            @Override
            protected void onPostExecute(List<WordInfo> wordInfos) {
                mPracticeLevelAdapter.addAll(wordInfos);
                mListView.setAdapter(mPracticeLevelAdapter);
                super.onPostExecute(wordInfos);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        };
        loadDataPracticeTask.execute();
    }

    private List<WordInfo> getListWord() {
        wordInfoList = new ArrayList<WordInfo>();

        WordInfo wordInfo1 = new WordInfo();
        wordInfo1.setEnglish("Part 1");
        wordInfo1.setVietnamese("Photo");
        wordInfo1.setIcon(getResources().getDrawable(R.drawable.photo, mContext.getTheme()));
        wordInfoList.add(wordInfo1);

        WordInfo wordInfo2 = new WordInfo();
        wordInfo2.setEnglish("Part 2");
        wordInfo2.setVietnamese("Question response");
        wordInfo2.setIcon(getResources().getDrawable(R.drawable.question_responce, mContext.getTheme()));
        wordInfoList.add(wordInfo2);

        WordInfo wordInfo3 = new WordInfo();
        wordInfo3.setEnglish("Part 3");
        wordInfo3.setVietnamese("Short conversation");
        wordInfo3.setIcon(getResources().getDrawable(R.drawable.short_conversatiob, mContext.getTheme()));
        wordInfoList.add(wordInfo3);

        WordInfo wordInfo4 = new WordInfo();
        wordInfo4.setEnglish("Part 4");
        wordInfo4.setVietnamese("Incomplete Sentence");
        wordInfo4.setIcon(getResources().getDrawable(R.drawable.iincomplete_sentence, mContext.getTheme()));
        wordInfoList.add(wordInfo4);

        WordInfo wordInfo5 = new WordInfo();
        wordInfo5.setEnglish("Part 5");
        wordInfo5.setVietnamese("Text Completion");
        wordInfo5.setIcon(getResources().getDrawable(R.drawable.text_complete, mContext.getTheme()));
        wordInfoList.add(wordInfo5);

        return wordInfoList;
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
}
