package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Ranking uzytkownikow poz wzgledem maksymalnego czasu zmarnowanego.
 */
public class RankingLen extends Activity {

    private ListView listaLeni;
    private  int counter=0;


    private String uzytkownik;
    private  int semestr;
    private String specjalizacja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_len);

        if (getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("Bundle");
                uzytkownik = bundle.getString("uzytkownik");
                semestr = bundle.getInt("semestr");
                specjalizacja = bundle.getString("specjalizacja");


            } catch (NullPointerException e) {
                System.out.println("Brak bundle");
            }

        }


        Wyswietl1 wyswietl= new Wyswietl1();
        wyswietl.execute("");

    }

    /**
     *Odpowiedzialna za powrot do menu.
     */

    public void wrocDoMenu (View view){

        Intent menu = new Intent(getApplicationContext(), Menu.class);

        Bundle bundle = new Bundle();
        bundle.putInt("semestr",semestr);
        bundle.putString("specjalizacja",specjalizacja);
        bundle.putString("uzytkownik",uzytkownik);
        menu.putExtra("Bundle",bundle);

        startActivity(menu);


    }


    /**
     * Dodaje uzytkownikow wraz z wynikami do ListView.
     */
    public class Wyswietl1 extends AsyncTask<String, String, String> {

        ArrayAdapter<String> adapter;

        String z = "";

        public ArrayList<String> wyniki = new ArrayList<>();


        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            listaLeni = (ListView) findViewById(R.id.listaLeni);

            adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, wyniki);

            listaLeni.setAdapter(adapter);

        }


        @Override
        protected String doInBackground(String... strings) {




            try {
                String najlepsi = "select top 5  * from RankinLen order by max_czas_zmarnowany desc";

                Statement znajdz = con.createStatement();
                ResultSet resultSet = znajdz.executeQuery(najlepsi);



                while (resultSet.next()) {

                    counter++;

                    String user = resultSet.getString("uzytkownik");
                    float max = resultSet.getFloat("max_czas_zmarnowany");

                    int hZmarnowane=((int) max);

                    double minZmanrnowane= (max - hZmarnowane) * 60 ;




                    wyniki.add(counter + "." + user + " | Czas zmarnowany : " + hZmarnowane + " h " + (int) minZmanrnowane + " min" );

                }

                z="TOP 5 ";


            } catch (SQLException e) {

                z="blad";

                e.printStackTrace();
            }


            return z;
        }
    }
}



