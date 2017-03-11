package com.bohdan.gubar.youtubetest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bohdan.gubar.youtubetest.R;
import com.bohdan.gubar.youtubetest.activities.MainActivity;
import com.bohdan.gubar.youtubetest.adapters.EndlessScrollListener;
import com.bohdan.gubar.youtubetest.adapters.PlaylistAdapter;
import com.bohdan.gubar.youtubetest.application.MyApp;
import com.bohdan.gubar.youtubetest.classes.NetworkState;
import com.bohdan.gubar.youtubetest.model.playlist.Item;
import com.bohdan.gubar.youtubetest.model.playlist.VideoInfo;
import com.bohdan.gubar.youtubetest.model.realm.ListItem;
import com.bohdan.gubar.youtubetest.model.video.VideoDetail;
import com.bohdan.gubar.youtubetest.retrofit.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    public static final String QUERY_KEY = "key";
    public static final String QUERY_PART = "part";
    public static final String QUERY_PLAYLIST_ID = "playlistId";
    public static final String QUERY_MAX_RESULTS = "maxResults";
    public static final String SNIPPET = "snippet,status";
    public static final String QUERY_PAGE_TOKEN = "pageToken";
    public static final String TAG_RETROFIT = "RETROFIT";

    private String playlistId;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PlaylistAdapter mPlaylistAdapter;

    private List<ListItem> mList;

    private String nextPageToken;
    private EndlessScrollListener scrollListener;

    private Realm realm;

    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        realm = Realm.getDefaultInstance();

        Bundle bundle = getArguments();
        playlistId = bundle.getString(MainActivity.BUNDLE_ID);


        ((MainActivity)getActivity()).getSupportActionBar().setTitle(bundle.getString(MainActivity.BUNDLE_TITLE));

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mList = new ArrayList<>();



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        scrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };



        mPlaylistAdapter = new PlaylistAdapter(getActivity(), mList, (MainActivity)getActivity());
        mRecyclerView.setAdapter(mPlaylistAdapter);

        NetworkState networkState = new NetworkState(getActivity());

        if(networkState.checkNetworkState()){
            mRecyclerView.addOnScrollListener(scrollListener);
            getPlaylistInfoFromNet();
        }
        else {
            getPlaylistInfoFromCache();
        }




        return view;
    }

    private void getPlaylistInfoFromNet(){
        mProgressBar.setVisibility(View.VISIBLE);
        final RetrofitApi retrofitApi = MyApp.getRetrofitApi();
        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put(QUERY_PART, SNIPPET);
        optionsMap.put(QUERY_PLAYLIST_ID, playlistId);
        optionsMap.put(QUERY_KEY, MainActivity.API_KEY);
        optionsMap.put(QUERY_MAX_RESULTS, "10");
        if(nextPageToken != null) {
            if(!nextPageToken.equals("last")){
                optionsMap.put(QUERY_PAGE_TOKEN, nextPageToken);
            }
            else {
                mProgressBar.setVisibility(View.GONE);
                return;
            }
        }


        Call<VideoInfo> call = retrofitApi.listRepos(optionsMap);
        call.enqueue(new Callback<VideoInfo>() {
            @Override
            public void onResponse(Call<VideoInfo> call, Response<VideoInfo> response) {
                nextPageToken = response.body().getNextPageToken();

                if(nextPageToken == null){
                    nextPageToken = "last";
                }

                for (Item i:response.body().getItems()
                     ) {
                    if(i.getStatus().getPrivacyStatus().equals("public")){
                        ListItem item = new ListItem();
                        item.setVideoId(i.getSnippet().getResourceId().getVideoId());
                        item.setPhotoUrl(i.getSnippet().getThumbnails().getHigh().getUrl());
                        item.setTitle(i.getSnippet().getTitle());
                        item.setDescription(i.getSnippet().getDescription());
                        item.setPlaylistId(playlistId);
                        mList.add(item);
                    }
                }

                realm.beginTransaction();
                List<ListItem> listItem = realm.copyToRealmOrUpdate(mList);
                realm.commitTransaction();

                mPlaylistAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);

                for (final ListItem i:mList
                     ) {
                    Map<String, String> optionsMap = new HashMap<>();
                    optionsMap.put(QUERY_PART, "contentDetails");
                    optionsMap.put("id", i.getVideoId());
                    optionsMap.put(QUERY_KEY, MainActivity.API_KEY);

                    Call<VideoDetail> callVideo = retrofitApi.videoDetail(optionsMap);
                    callVideo.enqueue(new Callback<VideoDetail>() {
                        @Override
                        public void onResponse(Call<VideoDetail> call, Response<VideoDetail> response) {
                            i.setDuration(response.body().getItems().get(0).getContentDetails().getDuration());
                            mPlaylistAdapter.notifyDataSetChanged();

                            realm.beginTransaction();
                            ListItem listItem = realm.copyToRealmOrUpdate(i);
                            realm.commitTransaction();
                        }

                        @Override
                        public void onFailure(Call<VideoDetail> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<VideoInfo> call, Throwable t) {
                Log.e(TAG_RETROFIT, t.getMessage());
            }
        });
    }

    private void getPlaylistInfoFromCache(){
        List<ListItem> list = realm.where(ListItem.class).equalTo(QUERY_PLAYLIST_ID, playlistId).findAll();
        mList.addAll(list);
        mPlaylistAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
    }

    public void loadNextDataFromApi(int offset) {
        getPlaylistInfoFromNet();
    }

}
