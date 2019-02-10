package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Statement;

import static com.example.ibniezbednik.Logowanie.con;

/**
 *
 * Aktywnosc sluzaca za tworzenie konta dla nowego uzytkownika.
 */

public class Rejestracja extends Activity {

    private ConnectionClass connectionClass;


    private EditText uzytkownik;
    private EditText haslo;
    private EditText haslo2;
    private EditText ko;

    private String uzytkownik1;
    private String haslo1;
    private String haslo21;
    private String kod;


    private boolean stworzono = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        connectionClass = new ConnectionClass();
        uzytkownik = (EditText) findViewById(R.id.uzytkownikTxt);
        haslo = (EditText) findViewById(R.id.haslooTxt);
        haslo2 = (EditText) findViewById(R.id.haslo2Txt);
        ko = (EditText) findViewById(R.id.kodTxt);

    }

    public void stworz(View view) {

        Utworz utworz = new Utworz();
        utworz.execute("");

    }


    /**
     * Dodaje uzytkownika do bazy danych.
     */
    public class Utworz extends AsyncTask<String, String, String> {

        String z = "";


        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


        }


        @Override
        protected String doInBackground(String... strings) {


            try {

                con = connectionClass.CONN();


                if (con == null) {
                    z = "Blad przy laczeniu z baza danych ";
                } else {
                    uzytkownik1 = uzytkownik.getText().toString();
                    haslo1 = haslo.getText().toString();
                    haslo21 = haslo2.getText().toString();
                    kod = ko.getText().toString();

                    if (uzytkownik1.trim().equals("") || haslo1.trim().equals("") || haslo21.trim().equals("")) {


                        z = "Bledne dane!";

                    } else if (!haslo1.equals(haslo21)) {


                        z = "Podane hasla nie zgadzaja sie!";

                    } else {

                        try {


                            if (con == null) {

                                z = "Bled w laczeniu z baza danych!";
                            } else {

                                try {

                                    String dodajUzytkownika = "insert into Uzytkownicy(nazwa_uzytkownika, haslo,kod) " +
                                            "values ('" + uzytkownik1 + "','" + haslo1 + "','" + kod + "')";

                                    Statement insert = con.createStatement();

                                    insert.execute(dodajUzytkownika);


                                    z="Utworzono uzytkownika";

                                    stworzono = true;


                                } catch (SQLException ex) {
                                    ex.printStackTrace();


                                    z = "Uzytkownik o takiej nazwie juz istnieje!";
                                }


                            }


                        } catch (Exception e) {
                            z= "blad w laczeniu z baza  danych";
                        }

                        if (stworzono) {
                            Intent powrotDoLogowania = new Intent(getApplicationContext(), Logowanie.class);
                            startActivity(powrotDoLogowania);

                        }


                    }// koniec else


                    return z;

                }


            } catch (Exception e) {

               z="brak polaczenia";
            }

            return z;
        }

    }
}


