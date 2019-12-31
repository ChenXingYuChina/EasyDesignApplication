package cn.edu.hebut.easydesign.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.R;


public class huojianFragment extends Fragment {
    private static huojianFragment huojianFragment;
    public static huojianFragment getShubiaoFragment(){
        if(huojianFragment == null){
            huojianFragment = new huojianFragment();
            return huojianFragment;
        }
        else {
            return huojianFragment;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_huojian ,container, false);
        return view;
    }

    ;
}
