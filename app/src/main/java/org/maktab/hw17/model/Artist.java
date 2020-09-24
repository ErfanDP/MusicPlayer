package org.maktab.hw17.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Artist {
    private List<Music> mMusics = new ArrayList<>();
    private String mName;

    public Artist(String name) {
        mName = name;
    }

    public List<Music> getMusics() {
        return mMusics;
    }

    public void addMusics(Music music) {
        mMusics.add(music);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumberOfTracks() {
        return mMusics.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(mName, artist.mName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mName);
    }
}
