package cn.edu.hebut.easydesign.Activity.UserInformation.Editor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ArrayRes;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;

public class TwoSpinnerHelper {
    private Spinner father, child;
    private List<String[]> childrenLabel;
    private int selectedFather;
    private int selectedChild;
    private childAdapter adapter;
    private String[] fatherLabel;

    TwoSpinnerHelper(Spinner father, @ArrayRes int fatherArrays, Spinner child, /*@ArrayRes*/ int[] childrenArrays) {
        this.father = father;
        this.child = child;
        fatherLabel = ContextHolder.getContext().getResources().getStringArray(fatherArrays);
        childrenLabel = new ArrayList<>(childrenArrays.length);
        for (int r : childrenArrays) {
            childrenLabel.add(ContextHolder.getContext().getResources().getStringArray(r));
        }
        adapter = new childAdapter(childrenLabel.get(0));
        child.setAdapter(adapter);
        setupListener();
    }

    TwoSpinnerHelper(Spinner father, String[] fatherLabel, Spinner child, List<String[]> childrenLabel) {
        this.childrenLabel = childrenLabel;
        this.fatherLabel = fatherLabel;
        this.father = father;
        this.child = child;
        adapter = new childAdapter(childrenLabel.get(0));
        child.setAdapter(adapter);
        setupListener();
    }

    private void setupListener() {

        father.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFather = position;
                selectedChild = 0;
                adapter.changeData(childrenLabel.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedChild = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    static class childAdapter extends BaseAdapter {
        String[] data;

        childAdapter(String[] data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(ContextHolder.getContext());
            textView.setText(data[position]);
            return textView;
        }

        void changeData(String[] newData) {
            data = newData;
            notifyDataSetChanged();
        }
    }

    public int getSelectedChild() {
        return selectedChild;
    }

    public int getSelectedFather() {
        return selectedFather;
    }

    public String getSelectedFatherString() {
        return fatherLabel[selectedFather];
    }

    public String getSelectedChildString() {
        return childrenLabel.get(selectedFather)[selectedChild];
    }

    public void setSelection(int fatherPosition, int childPosition) {
        father.setSelection(fatherPosition);
        child.setSelection(childPosition);
        selectedFather = fatherPosition;
        selectedChild = childPosition;
    }
}
