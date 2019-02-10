package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Rnaking trudnosci kursow.
 */
public class RankingKursow extends Activity {


    private String uzytkownik;
    private String specjalizacja;
    private Integer semestr;


    private Spinner kursSpinner;
    private ListView listaRankingowa;

    private EditText oce;

    private ArrayList<String> kursy=new ArrayList<>();

    private  Spinner ocenioneKursy;
    private  EditText nowaOcena;

    private String ocenionyKurs;
    private  String nowa;

    private  ArrayList<String> ocenione= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_kursow);
        if (getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("Bundle");
                specjalizacja = bundle.getString("specjalizacja");
                semestr = bundle.getInt("semestr");
                uzytkownik = bundle.getString("uzytkownik");





            } catch (NullPointerException e) {
                System.out.println("Brak bundle");


            }
        }


        kursSpinner= (Spinner) findViewById(R.id.spinnerKursOcena);
        listaRankingowa=(ListView) findViewById(R.id.listaRankingowa);
        oce= (EditText) findViewById(R.id.ocenaKursuRan);

        ocenioneKursy = findViewById(R.id.spinnerOcenioneKursy);
        nowaOcena = findViewById(R.id.nowaOcenaTxt);

        Kursy1 kursy = new Kursy1();
        kursy.execute("");

        Wyswietl2 wyswietl2 = new Wyswietl2();
        wyswietl2.execute("");

        OcenioneKursy ocenione = new OcenioneKursy();
        ocenione.execute("");

    }// koniec create


    /**
     * Polecenia wykonywane po wcisnieciu przycisku dodaj.
     */
    public void dodaj(View view) {

        if(Integer.parseInt(oce.getText().toString())>0 && Integer.parseInt(oce.getText().toString())<11 ) {

            Dodaj1 dodaj1 = new Dodaj1();
            dodaj1.execute("");
        }else{
            Toast.makeText(getApplicationContext(), "Prosze podac ocene z przedzialu 0-10!", Toast.LENGTH_SHORT).show();
        }



    }

    /**
     * Polecenia wykonywane po wcisnieciu przycisku zmien ocene.
     * @param view
     */

    public void zmienOceneKursu(View view) {


        ocenionyKurs = ocenioneKursy.getSelectedItem().toString();
        nowa= nowaOcena.getText().toString();


        if(Integer.parseInt(nowa)>0 || Integer.parseInt(nowa)<11 ) {

            Zmien zmien = new Zmien();
            zmien.execute("");
        }else{
            Toast.makeText(getApplicationContext(), "Prosze podac ocene z przedzialu 0-10!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Pobranie danych odnosnie ocenionych kursow z bazy danych.
     */


    public class OcenioneKursy extends AsyncTask<String, String, String> {


        String z = "";
        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_spinner_dropdown_item, ocenione);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ocenioneKursy.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();

        }


        @Override
        protected String doInBackground(String... strings) {

        ocenione.clear();
            try{

                String ocen="  select * from Oceny o join Kurs k on o.kurs=k.nazwa_kursu where  o.uzytkownik  = '"+  uzytkownik +"' and k.semestr=" +semestr ;

                Statement st = con.createStatement();
                ResultSet rs= st.executeQuery(ocen);

                while(rs.next()){

                    ocenione.add(rs.getString("kurs"));

                }


            }catch (SQLException e ){

                z="Blad";
            }

            return z;
        }
    }


    /**
     * Aktywnosc odpowiedzialna za zmiane oceny kursu w bazie .
     */
    public class Zmien extends AsyncTask<String, String, String> {

        String z="";

        @Override
        protected void onPostExecute(String r) {


            Wyswietl2 wyswietl2 = new Wyswietl2();
            wyswietl2.execute("");


        }



        @Override
        protected String doInBackground(String... strings) {


            try{

                String upd = "Update Oceny set ocena= " + nowa + " where kurs='" + ocenionyKurs + "' and uzytkownik = '" + uzytkownik + "'";

                Statement st= con.createStatement();

                st.execute(upd);

                z="Zmieniono";

            }catch (SQLException e){

                z="blad";
            }


            return z;
        }


        }


    /**
     * Pobranie danych odnosnie realizowanych kursow z bazy danych.
     */


    public class Kursy1 extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_spinner_dropdown_item, kursy);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kursSpinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();

        }







        @Override
        protected String doInBackground(String... strings) {

            String kursyZsemestru="";


            try{


                if(specjalizacja.equals("Informatyka Medyczna")  || specjalizacja.equals("Elektronika Medyczna")  ){

                    kursyZsemestru="select  cnps,liczba_h,nazwa_kursu , semestr from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' or specjalizacja ='Informatyka-Elektronika' )";



                }else{

                    kursyZsemestru="select  cnps,liczba_h,nazwa_kursu , semestr from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' )";
                }



                Statement ku= con.createStatement();
                ResultSet resultSet = ku.executeQuery(kursyZsemestru);


                while(resultSet.next()){

                    kursy.add(resultSet.getString("nazwa_kursu"));

                }



                z="Laduje";

            }catch (SQLException e){
                e.printStackTrace();
                z="Blad";
            }





            return z;
        }
    }// koniec kursy


    /**
     * Dodaje ocene co bazy danych.
     */

    public  class Dodaj1 extends AsyncTask<String ,String,String >{

        String z="";


        int ocena=0;

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


            OcenioneKursy ocenione = new OcenioneKursy();
            ocenione.execute("");

            Wyswietl2 wyswietl2 = new Wyswietl2();
            wyswietl2.execute("");
        }


        @Override
        protected String doInBackground(String... strings) {


            String kurs = kursSpinner.getSelectedItem().toString();

            try {
                ocena = Integer.parseInt(oce.getText().toString());

            }catch (Exception e){
                z="Nie podano oceny !";
            }




            if( ocena > 0 && ocena <11 ) {


                try{



                    String sprawdz = "Select * from Oceny where uzytkownik='" + uzytkownik + "' and kurs ='" + kurs + "'";

                    Statement sprawdzenie = con.createStatement();

                    ResultSet resultSet = sprawdzenie.executeQuery(sprawdz);

                    if (!resultSet.isBeforeFirst() ) {


                        String  dodajOcene=" Insert  into Oceny(kurs, uzytkownik, ocena, data_dodania)" +
                                "values ('" + kurs + "','" + uzytkownik + "'," + ocena +",GETDATE())";


                        Statement dod= con.createStatement();
                        dod.execute(dodajOcene);

                        z="Dodano";

                    }else {

                        z="Dodano juz ocene dla tego kursu! ";
                    }


                }catch (SQLException e){
                    e.printStackTrace();
                    z="blad";
                }

            }else {

                z="Prosze podac ocene z przedzialu 1-10 !";
            }


            return z;


        }
    }// koniec dodaj


    /**
     * Dodaje kursy wraz z ich usredniona ocena do ListView.
     */

    public  class Wyswietl2 extends AsyncTask<String ,String,String >{

        String z="";

        ArrayAdapter<String> adapter;



        public ArrayList<String> wyniki = new ArrayList<>();


        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


            adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, wyniki);
            listaRankingowa.setAdapter(adapter);


        }


        @Override
        protected String doInBackground(String... strings) {


            String kursyZsemestru="";


            try{


                if(specjalizacja.equals("Informatyka Medyczna")  || specjalizacja.equals("Elektronika Medyczna")  ){

                    kursyZsemestru="select  ocena,liczba_oceniajacych,nazwa_kursu  from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' or specjalizacja ='Informatyka-Elektronika') order by ocena desc";



                }else{

                    kursyZsemestru="select  ocena,liczba_oceniajacych,nazwa_kursu from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' ) order by ocena desc ";
                }



                Statement ku= con.createStatement();
                ResultSet resultSet = ku.executeQuery(kursyZsemestru);


                while(resultSet.next()){

                    wyniki.add(resultSet.getString("nazwa_kursu")  + " | " + resultSet.getFloat("ocena")   );

                }



                z="Oceniaj obiektywnie";

            }catch (SQLException e){
                e.printStackTrace();
                z="Blad";
            }



            return z;
        }
    }// koniec wyswietl

}
