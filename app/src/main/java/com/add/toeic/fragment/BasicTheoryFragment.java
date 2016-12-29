package com.add.toeic.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.add.toeic.R;
import com.add.toeic.adapter.BookTheoryAdapter;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.BookTheory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BasicTheoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicTheoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<BookTheory> mArrList = null;
    private BookTheoryAdapter mViewAdapter = null;
    private ListView mListView = null;
    private Context mContext;
    private View view;


    private OnFragmentInteractionListener mListener;

    public BasicTheoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicTheoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicTheoryFragment newInstance(String param1, String param2) {
        BasicTheoryFragment fragment = new BasicTheoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_basic_theory, container, false);
        initViewList();

        return view;
    }

    public void initViewList() {
        mListView = (ListView) view.findViewById(R.id.list_theory);
        mArrList = new ArrayList<>();

        mViewAdapter = new BookTheoryAdapter(getContext(), R.layout.booktheory_item_layout, mArrList);
        mListView.setAdapter(mViewAdapter);

        initLoadData();
    }

    public void initLoadData() {
        AsyncTask<Void, Void, List<BookTheory>> loadDataBasicTheoryTask = new AsyncTask<Void, Void, List<BookTheory>>() {
//            private ProgressDialog progress = null;

            @Override
            protected void onPreExecute() {
//                progress = ProgressDialog.show(getContext(), null, "Loading application info...");
                super.onPreExecute();
            }

            @Override
            protected List<BookTheory> doInBackground(Void... params) {
                return getListBooks();
            }

            @Override
            protected void onPostExecute(List<BookTheory> listWords) {
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

        loadDataBasicTheoryTask.execute();
    }

    public List<BookTheory> getListBooks() {
        List<BookTheory> listBook = new ArrayList<BookTheory>();
        ArrayList<BookTheory> arr = new ArrayList<>();

        BookTheory book1 = new BookTheory();
        book1.setNumber("1. ");
        book1.setContent("Sach hay 1");

        BookTheory book2 = new BookTheory();
        book2.setNumber("2. ");
        book2.setContent("Sach hay 2");

        BookTheory book3 = new BookTheory();
        book3.setNumber("3. ");
        book3.setContent("Sach hay 3");

        return listBook;
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
