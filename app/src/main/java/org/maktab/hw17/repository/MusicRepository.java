package org.maktab.hw17.repository;

import org.maktab.hw17.model.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicRepository {
    private static MusicRepository sRepository;
    private List<Music> mMusics = new ArrayList<>();

    public static MusicRepository getInstance() {
        if(sRepository == null){
            sRepository = new MusicRepository();
        }
        return sRepository;
    }

    private MusicRepository() { }

    public List<Music> getMusics() {
        return mMusics;
    }

    public void setMusics(List<Music> musics) {
        mMusics = musics;
    }
}
