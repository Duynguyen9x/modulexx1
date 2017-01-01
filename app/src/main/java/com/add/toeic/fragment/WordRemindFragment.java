package com.add.toeic.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.add.toeic.adapter.WordRemindAdapter;
import com.add.toeic.database.MyDatabaseHelper;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.Word;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordRemindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordRemindFragment extends Fragment implements WordRemindAdapter.OnRemindButtonClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Word> mArrList = null;
    private WordRemindAdapter mWordRemindAdapter = null;
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
    private MyDatabaseHelper db;

    public static final String AUTHORITY = "com.add.toeic";
    public static final String BASE_PATH = "word";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    //= Uri.parse("/data/user/0/com.add.toeic/databases/word_manager");
    //


    public WordRemindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordRemindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordRemindFragment newInstance(String param1, String param2) {
        WordRemindFragment fragment = new WordRemindFragment();
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
    public void onResume() {
        super.onResume();
//        Log.i("duy.pq", "URI1=" + db.getDatabaseName());
//        Log.i("duy.pq", "URI2=" + db.toString());
//        Log.i("duy.pq", "URI3=" + db.getWritableDatabase().getPath());
        mContext.getContentResolver().registerContentObserver(CONTENT_URI, true, contentObserver);
    }

    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            mWordRemindAdapter.addAll(getListWords());
            mListView.setAdapter(mWordRemindAdapter);
            mWordRemindAdapter.notifyDataSetChanged();
            Log.i("duy.pq", "contentObserver = ");

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.getContentResolver().unregisterContentObserver(contentObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("duynq", "WordRemindFragment : onCreateView");
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
        mWordRemindAdapter = new WordRemindAdapter(mContext, R.layout.word_item_layout_for_remind, mArrList, false);
        mWordRemindAdapter.setOnRemindButtonClickListener(this);
        mListView.setAdapter(mWordRemindAdapter);

        initLoadData();
    }

    public void initLoadData() {
        AsyncTask<Void, Void, List<Word>> loadDataTask = new AsyncTask<Void, Void, List<Word>>() {
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

                mWordRemindAdapter.addAll(listWords);
                mListView.setAdapter(mWordRemindAdapter);
                super.onPostExecute(listWords);
            }
        };

        loadDataTask.execute();
    }

    public List<Word> getListWords() {
        List<Word> listWord = new ArrayList<Word>();

        db = new MyDatabaseHelper(getActivity());
        //  db.createDefaultNotesIfNeed();
        ArrayList<Word> arr = db.getAllWords();

        return arr;
    }

    @Override
    public void onDestroyView() {
        Log.i("duynq", "WordRemindFragment : onDestroyView");
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