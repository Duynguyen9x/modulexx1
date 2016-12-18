package info.androidhive.navigationdrawer.fragment;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.activity.AboutUsActivity;
import info.androidhive.navigationdrawer.activity.MainActivity;
import info.androidhive.navigationdrawer.activity.TopicItemActivity;
import info.androidhive.navigationdrawer.util.WordInfo;
import info.androidhive.navigationdrawer.adapter.ViewVocabularyAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VocabularyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VocabularyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocabularyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<WordInfo> mArrList = null;
    private ViewVocabularyAdapter mVocabularyAdapter = null;
    private ExpandableListView mListView = null;
    private Context mContext;
    private View view;
    private static String[] eng_word = null;
    private static String[] viet_word = null;

//    private ExpandableListAdapter listAdapter;
    private List<WordInfo> mListDataHeader;
    private HashMap<WordInfo, List<WordInfo>> mListDataChild;

    private OnFragmentInteractionListener mListener;

    public VocabularyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VocabularyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VocabularyFragment newInstance(String param1, String param2) {
        VocabularyFragment fragment = new VocabularyFragment();
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
        eng_word = getActivity().getResources().getStringArray(R.array.new_word_eng);
        viet_word = getActivity().getResources().getStringArray(R.array.new_word_viet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("duynq", "VocabularyFragment : onCreateView");
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

        mVocabularyAdapter = new ViewVocabularyAdapter(getContext(), mListDataHeader, mListDataChild);

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

                startActivity(new Intent(getActivity(), TopicItemActivity.class));
                return true;
            }
        });

        // Listview Group expanded listener
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                WordInfo info = mListDataHeader.get(groupPosition);
                info.setExpandIcon(getResources().getDrawable(R.drawable.ic_action_collapse));

                mVocabularyAdapter.notifyDataSetChanged();
            }
        });

        // Listview Group collasped listener
        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                WordInfo info = mListDataHeader.get(groupPosition);
                info.setExpandIcon(getResources().getDrawable(R.drawable.ic_action_expand));

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
            wordInfo.setIcon(getResources().getDrawable(R.drawable.ic_photo_library_black_24dp));
            wordInfo.setExpandIcon(getResources().getDrawable(R.drawable.ic_action_expand));
            mListDataHeader.add(wordInfo);
        }

        // Adding child data
        List<WordInfo> top250 = new ArrayList<WordInfo>();
        for (int i = 0; i < eng_word.length; i++) {
            WordInfo wordInfo = new WordInfo();

            wordInfo.setEnglish(eng_word[i]);
            wordInfo.setVietnamese(viet_word[i]);
            wordInfo.setIcon(getResources().getDrawable(R.drawable.bg_circle));

            top250.add(wordInfo);
        }

        List<WordInfo> nowShowing = new ArrayList<WordInfo>();
        for (int i = 0; i < eng_word.length; i++) {
            WordInfo wordInfo = new WordInfo();

            wordInfo.setEnglish(eng_word[i]);
            wordInfo.setVietnamese(viet_word[i]);
            wordInfo.setIcon(getResources().getDrawable(R.drawable.bg_circle));

            nowShowing.add(wordInfo);
        }

        List<WordInfo> comingSoon = new ArrayList<WordInfo>();
        for (int i = 0; i < eng_word.length; i++) {
            WordInfo wordInfo = new WordInfo();

            wordInfo.setEnglish(eng_word[i]);
            wordInfo.setVietnamese(viet_word[i]);
            wordInfo.setIcon(getResources().getDrawable(R.drawable.bg_circle));

            comingSoon.add(wordInfo);
        }

        List<WordInfo> comingSoon1 = new ArrayList<WordInfo>();
        for (int i = 0; i < eng_word.length; i++) {
            WordInfo wordInfo = new WordInfo();

            wordInfo.setEnglish(eng_word[i]);
            wordInfo.setVietnamese(viet_word[i]);
            wordInfo.setIcon(getResources().getDrawable(R.drawable.bg_circle));

            comingSoon1.add(wordInfo);
        }

        List<WordInfo> comingSoon2 = new ArrayList<WordInfo>();
        for (int i = 0; i < eng_word.length; i++) {
            WordInfo wordInfo = new WordInfo();

            wordInfo.setEnglish(eng_word[i]);
            wordInfo.setVietnamese(viet_word[i]);
            wordInfo.setIcon(getResources().getDrawable(R.drawable.bg_circle));

            comingSoon2.add(wordInfo);
        }

        mListDataChild.put(mListDataHeader.get(0), top250); // Header, Child data
        mListDataChild.put(mListDataHeader.get(1), nowShowing);
        mListDataChild.put(mListDataHeader.get(2), comingSoon);
        mListDataChild.put(mListDataHeader.get(3), comingSoon1);
        mListDataChild.put(mListDataHeader.get(4), comingSoon2);
    }


    public List<WordInfo> getListWord() {
        List<WordInfo> arr = new ArrayList<>();
        for (int i = 0; i < eng_word.length; i++) {
            WordInfo wordInfo = new WordInfo();

            wordInfo.setEnglish(eng_word[i]);
            wordInfo.setVietnamese(viet_word[i]);
            wordInfo.setIcon(getResources().getDrawable(R.drawable.ic_photo_library_black_24dp));

            arr.add(wordInfo);
        }

        return arr;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
        Log.i("duynq", "VocabularyFragment : onDestroyView");
        super.onDestroyView();
    }
}
