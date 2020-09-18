package org.maktab.hw17.controller.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.maktab.hw17.R;
import org.maktab.hw17.model.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrackListFragment extends Fragment {

    private ListCallBacks mCallBacks;
    private RecyclerView mRecyclerView;


    public static TrackListFragment newInstance() {
        return new TrackListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ListCallBacks)
            mCallBacks = (ListCallBacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        findViews(view);
        List<Music> songs = gettingMusicList();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new TrackAdapter(songs));

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.trackList_recycler_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private List<Music> gettingMusicList() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        List<Music> songs;
        try (Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)) {

            songs = new ArrayList<>();
            Objects.requireNonNull(cursor).moveToFirst();
            while (!cursor.isAfterLast()) {
                songs.add(new Music(cursor.getString(0)
                        , cursor.getString(1)
                        , cursor.getString(2)
                        , cursor.getString(3)
                        , cursor.getString(4)));
                cursor.moveToNext();
            }
        }
        return songs;
    }

    private class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
        private List<Music> mMusicList;

        public TrackAdapter(List<Music> musicList) {
            mMusicList = musicList;
        }

        public void setMusicList(List<Music> musicList) {
            mMusicList = musicList;
        }

        @NonNull
        @Override
        public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.list_row_track, parent, false);
            return new TrackViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
            holder.bindViews(mMusicList.get(position));
        }

        @Override
        public int getItemCount() {
            return mMusicList.size();
        }

        private class TrackViewHolder extends RecyclerView.ViewHolder {
            private Music mMusic;
            private TextView mTitle;
            private TextView mArtist;
            private TextView mDuration;
            private TextView mAlbum;
            private ImageView mImage;

            public TrackViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallBacks.onMusicClick(mMusic);
                    }
                });
                mTitle = itemView.findViewById(R.id.row_music_name);
                mArtist = itemView.findViewById(R.id.row_music_artist);
                mAlbum = itemView.findViewById(R.id.row_music_albume);
                mDuration = itemView.findViewById(R.id.row_music_duration);
                mImage = itemView.findViewById(R.id.row_imageView_music_image);
            }

            public void bindViews(Music music) {
                mMusic = music;
                mTitle.setText(music.getName());
                mAlbum.setText(music.getAlbum());
                mDuration.setText(getTime(Long.valueOf(music.getDuration())));
                mArtist.setText(music.getArtist());
                MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
                metaRetriver.setDataSource(music.getFilePath());
                byte[] art = metaRetriver.getEmbeddedPicture();
                if(art !=null) {
                    Bitmap songImage = BitmapFactory
                            .decodeByteArray(art, 0, art.length);
                    mImage.setImageBitmap(songImage);
                }

            }

            private String getTime(long duration) {
                String minutes = String.valueOf((duration / 1000) / 60);
                String seconds = String.valueOf((int) ((duration / 1000) % 60));
                return minutes + ":" + seconds;
            }
        }
    }

    public interface ListCallBacks {
        void onMusicClick(Music music);
    }
}