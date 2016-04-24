package edu.westga.jeffrichardsrollcall.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.westga.jeffrichardsrollcall.R;

public class ClassListViewAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    public ClassListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clear() {
        this.list.clear();
    }

    public void add(String name, String classes, String percentage) {
        HashMap<String,String> temp = new HashMap<>();
        temp.put("first", name);
        temp.put("second", classes);
        temp.put("third", percentage);
        list.add(temp);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        if(convertView == null){
            convertView=inflater.inflate(R.layout.class_listview, null);
            txtFirst=(TextView) convertView.findViewById(R.id.name);
            txtSecond=(TextView) convertView.findViewById(R.id.classes);
            txtThird=(TextView) convertView.findViewById(R.id.percentage);

        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get("first"));
        txtSecond.setText(map.get("second"));
        txtThird.setText(map.get("third"));

        return convertView;
    }

}