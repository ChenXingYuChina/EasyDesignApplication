package com.example.dachuangxiangmu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.dachuangxiangmu.R;

public class weixinFragment extends Fragment {
    private static weixinFragment weixinFragment;
    public static weixinFragment getWeixinFragment(){
        if(weixinFragment == null){
            weixinFragment = new weixinFragment();
            return weixinFragment;
        }
        else {
            return weixinFragment;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weixin ,container, false);
        return view;
    }

    ;
}
