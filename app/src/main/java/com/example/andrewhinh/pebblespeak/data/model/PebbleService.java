package com.example.andrewhinh.pebblespeak.data.model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.Constants;
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

    private Camera cam;

    private boolean actionReceived = false;

    private Handler mHandler = new Handler();

    final Runnable r = new Runnable() {
        public void run() {
            actionReceived=false;
        }
    };

    final Runnable toggleFlash = new Runnable() {
        @Override
        public void run() {
            actionReceived = false;
            cam.stopPreview();
            cam.release();
        }
    };

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
//                        Log.i(C.TAG, "Received value=" + data.getUnsignedIntegerAsLong(0) + " for key: 0");
//                        Log.i(C.TAG, "Received value=" + data.getUnsignedIntegerAsLong(1) + " for key: 1");
//                        Log.i(C.TAG, "Received value=" + data.getUnsignedIntegerAsLong(2) + " for key: 2");
                        PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);

                        long x1 = data.getInteger(0);
                        long y1 = data.getInteger(1);

                        if (!actionReceived) {
                            if (x1 > -800) {
                                if (y1 > 800) {
                                    cam = Camera.open();
                                    Camera.Parameters p = cam.getParameters();
                                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                    cam.setParameters(p);
                                    cam.startPreview();

                                    actionReceived = true;
                                    mHandler.postDelayed(toggleFlash, 10000);

                                } else if (y1 < -800) {
                                    actionReceived = true;
                                    mHandler.postDelayed(r, 10000);
                                    //extended left
                                    Log.i(C.TAG, "LEFT");
                                    Intent i = new Intent(Intent.ACTION_DIAL);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            }
                        }

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
