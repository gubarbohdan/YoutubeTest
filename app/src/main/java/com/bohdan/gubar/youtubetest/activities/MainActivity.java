package com.bohdan.gubar.youtubetest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bohdan.gubar.youtubetest.R;
import com.bohdan.gubar.youtubetest.adapters.PlaylistAdapter;
import com.bohdan.gubar.youtubetest.classes.NetworkState;
import com.bohdan.gubar.youtubetest.fragments.PlaylistFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlaylistAdapter.OnItemClickListener {

    public static final String PLAYLIST_POP_ID = "PL9tY0BWXOZFt2TyOofWG0XwA8IDC8SGIN";
    public static final String PLAYLIST_HIP_HOP_ID = "PL9tY0BWXOZFv-V7FvZP-PWuE-1x2Deqqe";
    public static final String PLAYLIST_ROCK_ID = "PL9tY0BWXOZFvrS_oXmav-as9fy3lt1i34";
    public static final String API_KEY = "AIzaSyBZEL0AeFRZSC0717cNvW3TKZTu3vYUZxI";

    public static final String BUNDLE_ID = "ID";
    public static final String BUNDLE_TITLE = "TITLE";

    private ImageView mPhotoImageView;
    private Toolbar mToolbar;
    private TextView mNameTextView;
    private TextView mEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, 1);
        }
        else {
            init();
            if(savedInstanceState == null){
                setFragment(PLAYLIST_POP_ID, getString(R.string.title_pop));
            }
        }
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPhotoImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView_user_photo);
        mNameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_name);
        mEmailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_email);

        setUserInfo();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list1) {
            setFragment(PLAYLIST_POP_ID, item.getTitle().toString());
        } else if (id == R.id.nav_list2) {
            setFragment(PLAYLIST_HIP_HOP_ID, item.getTitle().toString());
        } else if (id == R.id.nav_list3) {
            setFragment(PLAYLIST_ROCK_ID, item.getTitle().toString());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(String playlistId, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ID, playlistId);
        bundle.putString(BUNDLE_TITLE, title);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            finish();
        } else if(resultCode == 1){
            init();
            setFragment(PLAYLIST_POP_ID, getString(R.string.title_pop));

        }
    }

    private void setUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Picasso.with(this).load(user.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(mPhotoImageView);
        mToolbar.setTitle(user.getDisplayName());
        mNameTextView.setText(user.getDisplayName());
        mEmailTextView.setText(user.getEmail());
    }

    @Override
    public void onItemClick(String videoId) {
        NetworkState networkState = new NetworkState(this);
        if(networkState.checkNetworkState()){
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, API_KEY, videoId, 0, true, false);
            this.startActivity(intent);
        }
        else {
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.snackbar_no_internet), Snackbar.LENGTH_SHORT).show();
        }

    }
}
