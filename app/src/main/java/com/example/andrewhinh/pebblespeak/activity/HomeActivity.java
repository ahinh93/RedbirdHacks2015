package com.example.andrewhinh.pebblespeak.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andrewhinh.pebblespeak.R;

/**
 * Created by Alec on 4/25/15.
 */
public class HomeActivity extends Activity {

    ImageView imageStatus, imageLeft, imageRight, imageBottom, imageCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        imageStatus = (ImageView) findViewById(R.id.image_status);
        imageLeft = (ImageView) findViewById(R.id.image_left);
        imageRight = (ImageView) findViewById(R.id.image_right);
        imageBottom = (ImageView) findViewById(R.id.image_bottom);
        imageCenter = (ImageView) findViewById(R.id.image_center);

        imageStatus.setOnClickListener(new ImageOnClickListener());
        imageLeft.setOnClickListener(new ImageOnClickListener());
        imageRight.setOnClickListener(new ImageOnClickListener());
        imageBottom.setOnClickListener(new ImageOnClickListener());
        imageCenter.setOnClickListener(new ImageOnClickListener());
    }

    private class ImageOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.equals(imageStatus))
            {
                Intent i = new Intent(HomeActivity.this, SelectionActivity.class);
            }
            else if(v.equals(imageLeft))
            {
                Intent i = new Intent(HomeActivity.this, SelectionActivity.class);
                startActivity(i);
            }
            else if(v.equals(imageRight))
            {
                Intent i = new Intent(HomeActivity.this, SelectionActivity.class);
                startActivity(i);
            }
            else if(v.equals(imageBottom))
            {
                Intent i = new Intent(HomeActivity.this, SelectionActivity.class);
                startActivity(i);
            }
            else if(v.equals(imageCenter))
            {
                Intent i = new Intent(HomeActivity.this, SelectionActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Oops!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
