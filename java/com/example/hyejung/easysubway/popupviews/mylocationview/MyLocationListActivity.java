package com.example.hyejung.easysubway.popupviews.mylocationview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.cellRow.MyLocationListRow;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.process.SearchLocationManager;
import com.example.hyejung.easysubway.process.SearchLocationManager.SearchLocationListener;
import java.util.ArrayList;

//내 현재 위치에서 주변역 탐색-> 리스트 창
public class MyLocationListActivity extends Activity implements OnClickListener {
    AppDataManager appManager = null;
    ArrayList<Bundle> arrayList = null;
    int iModeFlag = 0;
    ArrayAdapter<Bundle> listData = null;
    SearchLocationManager locationManager = null;
    Button modeFlagButton = null;

    public class arrayListAdapter extends ArrayAdapter<Bundle> {
        Activity context;

        public arrayListAdapter(Activity context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            MyLocationListRow rowView;
            View cell = convertView;
            if (cell == null || cell.getId() != R.layout.cell_my_location_list_row) {
                cell = this.context.getLayoutInflater().inflate(R.layout.cell_my_location_list_row, null);
                rowView = new MyLocationListRow(cell);
                cell.setTag(rowView);
                cell.setId(R.layout.cell_my_location_list_row);
            } else {
                rowView = (MyLocationListRow) cell.getTag();
            }
            rowView.setCellData(this.context, (Bundle) MyLocationListActivity.this.arrayList.get(position));
            return cell;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_list_view);
        this.appManager = AppDataManager.shared();
        initTableBar();
        initListView();
        myLocationUpdates();
    }

    private void initTableBar() {
        LinearLayout titlebar = (LinearLayout) findViewById(R.id.titleBer);
        View barIndicator = LayoutInflater.from(this).inflate(R.layout.bar_titlebar_2button, null);
        ((TextView) barIndicator.findViewById(R.id.title)).setText("\uc8fc\ubcc0\uc5ed \uac80\uc0c9");
        Button button1 = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button1);
        button1.setText("\ud604\uc704\uce58");
        button1.setOnClickListener(this);
        Button button2 = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button2);
        button2.setText("\ub2eb\uae30");
        button2.setOnClickListener(this);
        titlebar.addView(barIndicator);
    }

    private void initListView() {
        if (this.arrayList == null) {
            this.arrayList = new ArrayList();
        } else {
            this.arrayList.clear();
        }
        this.listData = new arrayListAdapter(this, 0, this.arrayList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(this.listData);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle bundle = (Bundle) MyLocationListActivity.this.arrayList.get(position);
                Intent intent = MyLocationListActivity.this.getIntent();
                String stationCode = bundle.getString("stationCode");
                intent.putExtra("resFlag", 2);
                intent.putExtra("stationCode", stationCode);
                MyLocationListActivity.this.setResult(-1, intent);
                MyLocationListActivity.this.finish();
                MyLocationListActivity.this.overridePendingTransition(0, 0);
            }
        });
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.navi_titlebar_2button1) {
            cmdTableBer_Button1();
        } else if (id == R.id.navi_titlebar_2button2) {
            cmdTableBer_Button2();
        }
    }

    private void cmdTableBer_Button1() {
        if (this.listData != null) {
            this.arrayList.clear();
            this.listData.notifyDataSetChanged();
            ((ListView) findViewById(R.id.listView)).setSelection(0);
        }
        myLocationUpdates();
    }

    private void cmdTableBer_Button2() {
        finish();
    }

    private void myLocationUpdates() {
        if (this.locationManager == null) {
            this.locationManager = new SearchLocationManager();
            this.locationManager.initLocation(this);
        }
        this.locationManager.setSearchLocationListener(new SearchLocationListener() {
            public void onSearchLocationListener(ArrayList<Bundle> resData) {
                if (resData == null || resData.size() <= 0) {
                    Toast.makeText(MyLocationListActivity.this, "\uc8fc\ubcc0\uc5d0 \uac00\uae4c\uc6b4 \uc5ed\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.", 0).show();
                } else {
                    MyLocationListActivity.this.arrayList.addAll(resData);
                    MyLocationListActivity.this.listData.notifyDataSetChanged();
                }
            }
        });
        this.locationManager.searchLocationUpdates(25);
    }
}
