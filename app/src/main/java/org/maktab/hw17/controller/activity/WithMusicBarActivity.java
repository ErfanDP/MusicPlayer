package org.maktab.hw17.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.maktab.hw17.R;
import org.maktab.hw17.controller.fragment.MusicBarFragment;
import org.maktab.hw17.controller.fragment.TrackListFragment;
import org.maktab.hw17.model.Music;

import java.util.Objects;

public class WithMusicBarActivity extends AppCompatActivity
        implements TrackListFragment.ListCallBacks {
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private View mLayoutMusicBar;
    private View mLayoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_music_bar);
        findViews();
        mViewPager.setAdapter(new ViewPagerAdapter(this));
        tabLayoutAndViewPagerBinder();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment =  fragmentManager.findFragmentById(R.id.music_bar_container);
        if(fragment == null){
            fragmentManager.beginTransaction()
                    .add(R.id.music_bar_container,MusicBarFragment.newInstance(),"tag")
                    .commit();
        }
    }

    private void findViews() {
        mViewPager = findViewById(R.id.view_pager_music_lists);
        mTabLayout = findViewById(R.id.tablayout_lists);
        mLayoutMusicBar = findViewById(R.id.music_bar_container);
        mLayoutList = findViewById(R.id.fragment_container);
    }

    private void tabLayoutAndViewPagerBinder() {
        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(getTabName(position));
            }
        }).attach();
    }

    private String getTabName(int position) {
        switch (position) {
            case 0:
                return "Tracks";
            case 1:
                return "Albums";
            case 2:
                return "Artists";
        }
        return "null";
    }

    @Override
    public void onMusicClick(Music music) {
        mLayoutMusicBar.setVisibility(View.VISIBLE);
        MusicBarFragment musicBarFragment = (MusicBarFragment) getSupportFragmentManager().findFragmentByTag("tag");
        Objects.requireNonNull(musicBarFragment).playMusic(music);
    }




    private class ViewPagerAdapter extends FragmentStateAdapter{
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return TrackListFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

}