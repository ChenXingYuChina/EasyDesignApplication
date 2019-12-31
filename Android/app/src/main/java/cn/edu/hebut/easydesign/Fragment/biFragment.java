package cn.edu.hebut.easydesign.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.R;


public class biFragment extends Fragment {
    private static biFragment biFragment;
    public static biFragment biFragment(){
        if(biFragment == null){
            biFragment = new biFragment();
            return biFragment;
        }
        else {
            return biFragment;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bi ,container, false);
        return view;
    }

    ;
}
