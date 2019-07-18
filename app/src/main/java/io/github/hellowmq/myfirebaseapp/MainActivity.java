package io.github.hellowmq.myfirebaseapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FIRE_BASE";
    //实时数据库
    private EditText mDbEdit;
    private TextView mShowValueText;
    private Button mDbSetValueBtn;
    private TextView mRealTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        database();
    }

    /**
     * 初始化各种控件
     */
    private void initView() {
        mDbEdit = (EditText) findViewById(R.id.et_value);
        mShowValueText = (TextView) findViewById(R.id.tv_db_value);
        mDbSetValueBtn = (Button) findViewById(R.id.btn_db_set);
        mRealTimeText = (TextView) findViewById(R.id.tv_realtime_text);
    }

    /**
     * 实时数据库功能
     */
    private void database() {
        //获取数据库实例
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //获取Reference  "message"为存储值的key
        final DatabaseReference myRef = database.getReference("MyOnlineData");

        mDbSetValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mDbEdit.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
//                    showToast("还没输入要存储的内容嘞");
                    Snackbar.make(view, "还没输入要存储的内容嘞", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    return;
                }
                //为实时数据库存值
                myRef.setValue(str);
            }
        });
        //注册当值发生改变时的事件监听
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                mShowValueText.setText("显示：" + value);
                mRealTimeText.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.i("FirebaseDatabase","h获取数据异常");
                Log.e(TAG, error.toException().toString());
            }
        });
    }


}
