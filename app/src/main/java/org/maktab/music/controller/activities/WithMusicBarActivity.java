package org.maktab.music.controller.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.maktab.music.R;
import org.maktab.music.controller.fragment.MusicBarFragment;
import org.maktab.music.controller.fragment.MusicInformationFragment;
import org.maktab.music.controller.fragment.MusicPagerFragment;
import org.maktab.music.controller.fragment.TrackListFragment;
import org.maktab.music.model.Music;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WithMusicBarActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,TrackListFragment.ListCallBacks, MusicInformationFragment.MusicInformationButtonsCallBacks, MusicBarFragment.MusicBarCallBacks {

    private static final int RC_STORAGE = 1;
    private View mLayoutMusicBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_music_bar);
        findViews();
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            methodRequiresPermission();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.perm_request),
                    RC_STORAGE, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, R.string.perm_request, Toast.LENGTH_SHORT)
                    .show();
            recreate();
        }
    }

    @AfterPermissionGranted(RC_STORAGE)
    private void methodRequiresPermission() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        methodRequiresPermission();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

        Toast.makeText(this, R.string.perm_request, Toast.LENGTH_SHORT)
                .show();

    }


}