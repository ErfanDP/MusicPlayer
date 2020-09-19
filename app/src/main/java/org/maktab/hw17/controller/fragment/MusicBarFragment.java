package org.maktab.hw17.controller.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.maktab.hw17.R;
import org.maktab.hw17.model.Music;
import org.maktab.hw17.repository.MusicRepository;

import java.io.IOException;

public class MusicBarFragment extends Fragment {
    private Music mMusic;
    private MediaPlayer mMediaPlayer;
    private TextView mMusicTitle;
    private TextView mMusicArtist;
    private SeekBar mSeekBar;
    private ImageView mImageViewPlay;
    private MusicBarCallBacks mCallBacks;
    private MusicRepository mRepository;


    private Handler mHandler = new Handler();
    private Runnable mRunnableSeekBar = new Runnable() {
        @Override
        public void run() {
            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
            mHandler.postDelayed(this, 50);
        }
    };


    public static MusicBarFragment newInstance() {
        return new MusicBarFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRepository = MusicRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_bar, container, false);
        mMediaPlayer = new MediaPlayer();
        findViews(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicInformationOpened();
                mCallBacks.onMusicBarClicked(mRepository.getMusics().indexOf(mMusic));
            }
        });
        seekBarInit();
        listeners();
        return view;
    }

    private void listeners() {
        mImageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPauseMusic();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
    }

    private void findViews(View view) {
        mSeekBar = view.findViewById(R.id.seekBar);
        mMusicArtist = view.findViewById(R.id.bar_music_artist);
        mMusicTitle = view.findViewById(R.id.bar_music_name);
        mImageViewPlay = view.findViewById(R.id.bar_imageView_play_pause);
    }

    private void seekBarInit() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }
        });
    }

    private void updateSeekBar() {
        mSeekBar.setMax(mMediaPlayer.getDuration());
        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
        mHandler.postDelayed(mRunnableSeekBar, 50);
    }

    public void startMusic(Music music) {
        try {
            mMusic = music;
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(music.getFilePath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            updateSeekBar();
            mMusicArtist.setText(music.getArtist());
            mMusicTitle.setText(music.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void musicInformationOpened() {
        mMusicArtist.setVisibility(View.GONE);
        mImageViewPlay.setVisibility(View.INVISIBLE);
        mMusicTitle.setVisibility(View.GONE);
    }

    public void musicInformationClosed() {
        mMusicArtist.setVisibility(View.VISIBLE);
        mImageViewPlay.setVisibility(View.VISIBLE);
        mMusicTitle.setVisibility(View.VISIBLE);
    }

    public void playOrPauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
        playIconCheck();
    }

    public void playIconCheck(){
        if (mMediaPlayer.isPlaying()) {
            mImageViewPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
        } else {
            mImageViewPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
        }
    }

    public boolean isMusicPlaying() {
        return mMediaPlayer.isPlaying();
    }


    public Music  nextMusic(){
        mMusic = mRepository.nextMusic(mRepository.getMusics().indexOf(mMusic));
        startMusic(mMusic);
        return mMusic;
    }

    public Music previousMusic() {
        try {
            mMusic = mRepository.getMusics().get(mRepository.getMusics().indexOf(mMusic) - 1);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mMusic.getFilePath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            updateSeekBar();
            mMusicArtist.setText(mMusic.getArtist());
            mMusicTitle.setText(mMusic.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mMusic;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallBacks = (MusicBarCallBacks) context;
    }

    public interface MusicBarCallBacks {
        void onMusicBarClicked(int musicIndex);
    }
}