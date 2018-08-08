package com.tianlin.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class OutputService extends Service {
    public OutputService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
