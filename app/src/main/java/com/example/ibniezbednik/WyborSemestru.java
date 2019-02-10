package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Aktywnosc sluzaca za okreslenie parametrow wykorzystywanych przez funkcjonalnosi aplikacji.
 */
public class WyborSemestru extends Activity {



    private Spinner sem;
    private Spinner wyb;
    private Spinner spec;
    private Button dodaj;
    private TextView wybierzSpec;
    private  TextView dodajKurs;


    private String uzytkownik;
    private  String specjalizacja;
    private int semestr;
    private String kursWybieralny;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybor_semestru);

        if(getIntent().hasExtra("Bundle")) {

            try {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("Bundle");
                uzytkownik = bundle.getString("uzytkownik");
            } catch (NullPointerException e) {
                System.out.println("Brak bundle");
            }
        }



        sem=(Spinner) findViewById(R.id.spinnerSemestr);
        wyb=(Spinner) findViewById(R.id.spinnerWybieralny);
        spec=(Spinner) findViewById(R.id.spinnerSpecjalizacja);
        dodaj=(Button) findViewById(R.id.dodajBtn) ;
        wybierzSpec=(TextView) findViewById(R.id.wybierzSpecTxt);
        dodajKurs=(TextView) findViewById(R.id.dodajKursTxt);


        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                semestr=Integer.parseInt(String.valueOf(sem.getSelectedItem()));


                wybierzSpec.setVisibility(View.INVISIBLE);
                spec.setVisibility(View.INVISIBLE);
                dodajKurs.setVisibility(View.INVISIBLE);
                wyb.setVisibility(View.INVISIBLE);
                dodaj.setVisibility(View.INVISIBLE);
                specjalizacja="brak";

                if(semestr>1){

                    dodajKurs.setVisibility(View.VISIBLE);
                    wyb.setVisibility(View.VISIBLE);
                    dodaj.setVisibility(View.VISIBLE);

                }



                if(semestr>4){

                    wybierzSpec.setVisibility(View.VISIBLE);
                    spec.setVisibility(View.VISIBLE);


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * Uruchamia aktywnosc Menu.
     */
    public void PrzejdzDalej(View view) {

        specjalizacja=String.valueOf(spec.getSelectedItem());
        semestr=Integer.parseInt(String.valueOf(sem.getSelectedItem()));


        Intent menu = new Intent(getApplicationContext(),Menu.class);

        Bundle bundle1 =new Bundle();
        bundle1.putString("uzytkownik",uzytkownik);
        bundle1.putInt("semestr",semestr);
        bundle1.putString("specjalizacja",specjalizacja);
        menu.putExtra("Bundle",bundle1);

        startActivity(menu);


    }

    public void DodajKurs(View view) {

        DodajKurs dodaj=new DodajKurs();
        dodaj.execute("");


    }


    /**
     * Odpowiada za dodanie kursow wybieralnych do danego semestru wybranego przez uzytkownika.
     */
    public class DodajKurs extends AsyncTask<String,String,String> {

        String z="";
        int count=0;




        @Override
        protected String doInBackground(String... strings) {


            kursWybieralny = String.valueOf(wyb.getSelectedItem());
            specjalizacja=String.valueOf(spec.getSelectedItem());
            try{





                String ilosc = "select count(kurs_wybieralny) as wynik from Ustawienia WHERE nazwa_uzytkownika='" + uzytkownik + "' and semestr =" + semestr + "and specjalizacja ='" + specjalizacja + "'" ;
                Statement sprawdzIlosc = con.createStatement();
                ResultSet wynik = sprawdzIlosc.executeQuery(ilosc);


                while(wynik.next()){
                    count=wynik.getInt("wynik");
                }


                if(count>2){

                    z="Wykorzystano limit kursow wybieralnych";

                }else{

                    String dodajUstawienie="insert into Ustawienia(nazwa_uzytkownika, semestr, kurs_wybieralny,specjalizacja) values('" + uzytkownik + "'," + semestr + ",'" + kursWybieralny + "','" + specjalizacja + "')";
                    Statement dodajKurs=con.createStatement();
                    dodajKurs.execute(dodajUstawienie);


                    z="Dodano Kurs";
                }


            } catch (SQLException e) {
                z="Blad polaczenia";
            }


            return z;
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(WyborSemestru.this,r,Toast.LENGTH_SHORT).show();



        }




    }//koniec dodaj



}// koniec klasy

