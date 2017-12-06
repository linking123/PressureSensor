package com.suncreate.fireiot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.suncreate.fireiot.util.StringUtils;

import org.kymjs.kjframe.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LogUploadService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final File log = FileUtils.getSaveFile("Wukong", "Wukong.log");
        String data = null;
        try {
            FileInputStream inputStream = new FileInputStream(log);
            data = StringUtils.toConvertString(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(data)) {
//            WukongApi.uploadLog(data, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//                    log.delete();
//                    LogUploadService.this.stopSelf();
//                }
//
//                @Override
//                public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                        Throwable arg3) {
//                    LogUploadService.this.stopSelf();
//                }
//            });
        } else {
            LogUploadService.this.stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
