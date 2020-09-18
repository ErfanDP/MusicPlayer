package org.maktab.hw17.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.maktab.hw17.R;
import org.maktab.hw17.model.Music;


public class MusicInformationFragment extends Fragment {


    private static final String ARG_MUSIC = "music";

    private Music mMusic;

    public static MusicInformationFragment newInstance(Music music) {
        MusicInformationFragment fragment = new MusicInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MUSIC, music);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMusic = (Music) getArguments().getSerializable(ARG_MUSIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_music_information, container, false);
        return view;
    }
}