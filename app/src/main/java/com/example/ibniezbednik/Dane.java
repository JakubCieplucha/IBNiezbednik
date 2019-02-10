package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Aktywnosc bedaca menu mozliwosci zmiany danych uzytkownika w aplikacji.
 */

public class Dane extends Activity {


    private String specjalizacja;
    private  String uzytkownik;
    private Integer semestr;

   private Bundle bundle;

    private EditText kodzab;
    private  String kod;
    private Button usunBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dane);

        if(getIntent().hasExtra("Bundle")) {
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
        kodzab=(EditText) findViewById(R.id.kodZabUsun );
        usunBtn=(Button) findViewById(R.id.usunBtn);


    }

    /**
     * Rozpoczyna aktywnosc odpowiedzialna za zmiane kursu wybieralnego.
     *
     */
    public void zmienKursWybieralny(View view) {




        Intent kurs= new Intent(getApplicationContext(),ZmienKursWybieralny.class);
        kurs.putExtra("Bundle",bundle);
        startActivity(kurs);


    }

    /**
     * Rozpoczyna aktywnosc odpowiedzialna za zmiane hasla.
     *
     */

    public void zmienHaslo(View view) {


        Intent haslo= new Intent(getApplicationContext(),ZmienHaslo.class);
        haslo.putExtra("Bundle",bundle);
        startActivity(haslo);


    }

    /**
     * Gdy uzytkownik nacisnie przycisk usun konto pojawiaja sie dotej pory niewidoczne czesci aktywnosci.
     * @param view
     */
    public void usunKotno(View view) {



        kodzab.setVisibility(View.VISIBLE);
        usunBtn.setVisibility(View.VISIBLE);


    }

    /**
     * Jesli uzytkownik nacisnie przycisk usun i podal odpowiedni kod zabezpieczajacy, konto zostanie usuniete.
     * @param view
     */
    public void usunKontoPew(View view) {


        kod=kodzab.getText().toString();

        if(kod.equals("")){

            Toast.makeText(getApplicationContext(), "Podaj kod zabezpieczajacy.", Toast.LENGTH_SHORT).show();
        }else{

            UsunKonto usunKonto = new UsunKonto();
            usunKonto.execute("");

        }



    }

    public class UsunKonto extends AsyncTask<String,String,String> {

        String z="";



        String kodPorownanie;


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), z, Toast.LENGTH_SHORT).show();

            Intent logowanie= new Intent(getApplicationContext(),Logowanie.class);

            startActivity(logowanie);



        }

        /**
         *
         * Odpowiada za usuniecie danych uzytkownika z bazy danych.
         *
         */
        @Override
        protected String doInBackground(String... strings) {




            try{

                String sprawdz = "Select kod from Uzytkownicy where nazwa_uzytkownika='" + uzytkownik + "'";

                Statement statement1= con.createStatement();

                ResultSet wynik = statement1.executeQuery(sprawdz);


                while (wynik.next()){


                    kodPorownanie = wynik.getString("kod");

                }



                if(kodPorownanie.equals(kod)) {

                    try {
                        String usun = "Delete from RankinLen where uzytkownik ='" + uzytkownik +
                                "'delete from Indeks where uzytkownik ='" + uzytkownik +
                                "' delete from Ustawienia  where nazwa_uzytkownika ='" + uzytkownik +
                                "' delete from Oceny where uzytkownik='" + uzytkownik +
                                "' Delete from Uzytkownicy where nazwa_uzytkownika ='" + uzytkownik + "'";

                        Statement st = con.createStatement();


                        st.execute(usun);


                        z = "Usunieto konto";


                    } catch (SQLException e) {

                        z = "blad w laczeniu z baza danych";
                    }


                }else {

                    z="Niepoprawny kod";
                }

            }catch (SQLException e){

                z="blad laczenia";
            }



            return z;
        }
    }


}






