package org.maktab.music.repository;

import org.maktab.music.model.Artist;
import org.maktab.music.model.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicRepository {
    private static MusicRepository sRepository;
    private boolean mShuffle =false;

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


    public boolean isShuffle() {
        return mShuffle;
    }

    public void setShuffle(boolean shuffle) {
        mShuffle = shuffle;
    }


    public Music nextMusic(int musicIndex){
        if(mShuffle) {
            Random random = new Random();
            return mMusics.get(random.nextInt(mMusics.size()-1));
        } else{
            return mMusics.get(musicIndex+1);
        }
    }

    public Music previousMusic(int musicIndex){
        return mMusics.get(musicIndex-1);
    }

    public List<Artist> getArtists(){
        List<Artist> artists = new ArrayList<>();
        for (Music music:mMusics) {
            Artist artist = new Artist(music.getArtist());
            if(artists.contains(artist)){
                artists.get(artists.indexOf(artist)).addMusics(music);
            }else{
                artist.addMusics(music);
                artists.add(artist);
            }
        }
        return artists;
    }


    public List<Music> getArtistsMusic(String name){
        List<Music> musics = new ArrayList<>();
        for (Music music :mMusics) {
            if(music.getArtist().equals(name)){
                musics.add(music);
            }
        }
        return musics;
    }
}

