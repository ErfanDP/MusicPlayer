package org.maktab.hw17.controller.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.maktab.hw17.R;

import java.util.Objects;

public class MusicPagerFragment extends Fragment {
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    public static MusicPagerFragment newInstance() {

        return new MusicPagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_pager, container, false);
        findViews(view);
        mViewPager.setAdapter(new ViewPagerAdapter(Objects.requireNonNull(getActivity())));
        tabLayoutAndViewPagerBinder();
        return view;
    }

    private void findViews(View view) {
        mViewPager = view.findViewById(R.id.view_pager_music_lists);
        mTabLayout = view.findViewById(R.id.tablayout_lists);
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
                return "Artists";
            case 2:
                return "Albums";
        }
        return "null";
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return TrackListFragment.newInstance();
                case 1:
                    return ArtistsListFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}