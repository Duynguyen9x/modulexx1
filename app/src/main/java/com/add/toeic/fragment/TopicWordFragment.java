package com.add.toeic.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.add.toeic.R;
import com.add.toeic.activity.DetailWordActivity;
import com.add.toeic.adapter.VocabularyTopicAdapter;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.Word;
import com.add.toeic.model.WordInfo;
import com.add.toeic.utils.ImageLoader;
import com.add.toeic.utils.WordUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TopicWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicWordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String location = "location";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //  private ArrayList<WordInfo> mArrList = null;
    private ArrayList<Word> mArrWord = null;

    private List<WordInfo> mListWord = null;
    private VocabularyTopicAdapter mViewAdapter = null;
    private ListView mListView = null;
    private Context mContext;
    private View view;
    private static String[] eng_word = null;
    private static String[] viet_word = null;

    private OnFragmentInteractionListener mListener;
    private static int mLocation;
    private ImageLoader mImageLoader;

    public TopicWordFragment() {
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopicWordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopicWordFragment newInstance(int param1, String param2) {
        TopicWordFragment fragment = new TopicWordFragment();
        Bundle args = new Bundle();
        args.putInt(location, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocation = getArguments().getInt(location);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        eng_word = mContext.getResources().getStringArray(R.array.new_word_eng);
        viet_word = mContext.getResources().getStringArray(R.array.new_word_viet);


        Log.i("duy.pq", "TopicWordFragment=" + mLocation);
        Log.i("anhdt1", "abc");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_topic_word, container, false);
        initWordList();

        return view;
    }

    public void initWordList() {
        mListView = (ListView) view.findViewById(R.id.list_topic_word);

        mArrWord = new ArrayList<Word>();
        mImageLoader = new ImageLoader(mContext, 100);
        //  mImageLoader.startIconLoaderThread();


        mViewAdapter = new VocabularyTopicAdapter(getContext(), R.layout.word_item_layout_for_remind, mArrWord, mImageLoader);
        mListView.setAdapter(mViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Log.i("duy.pq", "mListView.setOnItemClickListener=" + mListWord.get(position).getEnglsih());
                // SoundUtis.play(mContext, mListWord.get(position).getEnglsih());

                Intent i = new Intent(mContext, DetailWordActivity.class);
                i.putExtra("num_word", position);
                i.putExtra("num_group", mLocation);

                Log.i("duy.pq", "mListView.num_word=" + position + "/num_word= " + mLocation);
                startActivity(i);


            }
        });


        initLoadData();
    }

    public void initLoadData() {
        AsyncTask<Void, Void, List<Word>> loadBitmapTask = new AsyncTask<Void, Void, List<Word>>() {
//            private ProgressDialog progress = null;

            @Override
            protected void onPreExecute() {
//                progress = ProgressDialog.show(getContext(), null, "Loading application info...");
                super.onPreExecute();
            }

            @Override
            protected List<Word> doInBackground(Void... params) {
                return getListWords();
            }

            @Override
            protected void onPostExecute(List<Word> listWords) {
                try {
//                    progress.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                mViewAdapter.addAll(listWords);
                mListView.setAdapter(mViewAdapter);
                super.onPostExecute(listWords);
            }
        };

        loadBitmapTask.execute();
    }

    public List<Word> getListWords() {
        ArrayList<Word> arr = null;
        ArrayList<Word> arrResult = new ArrayList<Word>();
        try {
            arr = WordUtils.readAllData(mContext);
            int k = mLocation * 12;
            for (int t = k; t < k + 12; t++) {
                arrResult.add(arr.get(t));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return arrResult;
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
    public void onDestroy() {
        super.onDestroy();
        if (mImageLoader != null) {
            mImageLoader.stopIconLoaderThread();
        }
    }
}
