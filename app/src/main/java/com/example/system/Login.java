package com.example.system;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class Login extends AppCompatActivity {

    private TextView title;
    private EditText accounts;
    private EditText password;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/com/cn21/account/public";
        //数据库名
        final String DATABASE_FILENAME = "systeminfo.db";
        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        final SQLiteDatabase SY = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);

        accounts = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的数据
                String strUsername = accounts.getText().toString();
                String strPassword = password.getText().toString();
                Cursor account = SY.rawQuery("select accounts from User",null);
                Cursor password = SY.rawQuery("select password from User",null);
                account.moveToFirst();
                password.moveToFirst();
                int state = 1;
                for(int i=0;i<account.getCount();i++){
                    if(strUsername.equals(account.getString(account.getColumnIndex("accounts"))) && strPassword.equals(password.getString(password.getColumnIndex("password")))){
                        //Intent
                        Intent intent = new Intent(Login.this,RoomListActivity.class);
                        startActivity(intent);
                    }else {
                        account.moveToNext();
                        password.moveToNext();
                        state = 0;
                    }
                }
                if(state == 0){
                    Toast.makeText(Login.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                }
                //判断用户名和密码是否正确


            }
        });

        register = (Button) findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent
                Intent intent = new Intent(Login.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}
