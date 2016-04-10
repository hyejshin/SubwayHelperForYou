package com.example.hyejung.easysubway.popupviews.subwaylistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.cellRow.SubwayLineListRow;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import java.util.ArrayList;

public class SubwayLineListActivity extends Activity implements OnClickListener, OnEditorActionListener {
    AppDataManager appManager = null;
    ArrayAdapter<Bundle> listData = null;
    EditText searchText = null;
    ArrayList<Bundle> stationList = null;

    public class StationListAdapter extends ArrayAdapter<Bundle> {
        Activity context;

        public StationListAdapter(Activity context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            SubwayLineListRow rowView;
            View cell = convertView;
            if (cell == null || cell.getId() != R.layout.cell_subway_line_list_row) {
                cell = this.context.getLayoutInflater().inflate(R.layout.cell_subway_line_list_row, null);
                rowView = new SubwayLineListRow(cell);
                cell.setTag(rowView);
                cell.setId(R.layout.cell_subway_line_list_row);
            } else {
                rowView = (SubwayLineListRow) cell.getTag();
            }
            rowView.setCellData(this.context, (Bundle) SubwayLineListActivity.this.stationList.get(position));
            return cell;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1 && requestCode == 100) {
            int rFlag = intent.getIntExtra("resFlag", 0);
            if (rFlag == 1) {
                finish();
            } else if (rFlag == 2) {
                setResult(-1, intent);
                finish();
                overridePendingTransition(0, 0);
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_line_list_view);
        this.appManager = AppDataManager.shared();
        initTableBar();
        initSearchBar();
        getStationList();
        initListView();
    }

    private void initTableBar() {
        String strTitle;
        LinearLayout titlebar = (LinearLayout) findViewById(R.id.titleBer);
        switch (this.appManager.getAreaCode()) {
            case 1:
                strTitle = "\uc218\ub3c4\uad8c\uc9c0\ud558\ucca0";
                break;
            case 2:
                strTitle = "\ubd80\uc0b0\uc9c0\ud558\ucca0";
                break;
            case 3:
                strTitle = "\ub300\uad6c\uc9c0\ud558\ucca0";
                break;
            case 4:
                strTitle = "\uad11\uc8fc\uc9c0\ud558\ucca0";
                break;
            case 5:
                strTitle = "\ub300\uc804\uc9c0\ud558\ucca0";
                break;
            default:
                strTitle = "\uc9c0\ud558\ucca0\ub9ac\uc2a4\ud2b8";
                break;
        }
        View barIndicator = LayoutInflater.from(this).inflate(R.layout.bar_titlebar_3button, null);
        ((TextView) barIndicator.findViewById(R.id.title)).setText(strTitle);
        Button button1 = (Button) barIndicator.findViewById(R.id.navi_titlebar_3button1);
        button1.setText("\ub2eb\uae30");
        button1.setOnClickListener(this);
        titlebar.addView(barIndicator);
    }

    private void initSearchBar() {
        ((Button) findViewById(R.id.search_button)).setOnClickListener(this);
        this.searchText = (EditText) findViewById(R.id.search_edit_text);
        this.searchText.setOnEditorActionListener(this);
    }

    private void getStationList() {
        if (this.stationList == null) {
            this.stationList = new ArrayList();
        } else {
            this.stationList.clear();
        }
        ArrayList<Bundle> arrList = this.appManager.getArrLineInfoAll();
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
                Bundle bundle = (Bundle) SubwayLineListActivity.this.stationList.get(position);
                int lineCode = Integer.parseInt(bundle.getString("lineCode"));
                String lineName = bundle.getString("lineName");
                Intent intent = new Intent(SubwayLineListActivity.this, SubwayStationListActivity.class);
                intent.putExtra("lineCode", lineCode);
                intent.putExtra("lineName", lineName);
                SubwayLineListActivity.this.startActivityForResult(intent, 100);
                SubwayLineListActivity.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.navi_titlebar_3button1) {
            cmdTableBer_Button1();
        } else if (id == R.id.search_button) {
            cmdSearchButton();
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.search_edit_text && actionId == 3) {
            cmdSearchButton();
        }
        return false;
    }

    private void cmdTableBer_Button1() {
        finish();
    }

    private void cmdSearchButton() {
        String keyword = null;
        Editable editable = this.searchText.getText();
        if (editable != null) {
            keyword = editable.toString();
        }
        if (keyword.length() > 0) {
            Intent intent = new Intent(this, SubwayStationListActivity.class);
            intent.putExtra("lineCode", 0);
            intent.putExtra("keyword", keyword);
            startActivityForResult(intent, 100);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            return;
        }
        Toast.makeText(this, "\uc5ed\uc774\ub984\uc744 \uc785\ub825\ud574\uc8fc\uc138\uc694.", 0).show();
    }
}
