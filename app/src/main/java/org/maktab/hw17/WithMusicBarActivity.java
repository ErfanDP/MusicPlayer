package org.maktab.hw17;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.maktab.hw17.controller.fragment.MusicBarFragment;
import org.maktab.hw17.controller.fragment.MusicInformationFragment;
import org.maktab.hw17.controller.fragment.MusicPagerFragment;
import org.maktab.hw17.controller.fragment.TrackListFragment;
import org.maktab.hw17.model.Music;

import java.util.Objects;

public class WithMusicBarActivity extends AppCompatActivity
        implements TrackListFragment.ListCallBacks, MusicInformationFragment.MusicInformationButtonsCallBacks, MusicBarFragment.MusicBarCallBacks {

    private View mLayoutMusicBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_music_bar);
        findViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentList = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragmentList == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, MusicPagerFragment.newInstance())
                    .commit();
        }
        Fragment fragmentBar = fragmentManager.findFragmentById(R.id.music_bar_container);
        if (fragmentBar == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.music_bar_container, MusicBarFragment.newInstance())
                    .commit();
        }
    }







    private void findViews() {
        mLayoutMusicBar = findViewById(R.id.music_bar_container);
    }




    @Override
    public void onMusicClick(Music music) {
        mLayoutMusicBar.setVisibility(View.VISIBLE);
        MusicBarFragment musicBarFragment = (MusicBarFragment) getSupportFragmentManager().findFragmentById(R.id.music_bar_container);
        Objects.requireNonNull(musicBarFragment).startMusic(music);
    }

    @Override
    public void onPlayClick() {
        MusicBarFragment fragment = (MusicBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.music_bar_container);
        Objects.requireNonNull(fragment).playOrPauseMusic();
    }

    @Override
    public Music onNextClick() {
        MusicBarFragment fragment = (MusicBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.music_bar_container);

        return Objects.requireNonNull(fragment).nextMusic();
    }

    @Override
    public Music onPreviousClick() {
        MusicBarFragment fragment = (MusicBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.music_bar_container);
        return Objects.requireNonNull(fragment).previousMusic();
    }


    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if ((fragment instanceof MusicInformationFragment)) {
            MusicBarFragment musicBarFragment = (MusicBarFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.music_bar_container);
            Objects.requireNonNull(musicBarFragment).musicInformationClosed();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MusicPagerFragment.newInstance())
                    .commit();
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean isMusicPlaying() {
        MusicBarFragment fragment = (MusicBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.music_bar_container);
        return Objects.requireNonNull(fragment).isMusicPlaying();
    }

    @Override
    public void onMusicBarClicked(int musicIndex) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container,MusicInformationFragment.newInstance(musicIndex))
                .commit();
    }

    @Override
    public void onMusicChanged(Music music) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if(fragment instanceof MusicInformationFragment){
            ((MusicInformationFragment)fragment).updateViews(music);
        }
    }



}