package com.add.toeic.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.add.toeic.R;
import com.add.toeic.activity.TopicItemActivity;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.HeaderWord;
import com.add.toeic.model.WordInfo;
import com.add.toeic.adapter.WordVocabularyParentAdapter;
import com.add.toeic.utils.ImageUtils;
import com.add.toeic.utils.WordUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WordVocabularyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordVocabularyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WordVocabularyParentAdapter mVocabularyAdapter = null;
    private ExpandableListView mListView = null;
    private Context mContext;
    private View view;
    private static String[] eng_word = null;
    private static String[] viet_word = null;

    //    private ExpandableListAdapter listAdapter;
    private List<WordInfo> mListDataHeader;
    private HashMap<WordInfo, List<WordInfo>> mListDataChild;

    private static ArrayList<HeaderWord> mHederWord;

    private OnFragmentInteractionListener mListener;

    public WordVocabularyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordVocabularyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordVocabularyFragment newInstance(String param1, String param2) {
        WordVocabularyFragment fragment = new WordVocabularyFragment();
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
        eng_word = mContext.getResources().getStringArray(R.array.new_word_eng);
        viet_word = mContext.getResources().getStringArray(R.array.new_word_viet);


        try {
            mHederWord = WordUtils.readAllHeaderWord(mContext);

//            for (int i = 0; i < mHederWord.size(); i++) {
//                Log.i("duy.pq", "mhedaer=" + mHederWord.get(i).toString());
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("duynq", "WordVocabularyFragment : onCreateView");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vocabulary, container, false);
        initAppList();
        return view;
    }

    public void initAppList() {

        // get the listview
        mListView = (ExpandableListView) view.findViewById(R.id.list_word);

        // preparing list data
        prepareListData();

        mVocabularyAdapter = new WordVocabularyParentAdapter(getContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mListView.setAdapter(mVocabularyAdapter);

        // Listview Group expanded listener
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        mListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview on child click listener
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Log.i("duy.pq", "setOnChildClickListener=" + groupPosition + "=" + childPosition);

                int location = groupPosition * 5 + childPosition;

                Log.i("duy.pq", "location=" + location);
                WordInfo info = (WordInfo) mVocabularyAdapter.getChild(groupPosition, childPosition);

                Intent intent = new Intent(getActivity(), TopicItemActivity.class);
                intent.putExtra("keyName", info.getVietnamese());
                intent.putExtra("location", location + "");
                startActivity(intent);
                return true;
            }
        });

        // Listview Group expanded listener
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                WordInfo info = mListDataHeader.get(groupPosition);
                info.setExpandIcon(getResources().getDrawable(R.drawable.arrow_up));

                mVocabularyAdapter.notifyDataSetChanged();
            }
        });

        // Listview Group collasped listener
        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                WordInfo info = mListDataHeader.get(groupPosition);
                info.setExpandIcon(getResources().getDrawable(R.drawable.arrow_dowm));

                mVocabularyAdapter.notifyDataSetChanged();
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        mListDataHeader = new ArrayList<WordInfo>();
        mListDataChild = new HashMap<WordInfo, List<WordInfo>>();

        // Adding group data
        for (int i = 0; i < eng_word.length; i++) {
            WordInfo wordInfo = new WordInfo();

            wordInfo.setEnglish(eng_word[i]);
            wordInfo.setVietnamese(viet_word[i]);
            wordInfo.setIcon(ImageUtils.loadDrawableParent(mContext, i));
            wordInfo.setExpandIcon(getResources().getDrawable(R.drawable.arrow_dowm));
            mListDataHeader.add(wordInfo);
        }

        // Adding child data


        for (int k = 0; k < 10; k++) {
            mListDataChild.put(mListDataHeader.get(k), getGroupHeader(k));
        }

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
    public void onDestroyView() {
        Log.i("duynq", "WordVocabularyFragment : onDestroyView");
        super.onDestroyView();
    }

    private List<WordInfo> getGroupHeader(int position) {
        if (mHederWord == null)
            return null;
        List<WordInfo> header1 = new ArrayList<WordInfo>();
        for (int i = 0; i < 5; i++) {
            WordInfo wordInfo = new WordInfo();
            wordInfo.setEnglish(mHederWord.get(position * 5 + i).getHeader_eng());
            wordInfo.setVietnamese(mHederWord.get(position * 5 + i).getHeader_vi());
            wordInfo.setIcon(ImageUtils.loadDrawableChild(mContext, position, i));
            header1.add(wordInfo);
        }
        return header1;
    }
}
