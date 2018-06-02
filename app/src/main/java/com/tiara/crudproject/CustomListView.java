package com.tiara.crudproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by smp_i on 3/30/2017.
 */

public class CustomListView extends BaseAdapter {
    Activity mainActivity;
    ArrayList <ModelPengguna> data;
    TextView txtnama,txtalamat,txthp;

    public CustomListView(Activity mainActivity, ArrayList<ModelPengguna> data) {
        this.mainActivity = mainActivity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.mainActivity.getSystemService(this.mainActivity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);

            txtnama = (TextView) convertView.findViewById(R.id.textnama);
            txtalamat = (TextView) convertView.findViewById(R.id.textalamat);
            txthp = (TextView) convertView.findViewById(R.id.texthp);
        }
        ModelPengguna rec = data.get(position);
        txtnama.setText(rec.getNama_pengguna());
        txtalamat.setText(rec.getAlamat_pengguna());
        txthp.setText(rec.getHp_pengguna());

        return convertView;
    }
}
