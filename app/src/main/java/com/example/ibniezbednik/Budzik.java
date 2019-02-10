package com.example.ibniezbednik;


import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.os.PowerManager;
import android.os.SystemClock;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Aktywnosc odpowiadajaca za ustawianie budzika.
 */

public class Budzik extends AppCompatActivity {


   private ArrayList<Integer> wczytane= new ArrayList<>();

    private TimePicker timePicker;
    private Spinner poziomTrudnosci;

    private  String uzytkownik;

    private String czasBudzenia;

    private Integer godzina;
    private  Integer minuta;

    private String trudnosc;


    private String specjalizacja;

    private Integer semestr;

    private  boolean zrownane = false;

    private  String  czasObecnie;

    private  PowerManager.WakeLock myWakeLock;
    private PowerManager powerManager;

    private  boolean lock = false;

    private  Bundle bundle;

    private  TextView ust;

    public static AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budzik);

        if (getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                 bundle = intent.getBundleExtra("Bundle");

                uzytkownik = bundle.getString("uzytkownik");
                specjalizacja=bundle.getString("specjalizacja");
                semestr=bundle.getInt("semestr");


            } catch (NullPointerException e) {
                System.out.println("Brak bundle");

            }
        }

        timePicker = (TimePicker) findViewById(R.id.simpleTimePicker);
        timePicker.setIs24HourView(true);
        poziomTrudnosci=(Spinner) findViewById(R.id.spinnerPoziomTrudnosci);

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        myWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MY APP : Budzik ");
       ust=(TextView) findViewById(R.id.ustawionyTxt);


            wyswietlWczytanyCzas();
    }


    /**
     * Odpowiada za ustawienie alarm managera, ktory obudzi telefon i wywola aktywnosc Alarm po uplywie czau pomiedzy obecnym a zadanym czasem.
     *
     */


    public void dodajBudzik(View view) {

        godzina=timePicker.getHour();
        minuta=timePicker.getMinute();

        writeToFile(godzina,minuta,getApplicationContext());
        trudnosc=poziomTrudnosci.getSelectedItem().toString();

        String trudnosc1 = trudnosc;
        bundle.putString("trudnosc",trudnosc1);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        int godzinyDoBudzenia=0;
        int minutyDoBudzenia=0;

        Calendar cal = Calendar.getInstance();
        Integer godzinaTeraz = cal.get(Calendar.HOUR_OF_DAY);
        Integer minutaTeraz = cal.get(Calendar.MINUTE);
        Integer milis = cal.get(Calendar.MILLISECOND);

        if(godzinaTeraz>godzina){

            godzinyDoBudzenia= (24 - (godzinaTeraz - godzina))  * 3_600_0000;

        }else if(godzinaTeraz<godzina){

            godzinyDoBudzenia= (godzina - godzinaTeraz) * 3_600_000;
        }


        if(minuta>minutaTeraz){

            minutyDoBudzenia=(minuta-minutaTeraz) * 60_000;
        }else if (minuta<minutaTeraz){
            minutyDoBudzenia=minuta-minutaTeraz * 60_000;

        }



       Intent intent = new Intent(Budzik.this, Alarm.class);
        intent.putExtra("Bundle",bundle);
        System.out.println(godzinaTeraz +"    " +  godzinyDoBudzenia + " " + godzina);
        System.out.println(minutaTeraz + "       " + minutyDoBudzenia +  "      " + minuta);
        System.out.println(bundle.get("trudnosc"));

        String wyswietlh="";
        String wyswietlM="";
        if(godzina<10){

            wyswietlh = wyswietlh.concat("0")+godzina;

        }else{
           wyswietlh=godzina.toString();
        }

        if(minuta<10){
            wyswietlM = wyswietlM.concat("0") + minuta;

        }else{
            wyswietlM=minuta.toString();
        }
        ust.setText(wyswietlh + ":" + wyswietlM);
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getActivity(Budzik.this, 0, intent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + godzinyDoBudzenia + minutyDoBudzenia - milis, pendingIntent);




}

    /**
     * Odpowiada za wyswietlanie czasu ustawionego budzika.
     */

        public void wyswietlWczytanyCzas(){

        try {
            readFromFile(getApplicationContext());
            String wyswietlh="";
            String wyswietlM="";
            if(wczytane.get(1)<10){

                wyswietlh = wyswietlh.concat("0")+ wczytane.get(1);

            }else{
                wyswietlh=wczytane.get(1).toString();
            }

            if(wczytane.get(0)<10){
                wyswietlM = wyswietlM.concat("0") + wczytane.get(0).toString();

            }else{
                wyswietlM=wczytane.get(0).toString();
            }
            ust.setText(wyswietlh + ":" + wyswietlM);
        }catch (Exception e){
            System.out.println("Brak ustawionego budzika.");
        }


    }

    /**
     *  Odpowiada za wczytannie z pliku, godziny na ktora ustawiono budzik.
     *
     */

    private void readFromFile(Context context) {


        String ret = "";
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/AppData/");
        File file = new File(dir, "IBBudzik.txt");
        try {

                 FileReader fr = new FileReader(file);

                BufferedReader bufferedReader = new BufferedReader(fr);
                String receiveString = "";


                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    String[] splited = receiveString.split(",");
                    for (String part : splited) {

                        wczytane.add(Integer.parseInt(part));

                    }


                }

                bufferedReader.close();
                fr.close();


        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


    }

    /**
     * Zapisuje ustawiony budzik do pliku.
     * @param godzina godzina na ktora ustawiono budzik.
     * @param minuta  minuta na ktora ustawiono budzik
     * @param context kontekst aplikacji.
     */
    private void writeToFile(int godzina,int minuta,Context context) {


        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/AppData/");
            dir.mkdirs();
            File file = new File(dir, "IBBudzik.txt");

            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);

                pw.print(minuta + "," + godzina + "");


                pw.flush();
                pw.close();
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            } catch ( Exception e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

    }


    /**
     *Odpowiada za usuwanie ustawionego budzika.
     */

    public void usunBudzik(View view) {



    try {
        Intent intent = new Intent(Budzik.this, Alarm.class);
        pendingIntent = PendingIntent.getActivity(Budzik.this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);

        ust.setText("");

    }catch (Exception e){
        e.printStackTrace();
    }

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



    }



    }
