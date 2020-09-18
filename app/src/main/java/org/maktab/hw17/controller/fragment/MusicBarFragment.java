package org.maktab.hw17.controller.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
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

import java.io.IOException;

public class MusicBarFragment extends Fragment {
    private MediaPlayer mMediaPlayer;
    private TextView mMusicTitle;
    private TextView mMusicArtist;
    private SeekBar mSeekBar;
    private ImageView mImageViewPlay;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_music_bar, container, false);
        mMediaPlayer = new MediaPlayer();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
            }
        });
        findViews(view);
        seekBarInit();
        mImageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    mImageViewPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
                }else{
                    mMediaPlayer.start();
                    mImageViewPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
                }
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //todo
            }
        });
        return view;
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
                if( fromUser){
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

    public void playMusic(Music music){
        try {
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(music.getFilePath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            updateSeekBar();
            mMusicArtist.setText(music.getArtist());
            mMusicTitle.setText(music.getName());
//            MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
//            metaRetriver.setDataSource(music.getFilePath());
//            byte[] art = metaRetriver.getEmbeddedPicture();
//            if(art !=null) {
//                Bitmap songImage = BitmapFactory
//                        .decodeByteArray(art, 0, art.length);
//                mImageViewMusic.setImageBitmap(songImage);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}