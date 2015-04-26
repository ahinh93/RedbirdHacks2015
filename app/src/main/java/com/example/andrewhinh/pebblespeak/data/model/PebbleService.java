package com.example.andrewhinh.pebblespeak.data.model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
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

    public static Intent leftIntent, rightIntent, bottomIntent, shakeIntent;

    private final IBinder mBinder = new BluetoothBinder();

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

                PebbleKit.registerReceivedAckHandler(getApplicationContext(), new PebbleKit.PebbleAckReceiver(C.APP_ID) {

                    @Override
                    public void receiveAck(Context context, int transactionId) {
                        Log.i(C.TAG, "Received ack for transaction " + transactionId);
                    }

                });

                PebbleKit.registerReceivedNackHandler(getApplicationContext(), new PebbleKit.PebbleNackReceiver(C.APP_ID) {

                    @Override
                    public void receiveNack(Context context, int transactionId) {
                        Log.i(C.TAG, "Received nack for transaction " + transactionId);
                    }

                });

                PebbleKit.registerReceivedDataHandler(this, new PebbleKit.PebbleDataReceiver(C.APP_ID) {

                    @Override
                    public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                        Log.i(C.TAG, "Received value=" + data.getUnsignedIntegerAsLong(0) + " for key: 0");

                        PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
                    }
                });
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
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    public class BluetoothBinder extends Binder {
        public PebbleService getService() {
            return PebbleService.this;
        }
    }

    public void setLeftIntent(Intent i){
        leftIntent = i;
    };

    public void setRightIntent(Intent i) {
       rightIntent = i;
    };

    public void setBottomIntent(Intent i) {
        bottomIntent = i;
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    };

    public Intent getLeftIntent() {
        return leftIntent;
    };

    public Intent getRightIntent() {
        return rightIntent;
    };

    public Intent getBottomIntent() {
        return bottomIntent;
    };
}
