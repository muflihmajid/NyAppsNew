package com.example.nytimesapps.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nytimesapps.API.apiClient;
import com.example.nytimesapps.API.nyTimesAPI;
import com.example.nytimesapps.Adapter.EndlessRecyclerViewScrollListener;
import com.example.nytimesapps.Adapter.NetworkUtils;
import com.example.nytimesapps.Adapter.RecyclerViewAdapter;
import com.example.nytimesapps.BuildConfig;
import com.example.nytimesapps.Model.Article;
import com.example.nytimesapps.Model.ResponseWrapper;
import com.example.nytimesapps.R;
import com.example.nytimesapps.databinding.FragmentHomeBinding;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.io.IOException;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    StaggeredGridLayoutManager mLayoutManager;
    private final static String API_KEY = "awpgAVdp8jZq7AcCnEfIGZJxIHeh6kxS";
    private final static String TAG = "NYTimesSearch";
    EndlessRecyclerViewScrollListener scrollListener;
    String mQuery = null;
    int mPage = 0;
    String mNewsDesk;
    FragmentHomeBinding binding;
    //Tambahan
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Article> NewsList;
    private MaterialFavoriteButton materialFavoriteButtonNice;
    ProgressDialog pd;
//    private FavoriteDbHelper favoriteDbHelper;
//    private Movie favorite;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_home,
                container, false);
        if (savedInstanceState != null) {
            NewsList = savedInstanceState.getParcelableArrayList("articles");
        } else {
            NewsList = new ArrayList<>();
        }
        adapter = new RecyclerViewAdapter(getContext(), NewsList);
        binding.recylerView.setAdapter(adapter);

        switch(getActivity1().getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);

                break;
        }

        binding.recylerView.setLayoutManager(mLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPage = page;

                Handler handler = new Handler();
                Runnable runnableCode = () -> loadJSON(page);
                handler.postDelayed(runnableCode, 500);

            }
        };
        binding.recylerView.addOnScrollListener(scrollListener);
        if (NewsList.size() == 0)
        {
            loadJSON(0);
        }
        return binding.getRoot();
    }

//    public void saveFavorite(){
//        favoriteDbHelper = new FavoriteDbHelper(getContext());
//        favorite = new Movie();
//
//        Double rate = movie.getVoteAverage();
//
//
//        favorite.setId(movie_id);
//        favorite.setOriginalTitle(movieName);
//        favorite.setPosterPath(thumbnail);
//        favorite.setVoteAverage(rate);
//        favorite.setOverview(synopsis);
//
//        favoriteDbHelper.addFavorite(favorite);
//    }
    public Activity getActivity1(){
        Context context = getContext();
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }

    private void loadJSON( final int pNum) {
        try{
            if (API_KEY.isEmpty()){
                Toast.makeText(getContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            Call<ResponseWrapper> call;

            nyTimesAPI apiCall = apiClient.getClient().create(nyTimesAPI.class);
            call = apiCall.loadArticles(API_KEY, mQuery, String.valueOf(pNum),mNewsDesk);
            binding.progressBar.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<ResponseWrapper>() {
                @Override
                public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        List<Article> article = response.body().getResponse().getArticles();
                        if(pNum == 0){
                            NewsList.clear();
                            scrollListener.resetState();
                        }
                        NewsList.addAll(article);
                        adapter.notifyItemRangeInserted(adapter.getItemCount(), NewsList.size() - 1);
                    } else {
                        try {
                            Log.d(TAG, response.errorBody().string());
                            if (response.code() == 429)
                                loadJSON(mPage);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Snackbar.make(binding.mainContent, R.string.response_unsuccessful, Snackbar.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(getContext(), "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("articles", NewsList);
    }
}
