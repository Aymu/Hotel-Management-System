package com.example.system;

import RoomDBHelper.RoomDBHelper;
import TableContanst.TableContanst;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.File;

public class RoomSearch extends Activity implements OnClickListener {
    private EditText nameText;
    private Button button;
    private Button reButton;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private Button returnButton;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        nameText = (EditText) findViewById(R.id.et_srarch);
        layout=(LinearLayout) findViewById(R.id.linersearch);
        button = (Button) findViewById(R.id.bn_sure_search);
        reButton = (Button) findViewById(R.id.bn_return);
        listView = (ListView) findViewById(R.id.searchListView);
        returnButton = (Button) findViewById(R.id.return_id);

        reButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            reButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);

            String name = nameText.getText().toString();

            final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/com/cn21/account/public";
            //数据库名
            final String DATABASE_FILENAME = "hotelinfo.db";
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists()){
                dir.mkdirs();
            }
            final SQLiteDatabase SY = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);

            Cursor info = SY.rawQuery("select Name as _id, Sex,Age,Tel,Data,ID from GuestInfo where Name = ?",new String[]{name});

            if (!info.moveToFirst()) {
                Toast.makeText(this, "没有所查房间信息！", Toast.LENGTH_SHORT).show();
            } else
                //如果有所查询的信息，则将查询结果显示出来
                adapter = new SimpleCursorAdapter(this, R.layout.room_list,
                        info, new String[] {"_id", "Sex", "Age", "Tel", "Data", "ID"},
                        new int[] {R.id.tv_stu_name, R.id.tv_stu_sex, R.id.tv_stu_age, R.id.tv_stu_tel, R.id.tv_stu_date,R.id.tv_stu_id},0);
                listView.setAdapter(adapter);
            }else if(v==reButton|v==returnButton){
            finish();
        }
    }
}
