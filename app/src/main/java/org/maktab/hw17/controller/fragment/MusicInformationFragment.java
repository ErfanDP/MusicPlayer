package org.maktab.hw17.controller.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.maktab.hw17.R;
import org.maktab.hw17.model.Music;
import org.maktab.hw17.repository.MusicRepository;


public class MusicInformationFragment extends Fragment {


    private static final String ARG_MUSIC_INDEX = "music";

    private Music mMusic;
    private MusicInformationButtonsCallBacks mCallBacks;
    private TextView mMusicName;
    private TextView mMusicArtist;
    private TextView mMusicAlbum;
    private ImageView mPlayPause;
    private ImageView mNext;
    private ImageView mPrevious;
    private ImageView mShuffle;
    private ImageView mMusicPicture;
    private ImageView mBack;
    private MusicRepository mRepository;


    public static MusicInformationFragment newInstance(int musicIndex) {
        MusicInformationFragment fragment = new MusicInformationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MUSIC_INDEX, musicIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = MusicRepository.getInstance();
        if (getArguments() != null) {
            mMusic = mRepository.getMusics().get(getArguments().getInt(ARG_MUSIC_INDEX));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_information, container, false);
        findViews(view);
        updateViews();
        listeners();
        return view;
    }

    private void updateViews() {
        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(mMusic.getFilePath());
        byte[] art = metaRetriver.getEmbeddedPicture();
        if (art != null) {
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(art, 0, art.length);
            mMusicPicture.setImageBitmap(songImage);
        }else {
            mMusicPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_music));
        }
        mMusicName.setText(mMusic.getName());
        mMusicArtist.setText(mMusic.getArtist());
        mMusicAlbum.setText(mMusic.getAlbum());
        playIconCheck();
        shuffleIconChecker();
    }

    private void findViews(View view) {
        mMusicAlbum = view.findViewById(R.id.music_album);
        mMusicArtist = view.findViewById(R.id.music_artist_information);
        mMusicName = view.findViewById(R.id.music_name_information);
        mPlayPause = view.findViewById(R.id.imageView_play);
        mMusicPicture = view.findViewById(R.id.imageView_music_image);
        mNext = view.findViewById(R.id.imageView_next);
        mPrevious = view.findViewById(R.id.imageView_previous);
        mShuffle = view.findViewById(R.id.imageView_shuffle);
        mBack = view.findViewById(R.id.imageView_back);
    }


    private void listeners() {
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBacks.onPlayClick();
                playIconCheck();
            }
        });
        mShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRepository.isShuffle()) {
                    mRepository.setShuffle(false);
                } else {
                    mRepository.setShuffle(true);
                }
                shuffleIconChecker();
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusic = mCallBacks.onNextClick();
                updateViews();
            }
        });
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusic = mCallBacks.onPreviousClick();
                updateViews();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBacks.onBack();
            }
        });

    }

    private void shuffleIconChecker() {
        if (mRepository.isShuffle()) {
            mShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_shufle));
        } else {
            mShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_shufle));
        }
    }


    private void playIconCheck() {
        if (mCallBacks.isMusicPlaying()) {
            mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
        } else {
            mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MusicInformationButtonsCallBacks) {
            mCallBacks = (MusicInformationButtonsCallBacks) context;
        }
    }

    public interface MusicInformationButtonsCallBacks {
        void onPlayClick();

        Music onNextClick();

        Music onPreviousClick();

        void onBack();

        boolean isMusicPlaying();
    }
}