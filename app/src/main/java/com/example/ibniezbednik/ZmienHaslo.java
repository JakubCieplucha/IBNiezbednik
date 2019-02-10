package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Aktywnosc umozliwiajaca zmiane hasla.
 */
public class ZmienHaslo extends Activity {


    private String specjalizacja;
    private  String uzytkownik;
    private Integer semestr;

    private  Bundle bundle;

    private EditText noweHaslo1Txt;
    private EditText noweHaslo2Txt;
    private EditText stareHasloTxt;

    private String noweHaslo;
    private  String noweHaslo2;
    private String stareHaslo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zmien_haslo);


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

        noweHaslo1Txt= (EditText) findViewById(R.id.noweHasloTxt);
        noweHaslo2Txt= (EditText) findViewById(R.id.noweHaslo2Txt);
        stareHasloTxt= (EditText) findViewById(R.id.stareHaloTxt);

    }

    /**
     * Polecenia wykonywane po kliknieciu przycisku zmien haslo.
     */
    public void zmienHaslo(View view) {



        noweHaslo=noweHaslo1Txt.getText().toString();
        noweHaslo2=noweHaslo2Txt.getText().toString();
        stareHaslo=stareHasloTxt.getText().toString();

        if(noweHaslo.equals("") || noweHaslo2.equals("") ||stareHaslo.equals("") || !noweHaslo.equals(noweHaslo2)){
            Toast.makeText(getApplicationContext(), "Blednie wprowadzone dane.", Toast.LENGTH_SHORT).show();

        }else{

            ZmienHa zmienHa = new ZmienHa();
            zmienHa.execute("");
        }



        }

    /**
     * Odpowiada za zmiane hasla w bazie danych.
     */

    public class ZmienHa extends AsyncTask<String,String,String> {

        String z="";



        String doPorownania;


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show();




        }

        @Override
        protected String doInBackground(String... strings) {




            try{

                String sprawdz = "Select haslo from Uzytkownicy where nazwa_uzytkownika='" + uzytkownik + "'";

                Statement statement1= con.createStatement();

                ResultSet wynik = statement1.executeQuery(sprawdz);


                while (wynik.next()){


                    doPorownania = wynik.getString("haslo");

                }



                if(doPorownania.equals(stareHaslo)) {

                    try {
                        String zmiana = "Update Uzytkownicy  set haslo='" + noweHaslo + "'  where nazwa_uzytkownika='" + uzytkownik + "'";

                        Statement st = con.createStatement();


                        st.execute(zmiana);


                        z = "Zmieniono";


                    } catch (SQLException e) {

                        z = "blad w laczeniu z baza danych";
                    }


                }else {

                    z="Niepoprawne haslo";
                }

            }catch (SQLException e){

                z="blad laczenia";
            }



            return z;
        }
    }





}

