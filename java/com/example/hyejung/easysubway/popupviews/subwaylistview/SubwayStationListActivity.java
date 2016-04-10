package com.example.hyejung.easysubway.popupviews.subwaylistview;

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
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.cellRow.SubwayStationListRow;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import java.util.ArrayList;

public class SubwayStationListActivity extends Activity implements OnClickListener {
    AppDataManager appManager = null;
    int lineCode = 0;
    ArrayAdapter<Bundle> listData = null;
    ArrayList<Bundle> stationList = null;

    public class StationListAdapter extends ArrayAdapter<Bundle> {
        Activity context;

        public StationListAdapter(Activity context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            SubwayStationListRow rowView;
            View cell = convertView;
            if (cell == null || cell.getId() != R.layout.cell_subway_station_list_row) {
                cell = this.context.getLayoutInflater().inflate(R.layout.cell_subway_station_list_row, null);
                rowView = new SubwayStationListRow(cell);
                cell.setTag(rowView);
                cell.setId(R.layout.cell_subway_station_list_row);
            } else {
                rowView = (SubwayStationListRow) cell.getTag();
            }
            rowView.setCellData(this.context, (Bundle) SubwayStationListActivity.this.stationList.get(position));
            return cell;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_list_view);
        this.appManager = AppDataManager.shared();
        Intent intent = getIntent();
        this.lineCode = intent.getIntExtra("lineCode", 0);
        if (this.lineCode > 0) {
            initTableBar(intent.getStringExtra("lineName"));
            getStationList();
        } else {
            initTableBar("\uac80\uc0c9\uacb0\uacfc");
            getSearchStationList(intent.getStringExtra("keyword"));
        }
        initListView();
    }

    private void initTableBar(String lineName) {
        if (lineName == null) {
            lineName = "\uc5ed \ub9ac\uc2a4\ud2b8";
        }
        LinearLayout titlebar = (LinearLayout) findViewById(R.id.titleBer);
        View barIndicator = LayoutInflater.from(this).inflate(R.layout.bar_titlebar_2button, null);
        ((TextView) barIndicator.findViewById(R.id.title)).setText(lineName);
        Button button1 = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button1);
        button1.setText("\ub4a4\ub85c");
        button1.setOnClickListener(this);
        Button button2 = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button2);
        button2.setText("\ub2eb\uae30");
        button2.setOnClickListener(this);
        titlebar.addView(barIndicator);
    }

    private void getStationList() {
        if (this.stationList == null) {
            this.stationList = new ArrayList();
        } else {
            this.stationList.clear();
        }
        ArrayList<Bundle> arrList = this.appManager.getArrStationInfoList(this.lineCode);
        if (arrList.size() > 0) {
            this.stationList.addAll(arrList);
        }
        arrList.clear();
    }

    private void getSearchStationList(String text) {
        if (this.stationList == null) {
            this.stationList = new ArrayList();
        } else {
            this.stationList.clear();
        }
        ArrayList<Bundle> arrList = this.appManager.getSreachStationInfoList(text);
        if (arrList.size() > 0) {
            this.stationList.addAll(arrList);
        }
        arrList.clear();
    }

    private void initListView() {
        this.listData = new StationListAdapter(this, R.layout.cell_subway_station_list_row, this.stationList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(this.listData);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String stationCode = ((Bundle) SubwayStationListActivity.this.stationList.get(position)).getString("stationCode");
                Intent intent = SubwayStationListActivity.this.getIntent();
                intent.putExtra("resFlag", 2);
                intent.putExtra("stationCode", stationCode);
                SubwayStationListActivity.this.setResult(-1, intent);
                SubwayStationListActivity.this.finish();
                SubwayStationListActivity.this.overridePendingTransition(0, 0);
            }
        });
    }

    public void onBackPressed() {
        cmdTableBer_Button1();
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
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    private void cmdTableBer_Button2() {
        Intent intent = getIntent();
        intent.putExtra("resFlag", 1);
        setResult(-1, intent);
        finish();
    }
}
