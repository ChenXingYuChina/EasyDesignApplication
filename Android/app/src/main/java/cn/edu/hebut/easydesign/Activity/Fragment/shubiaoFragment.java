package cn.edu.hebut.easydesign.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.R;


public class shubiaoFragment extends Fragment {
    private static shubiaoFragment shubiaoFragment;
    public static shubiaoFragment getShubiaoFragment(){
        if(shubiaoFragment == null){
            shubiaoFragment = new shubiaoFragment();
            return shubiaoFragment;
        }
        else {
            return shubiaoFragment;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shubiao ,container, false);
        return view;
    }

    ;
}
