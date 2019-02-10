package com.example.ibniezbednik;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Aktywnosc wyswietlajaca sie wylacznie w przypadku braku internetu .
 */

public class MainActivity extends AppCompatActivity  {


    static  volatile  boolean connected = false;
    private   GotoLogin gotoLogin ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


     if((isMyServiceRunning(Alarm.ServiceAlarm.class)) == true){

         Intent alarm = new Intent(getApplicationContext(),Alarm.class);
         Bundle bundle= new Bundle();
         bundle.putString("trudnosc","I tak sie nie obudze");
         alarm.putExtra("Bundle",bundle);
         startActivity(alarm);
        }else {
         gotoLogin = new GotoLogin();
         gotoLogin.execute("");
     }
    }

    public void Odswiez(View view) {

        gotoLogin = new GotoLogin();
        gotoLogin.execute("");


    }


    /**
     * Sprawdza polaczenie z internetem.
     */
    public class GotoLogin extends AsyncTask<String,String,String> {

        String z="";


        Intent logowanie = new Intent(getApplicationContext(), Logowanie.class);

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();

            if(connected) {

                startActivity(logowanie);
            }


        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    connected =true;
                    z="Internet connection";
                }else {
                    connected = false;
                    z="No internet connection";
                }

            }catch (Exception e){

                z="Exception";

            }

            return z;
        }
    }

    /**
     *Sprawdza czy service zwiazany z budzikiem jest aktywny.
     * @return stan serwisu.
     */

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
