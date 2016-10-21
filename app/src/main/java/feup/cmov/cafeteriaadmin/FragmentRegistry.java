package feup.cmov.cafeteriaadmin;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;

public class FragmentRegistry {

    HashMap<String, Fragment> fragments;
    FragmentManager fragmentManager;

    public FragmentRegistry(FragmentManager manager){
        this.fragments = new HashMap<>();
        this.fragmentManager = manager;
    }

    public FragmentRegistry(FragmentManager manager, HashMap<String, Fragment> fragments){
        this.fragments = fragments;
        this.fragmentManager = manager;
    }

    public void addFragment(String key, Fragment object){
        this.fragments.put(key, object);
    }

    public void openFragment(String key)
    {
        Fragment fragment = fragments.get(key);
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }
}
