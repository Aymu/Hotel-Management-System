package com.example.system;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.File;

public class ShowRoomActivity extends RoomSearch {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_info);
        Intent intent = getIntent();
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
        Cursor info = SY.rawQuery("select * from GuestInfo",null);
        info.moveToFirst();
        int length1 = RoomListActivity.raw;
        for (int i = 1;i<length1;i++){
            info.moveToNext();
        }
        ((TextView)findViewById(R.id.tv_info_id)).setText(info.getString(info.getColumnIndex("ID")));
        ((TextView)findViewById(R.id.tv_info_name)).setText(info.getString(info.getColumnIndex("Name")));
        ((TextView)findViewById(R.id.tv_info_age)).setText(info.getString(info.getColumnIndex("Age")));
        ((TextView)findViewById(R.id.tv_info_sex)).setText(info.getString(info.getColumnIndex("Sex")));
        ((TextView)findViewById(R.id.tv_info_train_date)).setText(info.getString(info.getColumnIndex("Data")));
        ((TextView)findViewById(R.id.tv_info_phone)).setText(info.getString(info.getColumnIndex("Tel")));
    }
    public void goBack(View view) {
        finish();
    }
}
