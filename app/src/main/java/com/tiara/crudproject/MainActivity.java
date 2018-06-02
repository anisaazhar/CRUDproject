package com.tiara.crudproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    AQuery aq;
    ListView list;
    ArrayList<ModelPengguna> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.list_pengguna);
        aq = new AQuery(MainActivity.this);

        GetDataPengguna();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Dialog d_insert = new Dialog(MainActivity.this);
                d_insert.setContentView(R.layout.dialog_insert);

                final EditText addname = (EditText) d_insert.findViewById(R.id.editname);
                final EditText addalamat = (EditText) d_insert.findViewById(R.id.editalamat);
                final EditText addhp = (EditText) d_insert.findViewById(R.id.edithp);
                Button btambah = (Button) d_insert.findViewById(R.id.btntambah);

                btambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String get_nama = addname.getText().toString();
                        String get_alamat = addname.getText().toString();
                        String get_hp = addname.getText().toString();

                        ActionSend(get_nama,get_alamat,get_hp);

                        d_insert.dismiss();
                    }
                });

               d_insert.show();

            }
        });
    }

    private void ActionSend(String get_nama, String get_alamat, String get_hp) {
        String url = "http://192.168.100.24/Server_CI,index.php/api/insert_pengguna";

        HashMap<String,String> params = new HashMap<>();
        params.put("nama",get_nama);
        params.put("alamat",get_alamat);
        params.put("hp",get_hp);

        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        aq.progress(progress).ajax(url,String.class,new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object!= null){
                    try {
                        JSONObject json = new JSONObject(object);
                        String pesan = json.getString("pesan");
                        String result = json.getString("result");

                        if (result.equalsIgnoreCase("true")){
                            Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } );

    }

    //DARI SINII
    private void GetDataPengguna() {
        String url_get = "http://192.168.100.24/Server_CI/index.php/api/get_pengguna";
        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Loading please to patient ya ....");
        progress.setCancelable(false);

        //mengambil data
        aq.progress(progress).ajax(url_get,String.class,new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object != null){
                    try {
                        JSONObject json = new JSONObject(object);
                        String result = json.getString("result");
                        String pesan = json.getString("pesan");

                        if (result.equalsIgnoreCase("true")){
                            JSONArray jarray = json.getJSONArray("data");
                            for (int a = 0; a < jarray.length();a++){
                                JSONObject b = jarray.getJSONObject(a);
                                ModelPengguna m = new ModelPengguna();
                                m.setId_pengguna(b.getString("id_pengguna"));
                                m.setNama_pengguna(b.getString("nama_pengguna"));
                                m.setAlamat_pengguna("alamat_pengguna");
                                m.setHp_pengguna(b.getString("hp_pengguna"));

                                data.add(m);

                                setListView(data);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setListView(final ArrayList<ModelPengguna> data) {
        CustomListView adapter = new CustomListView(this,data);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog d_edit = new Dialog(MainActivity.this);
                d_edit.setContentView(R.layout.dialog_edit);

                final EditText editnama = (EditText) d_edit.findViewById(R.id.edittextname);
                final EditText editalamat = (EditText) d_edit.findViewById(R.id.edittextalamat);
                final EditText edithp = (EditText) d_edit.findViewById(R.id.edittexthp);
                final EditText editid = (EditText) d_edit.findViewById(R.id.edittextid);
                Button btnhapus = (Button) d_edit.findViewById(R.id.btnhapus);
                Button btnedit = (Button) d_edit.findViewById(R.id.btntambah);

                ModelPengguna rec = data.get(position);
                editid.setText(rec.getId_pengguna());
                editnama.setText(rec.getNama_pengguna());
                editalamat.setText(rec.getAlamat_pengguna());
                edithp.setText(rec.getHp_pengguna());

                btnedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String get_id = editid.getText().toString();
                        String get_nama = editnama.getText().toString();
                        String get_alamat = editalamat.getText().toString();
                        String get_hp = edithp.getText().toString();

                        ActionEdit(get_id,get_nama,get_alamat,get_hp);

                        d_edit.dismiss();
                    }
                });


                btnhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_id = editid.getText().toString();

                ActionHapus(get_id);
                d_edit.dismiss();
            }
        });

        d_edit.show();
    }
});
    }

    private void ActionHapus(String get_id) {
        String url ="http://192.168.100.24/Server_CI/index.php/api/hapus_pengguna/"+get_id;

        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        aq.progress(progress).ajax(url,String.class,new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object!= null){
                    try {
                        JSONObject json = new JSONObject(object);
                        String pesan = json.getString("pesan");
                        String result = json.getString("result");

                        if (result.equalsIgnoreCase("true")){
                            Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }

    private void ActionEdit(String get_id, String get_nama, String get_alamat, String get_hp) {
        String url = "http://192.168.100.24/Server_CI/index.php/api/update_pengguna";

        HashMap<String,String> params = new HashMap<>();
        params.put("id", get_id);
        params.put("nama",get_nama);
        params.put("alamat",get_alamat);
        params.put("hp",get_hp);


        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        aq.progress(progress).ajax(url,String.class,new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object!= null){
                    try {
                        JSONObject json = new JSONObject(object);
                        String pesan = json.getString("pesan");
                        String result = json.getString("result");

                        if (result.equalsIgnoreCase("true")){
                            Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
