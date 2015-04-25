package com.example.andrewhinh.pebblespeak.data.model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

/**
 * Created by AndrewHinh on 4/25/2015.
 */
public class PebbleService extends Service {

    private static PebbleService mThis = null;

    public static PebbleService getInstance() {
        return mThis;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Launching my app
        PebbleKit.startAppOnPebble(getApplicationContext(), C.APP_ID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean connected = PebbleKit.isWatchConnected(getApplicationContext());

        Log.i(C.TAG, "Pebble is " + (connected ? "connected" : "not connected"));

        if (connected) {

            PebbleKit.registerPebbleConnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(getApplicationContext(), "Pebble connected!", Toast.LENGTH_SHORT).show();
                }

            });

            PebbleKit.registerPebbleDisconnectedReceiver(getApplicationContext(), new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(getApplicationContext(), "Pebble disconnected!", Toast.LENGTH_SHORT).show();
                }

            });

            if (PebbleKit.areAppMessagesSupported(getApplicationContext())) {
                Log.i(C.TAG, "App Message is supported!");

                PebbleDictionary data = new PebbleDictionary();

                // Add a key of 0, and a uint8_t (byte) of value 42.
                data.addUint8(0, (byte) 42);

                // Add a key of 1, and a string value.
                data.addString(1, "A string");

                PebbleKit.sendDataToPebble(getApplicationContext(), C.APP_ID, data);

            } else {
                Log.i(C.TAG, "App Message is not supported");
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);

        // Closing my app
        PebbleKit.closeAppOnPebble(getApplicationContext(), C.APP_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
