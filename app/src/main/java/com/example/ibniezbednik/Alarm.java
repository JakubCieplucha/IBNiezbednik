package com.example.ibniezbednik;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;


/**
 * Aktywnosc sluzaca do rozpoczeczecia dzwieku budzenia , oraz zadania uzytkownikowi wymagan co do jego wylaczenia.
 */

public class Alarm extends AppCompatActivity implements SensorEventListener {


    private SensorManager mySensorManager;
    private Sensor lightSensor;
    private Sensor accelerometer;


    private boolean swiatlo = false;

    private boolean ruch = false;

    private float swiatloWartosc;

    private float aX;
    private float aY;
    private float aZ;

    private boolean done = false;

    private String trudnosc;
    private PowerManager.WakeLock myWakeLock;

    private Button wylacz;
    private Button sprawdz;
    private TextView zadania;
    private TextView obecnaWartosc;
    private EditText wynik;


    private Button rozwiaz;
    private PowerManager powerManager;

    private boolean tylkoL = false;

    private boolean LiX = false;

    private float obrot;

    private boolean wszystko = false;


    private int a;

    private int b;

    private int c;

    private int wynikRownania;

    private int wartoscUzytkownika = 0;

    private int ktore;

    private boolean ostatnie = false;

    private boolean poprawnie = false;



    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        if (getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                bundle = intent.getBundleExtra("Bundle");
                trudnosc = bundle.getString("trudnosc");

            } catch (NullPointerException e) {
                System.out.println("Brak bundle");
            }
        }

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);


        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        accelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mySensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);


        myWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MY APP : Budzik ");
        mySensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        wylacz = (Button) findViewById(R.id.wylaczBudzik);
        sprawdz = (Button) findViewById(R.id.sprawdzR);
        zadania = (TextView) findViewById(R.id.zadaniaTxt);
        obecnaWartosc = (TextView) findViewById(R.id.obecnaWartosc);
        wynik = (EditText) findViewById(R.id.wynikRownania);


        if((isMyServiceRunning(ServiceAlarm.class))!=true) {
            startService(new Intent(Alarm.this, ServiceAlarm.class));
        }
        Budz();

    }

    /**
     * Determinuje jakie zadania uzytkownik musi wykonac aby wylaczyc dzwiek w zaleznosci od wybranego poziomu trudnosci.
     */

    public void Budz() {


        if (trudnosc.equals("Praktycznie nie potrzebuje budzika")) {

            //tylko dzwiek

            wylacz.setVisibility(View.VISIBLE);


        } else if (trudnosc.equals("Przyda mi sie mala pomoc")) {


            tylkoL = true;

            zadania.setText("Aby moc wylaczyc budzik :\nWlacz swiatlo o wartosci min. 20 lumenow");
            obecnaWartosc.setVisibility(View.VISIBLE);


            swiatlo = true;
            Sprawdz sprawdz = new Sprawdz();
            sprawdz.execute("");


        } else if (trudnosc.equals("Zazwyczaj ustawiam piec")) {
            //dzwiek swiatlo i ruch

            swiatlo = true;


            zadania.setText("Aby moc wylaczyc budzik :\nWlacz swiatlo o wartosci min. 20 lumenow");
            obecnaWartosc.setVisibility(View.VISIBLE);

            LiX = true;

            Sprawdz sprawdz = new Sprawdz();
            sprawdz.execute("");


        } else if (trudnosc.equals("I tak sie nie obudze")) {
            // dzwiek swiatlo ruch i rownanie


            swiatlo = true;


            zadania.setText("Aby moc wylaczyc budzik :\nWlacz swiatlo o wartosci min. 20 lumenow");
            obecnaWartosc.setVisibility(View.VISIBLE);


            wszystko = true;

            Sprawdz sprawdz = new Sprawdz();
            sprawdz.execute("");

        }

    }

    /**
     * Odpowiada za wylaczenie dzwieku poprzez zakonczenie service oraz wyjscie z aplikacji.
     * @param view
     */

    public void wylacz(View view) {



        stopService(new Intent(Alarm.this, ServiceAlarm.class));

        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/AppData/");
            File file = new File(dir, "IBBudzik.txt");

            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);

                pw.print("");


                pw.flush();
                pw.close();
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch ( Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        wylacz.setVisibility(View.INVISIBLE);
        done = false;
        swiatlo = false;


        finishAffinity();
        System.exit(0);

    }


    @Override
    public void onSensorChanged(SensorEvent SensorEvent) {

        if (swiatlo) {

            if (SensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {

                swiatloWartosc = SensorEvent.values[0];
                obecnaWartosc.setText("Obecna wartosc : " + Float.toString(swiatloWartosc) + " lumenow");

            }
        } else if (ruch) {


            if (SensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                aX = SensorEvent.values[0];
                obecnaWartosc.setText("Obecna wartosc : " + aX);

            }


        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Odpowiada za sprawdzanie wykonywania przez uzytkownika poszczegolnych wymagan.
     */


    public class Sprawdz extends AsyncTask<String, String, String> {


        float wartoscL = 19;

        String z = "";


        @Override
        protected void onPostExecute(String r) {


            if (ostatnie == true) {


                if (ktore == 0) {
                    zadania.setText("Rozwiaz rownanie " + a + " + " + b);

                    wynikRownania = a + b;
                } else if (ktore == 1) {
                    zadania.setText("Rozwiaz rownanie " + a + " - " + b);
                    wynikRownania = a - b;

                } else if (ktore == 2) {
                    zadania.setText("Rozwiaz rownanie " + a + " * " + b);
                    wynikRownania = a * b;
                }


                sprawdz.setVisibility(View.VISIBLE);
                wynik.setVisibility(View.VISIBLE);
                obecnaWartosc.setVisibility(View.INVISIBLE);


            } else if (ostatnie == false) {
                obecnaWartosc.setVisibility(View.INVISIBLE);
                swiatlo = false;
                ruch = false;
                wylacz.setVisibility(View.VISIBLE);
            }
        }


        @Override
        protected String doInBackground(String... strings) {


            if (tylkoL) {
                while (tylkoL == true) {

                    if (swiatloWartosc > wartoscL) {

                        tylkoL = false;
                    }


                }
            } else if (LiX) {

                float[] array = new float[]{8.5f, -8.5f};
                int rnd = new Random().nextInt(array.length);
                obrot = array[rnd];

                while (LiX == true) {

                    if (swiatloWartosc > wartoscL) {

                        swiatlo = false;
                        ruch = true;


                        zadania.setText("Teraz  obroc telefon w osi X na " + obrot);

                        if (obrot > 0) {

                            if (aX > obrot) {

                                LiX = false;
                            }

                        } else if (obrot < 0) {

                            if (aX < obrot) {

                                LiX = false;
                            }
                        }


                    }


                }

            } else if (wszystko) {

                float[] array = new float[]{8.5f, -8.5f};
                int rnd = new Random().nextInt(array.length);
                obrot = array[rnd];

                ktore = new Random().nextInt(3);
                a = new Random().nextInt(51) + 1;
                b = new Random().nextInt(51) + 1;


                while (wszystko == true) {

                    if (swiatloWartosc > wartoscL) {

                        swiatlo = false;
                        ruch = true;


                        zadania.setText("Teraz  obroc telefon w osi X na " + obrot);

                        if (obrot > 0) {

                            if (aX > obrot) {

                                ruch = false;

                                ostatnie = true;

                                wszystko = false;

                            } // obrot dodatni

                        } // obrot ujemny
                        else if (obrot < 0) {

                            if (aX < obrot) {

                                ostatnie = true;
                                ruch = false;
                                wszystko = false;
                            }


                        }// obrot ujemny


                    }// wartosc swiatla


                }//while


            }// wszystko


            return z = "wykonano sprawdzenie";
        }
    }


    /**
     * Czynnosci wykonywane gdy wcisniety zostanie przycisk sprawdz.
     */

    public void sprawdzClicked(View view) {


        try {
            wartoscUzytkownika = Integer.parseInt(wynik.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Nie podano odpowiedzi!",
                    Toast.LENGTH_LONG).show();
        }
        Rownania1 rownania = new Rownania1();
        rownania.execute("");
    }

    /**
     * Sprawdza czy  podana przez uzytkownika wartosc jest poprawna.
     */

    public class Rownania1 extends AsyncTask<String, String, String> {


        @Override
        protected void onPostExecute(String r) {


            if (poprawnie == true) {
                wynik.setVisibility(View.INVISIBLE);
                sprawdz.setVisibility(View.INVISIBLE);
                wylacz.setVisibility(View.VISIBLE);
            } else if (poprawnie == false) {
                Toast.makeText(getApplicationContext(), "Bledna odpowiedz!!",
                        Toast.LENGTH_LONG).show();
            }

        }


        @Override
        protected String doInBackground(String... strings) {


            while (ostatnie == true) {


                if (ktore == 0) {


                    if (wynikRownania == wartoscUzytkownika) {

                        ostatnie = false;
                        poprawnie = true;
                    } else if (wynikRownania != wartoscUzytkownika) {
                        ostatnie = false;
                    }


                }// dodawanie

                else if (ktore == 1) {


                    if (wynikRownania == wartoscUzytkownika) {

                        ostatnie = false;
                        poprawnie = true;
                    } else if (wynikRownania != wartoscUzytkownika) {
                        ostatnie = false;
                    }


                }//odejmowanie

                else if (ktore == 2) {


                    if (wynikRownania == wartoscUzytkownika) {

                        ostatnie = false;
                        poprawnie = true;
                    } else if (wynikRownania != wartoscUzytkownika) {
                        ostatnie = false;
                    }

                }//mnozenie

            }


            return null;
        }
    }

    /**
     *Service odpowiedzialny za rozpoczecie dzwieku budzenia.
     */

    public static class ServiceAlarm extends Service {
        MediaPlayer mediaPlayer;

        public ServiceAlarm() {
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            mediaPlayer.start();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onCreate() {

            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

            super.onCreate();
        }

        @Override
        public void onDestroy() {
            mediaPlayer.stop();
            super.onDestroy();
        }


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }


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

