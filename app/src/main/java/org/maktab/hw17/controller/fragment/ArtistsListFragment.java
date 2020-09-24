package org.maktab.hw17.controller.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.maktab.hw17.R;
import org.maktab.hw17.model.Artist;
import org.maktab.hw17.model.Music;
import org.maktab.hw17.repository.MusicRepository;

import java.util.List;

public class ArtistsListFragment extends Fragment {
    private MusicRepository mRepository;
    private RecyclerView mRecyclerView;
    private ArtistListAdapter mAdapter;

    public static ArtistsListFragment newInstance() {
        return new ArtistsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = MusicRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.root_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ArtistListAdapter(mRepository.getArtists());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private class ArtistListAdapter extends RecyclerView.Adapter<ArtistListViewHolder>{

        private List<Artist> mArtists;

        public ArtistListAdapter(List<Artist> artists) {
            mArtists = artists;
        }

        @NonNull
        @Override
        public ArtistListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.list_row_artist,parent,false);
            return new ArtistListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ArtistListViewHolder holder, int position) {
            holder.bindViews(mArtists.get(position));
        }

        @Override
        public int getItemCount() {
            return mArtists.size();
        }
    }

    private class ArtistListViewHolder extends RecyclerView.ViewHolder{
        private Artist mArtist;
        private TextView mName;
        private TextView mNumberOfTracks;
        public ArtistListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRepository.getArtistsMusic(mArtist.getName());
                }
            });
            mName = itemView.findViewById(R.id.artist_name);
            mNumberOfTracks = itemView.findViewById(R.id.artist_number_of_tracks);
        }
        public void bindViews(Artist artist){
            mArtist = artist;
            mName.setText(artist.getName());
            mNumberOfTracks.setText(String.valueOf(artist.getNumberOfTracks()));
        }
    }
}