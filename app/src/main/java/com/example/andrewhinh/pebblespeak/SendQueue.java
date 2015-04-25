package com.example.andrewhinh.pebblespeak;

import android.database.sqlite.SQLiteDatabase;

public interface SendQueue {
    void addNewReading(AccelData reading);
    int sendUnsent();
    void persistFailed(long now, SQLiteDatabase db);
}
