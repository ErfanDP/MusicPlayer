package org.maktab.hw17.controller.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.maktab.hw17.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.single_fragment_container);

        //create an add fragment transaction for CrimeDetailFragment
        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.single_fragment_container, createFragment())
                    .commit();
        }
    }
}
