package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Aktywnosc umozliwiajaca zmiane kursu wybieralnego przypisanego do danego semestru.
 */
public class ZmienKursWybieralny extends Activity {



    private String specjalizacja;
    private  String uzytkownik;
    private Integer semestr;
    private Bundle bundle;
    private  Spinner spinner1;
    private Spinner spinner2;

    private  String nowy="";
    private  String stary="";


    private ArrayList<String> stareKursy=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zmien_kurs_wybieralny);

        if (getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                bundle = intent.getBundleExtra("Bundle");
                specjalizacja = bundle.getString("specjalizacja");
                semestr = bundle.getInt("semestr");
                uzytkownik = bundle.getString("uzytkownik");


            } catch (NullPointerException e) {
                System.out.println("Brak bundle");
            }
        }


        spinner1=(Spinner) findViewById(R.id.spinnerWyb1);
        spinner2=(Spinner) findViewById(R.id.spinnerWyb2);

        WybUzytkowniaa wybUzytkowniaa = new WybUzytkowniaa();
        wybUzytkowniaa.execute("");

    }


    /**
     * Polecenia wykonywane po  nacisnieciu przycisku zmien kurs.
     */
    public void zmienKurs(View view) {

        try    {
            stary=spinner1.getSelectedItem().toString();
             nowy = spinner2.getSelectedItem().toString();
    }catch(Exception e){
        e.printStackTrace();
    }


    Zmien zmien = new Zmien();
        zmien.execute("");



}

    /**
     * Odpowiada za zmiane ustawien uzytkownika w bazie danych.
     */
    public class Zmien extends AsyncTask<String,String,String>{

        String z="";


            String stary = spinner1.getSelectedItem().toString();


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show();


            WybUzytkowniaa wybUzytkowniaa = new WybUzytkowniaa();
            wybUzytkowniaa.execute("");

        }

        @Override
        protected String doInBackground(String... strings) {




            try{
                String zmiana = " Update Ustawienia set kurs_wybieralny= '" + nowy + "" +
                        "' where nazwa_uzytkownika = '" + uzytkownik + "' and kurs_wybieralny = '" + stary + "' and specjalizacja = '"  + specjalizacja + "' and semestr=" + semestr;


                Statement st = con.createStatement();


                st.execute(zmiana);


                z="Zmieniono";


            }catch (SQLException e ){

                z="blad w laczeniu z baza danych";
            }




            return z;
        }
    }


    /**
     * Pobiera kursy wybieralne dodane przez uzytkownika, przypisane do danego semestru z bazy danych.
     */
    public class WybUzytkowniaa extends AsyncTask<String,String,String>{


        String z  = "";


        @Override
        protected void onPreExecute() {
           stareKursy.clear();
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_spinner_dropdown_item, stareKursy);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... strings) {



            try{


                String sprawdz = " Select * from Ustawienia  where nazwa_uzytkownika = '" + uzytkownik + "'  and specjalizacja = '"  + specjalizacja + "' and semestr=" + semestr ;

                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(sprawdz);



                while(rs.next()){


                    stareKursy.add(rs.getString("kurs_wybieralny"));

                }



            }catch (SQLException e){

                z="blad";
            }



            return z;
        }
    }



}

