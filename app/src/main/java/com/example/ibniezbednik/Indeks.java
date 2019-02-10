package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
 * Aktywnosc Indeks umozliwiajaca kontrolowanie ocen otrzymanych z poszczegolnych kursow.
 */

public class Indeks extends Activity implements AdapterView.OnItemSelectedListener {



    private String uzytkownik;
    private String specjalizacja;
    private Integer semestr;


    private Spinner kursSpinner;
    private ListView listaOcen;
    private EditText tytu;
    private  EditText oce;

    private ArrayList<String> kursy=new ArrayList<>();
    private ArrayList<String> ocenioneKursy=new ArrayList<>();
    private ArrayList<String> tytuly=new ArrayList<>();


    private Spinner ocenione;
    private Spinner tytulyOcenionych;


    private  String kursDoUsuniecia;
    private  String tytulDoUsuniecia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indeks);
        if(getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("Bundle");
                specjalizacja = bundle.getString("specjalizacja");
                semestr = bundle.getInt("semestr");
                uzytkownik = bundle.getString("uzytkownik");





            } catch (NullPointerException e) {
                System.out.println("Brak bundle");
            }

            kursSpinner=(Spinner) findViewById(R.id.spinnerKursyIndeks);
            listaOcen=(ListView)  findViewById(R.id.listaIndeks);
            tytu=(EditText) findViewById(R.id.tytulOcenyTxt);
            oce=(EditText) findViewById(R.id.ocenaIndeksTxt);

            ocenione= findViewById(R.id.spinnerKursyIndeksOcenione);
            tytulyOcenionych=findViewById(R.id.spinnerTytulyOcen);

            ocenione.setOnItemSelectedListener(this);

            Kursy kursy=new Kursy();
            kursy.execute("");

            Wyswietl wyswietl = new Wyswietl();
            wyswietl.execute("");

            Ocenione ocenione1 = new Ocenione();
            ocenione1.execute("");



        }



    }// koniec ccreate


    /**
     *Odpowiada za czynnosci zwiazane z nacisnieciem przycisku dodaj
     */

    public void dodajOcene(View view) {


        if(Double.parseDouble(oce.getText().toString())>1.9 && Double.parseDouble(oce.getText().toString())<5.6 ) {

            Dodaj dodaj = new Dodaj();
            dodaj.execute("");

        }else{
            Toast.makeText(getApplicationContext(), "Prosze podac ocene z przedzialu 2 - 5.5", Toast.LENGTH_SHORT).show();
        }






    }


    /**
     *Odpowiada za czynnosci zwiazane z nacisnieciem przycisku usun
     */
    public void usunOcene(View view) {


        tytulDoUsuniecia = tytulyOcenionych.getSelectedItem().toString();
        kursDoUsuniecia = ocenione.getSelectedItem().toString();


        UsunOcene usunOcene = new UsunOcene();
        usunOcene.execute("");



    }

    /**
     *Kontroluje, by po wyborze kursu, z ktoego ocene chcemy usunac , tytulu ocen w spinnerze odpowiadaly wybranemu kursowi.
     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Tytuly tytuly = new Tytuly();
        tytuly.execute("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Odpowiada za usuniecie oceny z bazy danych.
     */
    public class UsunOcene extends AsyncTask<String, String, String> {

        String z ="";

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            Wyswietl wyswietl = new Wyswietl();
            wyswietl.execute("");

            Ocenione ocenione = new Ocenione();
            ocenione.execute("");

        }


        @Override
        protected String doInBackground(String... strings) {



            try{


                String usun ="delete from Indeks where kurs ='" + kursDoUsuniecia + "' and tytul='" + tytulDoUsuniecia +"'" ;

                Statement st= con.createStatement();

                st.execute(usun);



            }catch (SQLException e){

                z="blad";
            }








        return z;
        }


    }


    /**
     * Odpowiada za pobranie kursow odpowiadajacych wybranemu semestrowi i specjalizacji i umieszczeniu ich w odpowiednim spinnerze.
     */

    public class Kursy extends AsyncTask<String, String, String> {

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



                String  kursyUzytkownika="select k.liczba_h ,k.cnps, k.nazwa_kursu from  Ustawienia u join Kurs k on u.kurs_wybieralny = k.nazwa_kursu " +

                        "where u.semestr=" + semestr + " and nazwa_uzytkownika='" + uzytkownik +"'  and u.specjalizacja ='" + specjalizacja+ "'";


                Statement kus= con.createStatement();
                ResultSet resultSet1 = kus.executeQuery(kursyUzytkownika);



                while (resultSet1.next()) {

                    kursy.add(resultSet1.getString("nazwa_kursu"));

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
     * Odopowiada za pobranie tytulow ocen z danego kursu.
     */

    public class Tytuly extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            ArrayAdapter<String> spinnerAdapter1= new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_spinner_dropdown_item, tytuly);





            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tytulyOcenionych.setAdapter(spinnerAdapter1);
            spinnerAdapter1.notifyDataSetChanged();

        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                kursDoUsuniecia = ocenione.getSelectedItem().toString();
            }catch (Exception e){

            }

            try{


                String oce= " select * from Indeks where semestr=" + semestr + " and uzytkownik='" + uzytkownik +"'  and specjalizacja ='" + specjalizacja+ "' and kurs ='" + kursDoUsuniecia + "'";


                Statement statement = con.createStatement();

                ResultSet rs= statement.executeQuery(oce);


                while(rs.next()){


                    tytuly.add(rs.getString("tytul"));
                }




            }catch (SQLException e ){

                e.printStackTrace();
                z="blad";
            }


            return z;
        }
    }


    /**
     * Pobiera nazwy kursow dla ktorych uzytkownik dodal ocene.
     */

    public class Ocenione extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<String>(getApplicationContext() ,android.R.layout.simple_spinner_dropdown_item, ocenioneKursy);




            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ocenione.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();

            Tytuly tytuly = new Tytuly();
            tytuly.execute("");

        }







        @Override
        protected String doInBackground(String... strings) {


            ocenioneKursy.clear();
            tytuly.clear();


          try{


              String oce= " select Distinct kurs   from Indeks where semestr=" + semestr + " and uzytkownik='" + uzytkownik +"'  and specjalizacja ='" + specjalizacja+ "'";


              Statement statement = con.createStatement();

              ResultSet rs= statement.executeQuery(oce);


              while(rs.next()){

                  ocenioneKursy.add(rs.getString("kurs"));

              }




          }catch (SQLException e ){

              z="blad";
          }





            return z;
        }
    }// koniec kursy


    /**
     * Dodaje ocene do bazy danych.
     */

    public  class Dodaj extends AsyncTask<String ,String,String >{

        String z="";



        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            Ocenione ocenione = new Ocenione();
            ocenione.execute("");

            Wyswietl wyswietl = new Wyswietl();
            wyswietl.execute("");

        }


        @Override
        protected String doInBackground(String... strings) {


            String tytul = tytu.getText().toString();
            String kurs = kursSpinner.getSelectedItem().toString();
            Float ocena = Float.parseFloat(oce.getText().toString());

            ArrayList<String> tytulyOcen = new ArrayList<>();


            boolean zgodne= false;

            try{

                String sprawdz = "Select * from Indeks where uzytkownik='" + uzytkownik + "' and kurs='" + kurs + "' and semestr=" + semestr + " and specjalizacja='" + specjalizacja + "'";

                Statement spr = con.createStatement();

                ResultSet resultSet= spr.executeQuery(sprawdz);

                while(resultSet.next()){

                    tytulyOcen.add(resultSet.getString("tytul"));

                }



                    for(int i = 0 ; i<tytulyOcen.size();i++) {

                        if(tytul.equals(tytulyOcen.get(i))){

                            zgodne = true;

                            z="Istnieje juz ocena z tego kursu o tym tytule!";
                        }

                    }



                    if(zgodne == false) {
                        String dodajOcene = "insert into Indeks(uzytkownik, kurs, tytul, ocena, semestr,specjalizacja) " +
                                " values ('" + uzytkownik + "','" + kurs + "','" + tytul + "'," + ocena + "," + semestr + ",'" + specjalizacja + "')";


                        Statement dod = con.createStatement();
                        dod.execute(dodajOcene);

                        z = "Dodano";
                    }

            }catch (SQLException e){
                e.printStackTrace();
                z="blad";
            }




            return z;
        }
    }// koniec dodaj


    /**
     * Odpowiada za dodanie ocen z kursow do ListView.
     */


    public  class Wyswietl extends AsyncTask<String ,String,String >{

        String z="";

        ArrayAdapter<String> adapter;



        public ArrayList<String> wyniki = new ArrayList<>();


        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


            adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, wyniki);
            listaOcen.setAdapter(adapter);


        }


        @Override
        protected String doInBackground(String... strings) {


            try{

                String wys = "Select * from Indeks where uzytkownik ='" + uzytkownik + "' and semestr=" + semestr + " and specjalizacja='" + specjalizacja + "' order by kurs asc";
                Statement wyni=con.createStatement();
                ResultSet rs=wyni.executeQuery(wys);

                while(rs.next()){

                    wyniki.add(rs.getString("kurs") + "| Ocena : " + rs.getInt("ocena") + "| " + rs.getString("tytul") );

                }





            }catch (SQLException e){
                e.printStackTrace();
                z="Blad";
            }



            return z;
        }
    }// koniec wyswietl

}

