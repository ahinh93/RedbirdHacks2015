package com.example.andrewhinh.pebblespeak.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.andrewhinh.pebblespeak.data.model.C;
import com.example.andrewhinh.pebblespeak.data.model.PebbleService;

/**
 * Created by AndrewHinh on 4/25/2015.
 */
public class YouRepoApplication extends Application {

    private PebbleService mPebbleService;


    //are we bound to the service
    private boolean isBound = false;



    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mPebbleService = ((PebbleService.BluetoothBinder)service).getService();

            Log.i(C.TAG, "Application connected to service.");



}

    @Override
    public void onServiceDisconnected(ComponentName name) {

        doUnbindService();

        mPebbleService = null;

        //Toast.makeText(this, "Activity disconnected from service.", Toast.LENGTH_SHORT).show();

    }
};
    public void onCreate() {

        super.onCreate();

        doBindService();

    }

    @Override
    public void onTerminate() {

//        if (mShadowService != null)
//            stopService(mShadowService);

//        mShadowReceiver.stopScan();

//        unregisterReceiver(mShadowReceiver);
        unbindService(mConnection);

        Log.i(C.TAG,"Terminated");

        super.onTerminate();

    }

    private void doBindService() {

        boolean start = false;

        Intent i = new Intent(getApplicationContext(), PebbleService.class);
        startService(i);
        //the service is bound
        start = bindService(new Intent(this, PebbleService.class), mConnection, Context.BIND_AUTO_CREATE);

        if (!start) {
            Toast.makeText(this, "Bind to BluetoothLeService failed", Toast.LENGTH_SHORT).show();
            //finish();
        }

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
