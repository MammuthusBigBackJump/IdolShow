package com.nd.idolshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nd.idolshow.camera.ui.CameraActivity;
import com.unity3d.player.UnityPlayer;

public class MainActivity extends UnityPlayerActivity {

    private Button btnBack;
    private Button btnNext;

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View plyaerView = mUnityPlayer.getView();
        LinearLayout unity_layout = (LinearLayout) findViewById(R.id.unity_layout);
        unity_layout.addView(plyaerView);

        btnBack = (Button) findViewById(R.id.btn_back);
        btnNext = (Button) findViewById(R.id.btn_next);
        editText = (EditText) findViewById(R.id.ed_view);

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                UnityPlayer.UnitySendMessage("unitychan", "ShowBack", "");
            	startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (editText.getText().toString().trim() != null){
//                    UnityPlayer.UnitySendMessage("man_body", "ChangeColor", editText.getText().toString().trim());
//                    Toast.makeText(MainActivity.this,editText.getText().toString().trim(),Toast.LENGTH_SHORT).show();
//                }
                UnityPlayer.UnitySendMessage("test_man", "Change", "");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        UnityPlayer.UnitySendMessage("man_body", "Change", "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
