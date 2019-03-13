package com.ismailhakkiaydin.asynctask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyStartedService extends Service {

    MyAsyncTask gorev;

    private static final String TAG = MyStartedService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        gorev=new MyAsyncTask();

        Log.i(TAG, "ONCREATE ÇAĞRILDI " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");
    }

    @Override
    public void onDestroy() {

        gorev.cancel(true);
        super.onDestroy();

        Log.i(TAG, "ONDESTROY ÇAĞRILDI " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "ON START COMMAND ÇAĞRILDI " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");

        int sleepTime=intent.getIntExtra("sleepTime", 1);

        gorev.execute(sleepTime);

        return START_REDELIVER_INTENT;
    }

    class MyAsyncTask extends AsyncTask<Integer, String, String>{

        private final String TAG=MyAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "PRE EXECUTE ÇAĞRILDI " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            Log.e(TAG, "DOINBACKGROUND ÇAĞRILDI " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");

            int sleepTime=params[0];

            int kontrol=1;


            while(kontrol <= sleepTime){

                if(!gorev.isCancelled()) {
                    try {
                        publishProgress("Şuanki döngü sayısı : " + kontrol);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                kontrol++;
            }

            try {
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return "ISLEM BASARIYLA TAMAMLANDI";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            Log.e(TAG, "POST EXECUTE ÇAĞRILDI " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");
            Toast.makeText(MyStartedService.this, aVoid, Toast.LENGTH_SHORT).show();
            stopSelf();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.e(TAG, "PROGRESS UPDATE ÇAĞRILDI Kontrol :" + values[0] + "  " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");
            Toast.makeText(MyStartedService.this, "Kontrol :"+ values[0], Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            Log.e(TAG, "CANCEL ÇAĞRILDI " + Thread.currentThread().getName() + " THREAD UZERINDE ÇALIŞIYOR");
            super.onCancelled();
        }
    }

}