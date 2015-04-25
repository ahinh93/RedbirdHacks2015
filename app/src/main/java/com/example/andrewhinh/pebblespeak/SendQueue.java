package com.example.andrewhinh.pebblespeak;

import android.database.sqlite.SQLiteDatabase;

import com.example.andrewhinh.pebblespeak.data.model.AccelData;

public interface SendQueue {
    void addNewReading(AccelData reading);
    int sendUnsent();
    void persistFailed(long now, SQLiteDatabase db);
}
