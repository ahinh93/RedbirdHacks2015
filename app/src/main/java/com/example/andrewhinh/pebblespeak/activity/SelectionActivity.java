package com.example.andrewhinh.pebblespeak.activity;

import com.example.andrewhinh.pebblespeak.R;
import com.example.andrewhinh.pebblespeak.adapter.IntentAdapter;
import com.example.andrewhinh.pebblespeak.data.model.PebbleService;
import com.example.andrewhinh.pebblespeak.util.SystemUiHider;
import com.getpebble.android.kit.PebbleKit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SelectionActivity extends Activity {

    private ListView mainMenu;
    private IntentAdapter listAdapter;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private PebbleService mPebbleService;


    private boolean isBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mPebbleService = ((PebbleService.BluetoothBinder)service).getService();

            //registerReceiver(mBatteryReceiver, new IntentFilter("com.servabosafe.shadow.batterybroadcast"));

            //isRegisterReceived = true;

            //mShadowService.getBatteryLevel();

            //Toast.makeText(getActivity(), "Fragment connected to service.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mPebbleService = null;

            //unregisterReceiver(mBatteryReceiver);

            //Toast.makeText(getActivity(), "Fragment disconnected from service.", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection);

        //tie to .xml variable
        mainMenu = (ListView) findViewById( R.id.mainMenu );


        // Create ArrayAdapter using the planet list.
        listAdapter = new IntentAdapter(SelectionActivity.this, R.layout.item_user);

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));

        String clientId = "my_client_id";
        Intent soundIntent = new Intent("com.soundcloud.android.SHARE")
                .putExtra("com.soundcloud.android.extra.tags", new String[] {
                        "soundcloud:created-with-client-id="+clientId
                });

        listAdapter.add( new Pair<String, Intent> ("Message", sendIntent) );
        listAdapter.add( new Pair<String, Intent> ("Call", new Intent(Intent.ACTION_DIAL)) );
        listAdapter.add( new Pair<String, Intent> ("Sound", soundIntent ));

        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), listAdapter.getItem(position).first, Toast.LENGTH_SHORT).show();
                if (mPebbleService != null)
                    mPebbleService.setBottomIntent(listAdapter.getItem(position).second);
            }
        });

        mainMenu.setAdapter( listAdapter );

        doBindService();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private void doBindService() {

        //the service is bound
        getApplicationContext().bindService(new Intent(SelectionActivity.this, PebbleService.class), mConnection, Context.BIND_AUTO_CREATE);

        //Toast.makeText(SSConnectActivity.this, "Bound from activity", Toast.LENGTH_SHORT).show();

        //our service bound
        isBound = true;

    }

    private void doUnbindService() {

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
            //Toast.makeText(SSConnectActivity.this, "Unbound from activity", Toast.LENGTH_SHORT).show();
        }
    }

}
