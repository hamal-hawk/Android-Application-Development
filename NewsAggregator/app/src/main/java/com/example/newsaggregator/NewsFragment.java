package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.newsaggregator.databinding.FragmentNewsBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements ViewPagerClick{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Article> articlesList;
    //private ArrayList<Article> mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters

    private NewsAdapter newsAdapter;
    private ViewPager2 viewPager2;
    private FragmentNewsBinding binding;

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
            articlesList = (ArrayList<Article>)getArguments().getSerializable("ARTICLES_LIST");
            newsAdapter = new NewsAdapter(articlesList, getActivity(), this);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(viewPager2 != null){
            outState.putInt("CURRENT", viewPager2.getCurrentItem());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewPager2 = binding.viewPagerTest;
        if(savedInstanceState != null){
            viewPager2.setCurrentItem(savedInstanceState.getInt("CURRENT"));
        }
        if(newsAdapter != null){
            viewPager2.setAdapter(newsAdapter);
            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        }
        return view;
    }

    @Override
    public void onItemClick(int position) {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(articlesList.get(position).getUrl()));
            startActivity(intent);

        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public void onItemLongClick(int position) {

    }
}