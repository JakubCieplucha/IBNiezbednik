package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

/**
 * Aktywnosc sluzaca jako menu wyboru funkcjonalonosci aplikacji.
 */

public class Menu extends Activity {

    private String uzytkownik;
    private  String specjalizacja;
    private int semestr;
    private  Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if(getIntent().hasExtra("Bundle")) {

         try {
             Intent intent = getIntent();
             bundle = intent.getBundleExtra("Bundle");
             uzytkownik = bundle.getString("uzytkownik");
             specjalizacja = bundle.getString("specjalizacja");
             semestr = bundle.getInt("semestr");

         }catch (NullPointerException e){
             System.out.println("Brak bundle");
         }
         }


    }

    /**
     * Prejdz ddo attywnosc Indeks.
     */

    public void indeksClicked(View view) {

        Intent indeks=new Intent(getApplicationContext(),Indeks.class);

        indeks.putExtra("Bundle",bundle);

        startActivity(indeks);


    }




    /**
     * Prejdz ddo attywnosc Len.
     */



    public void lenClicked(View view) {

        Intent len=new Intent(getApplicationContext(),Len.class);

        len.putExtra("Bundle",bundle);

        startActivity(len);


    }

    /**
     * Prejdz ddo attywnosc Licznik Deficytu.
     */


    public void licznikClicked(View view) {

        Intent licznik = new Intent(getApplicationContext(),LicznikDeficytu.class);

        licznik.putExtra("Bundle",bundle);

        startActivity(licznik);

    }

    /**
     * Prejdz ddo attywnosc Mapa.
     */


    public void mapaClicked(View view) {


        Intent mapa= new Intent(getApplicationContext(),Mapa.class);
        startActivity(mapa);

    }


    /**
     * Prejdz ddo attywnosc Dane.
     */


    public void daneClicked(View view) {

        Intent dane=new Intent(getApplicationContext(), Dane.class);

        dane.putExtra("Bundle",bundle);

        startActivity(dane);

    }


    /**
     * Prejdz ddo attywnosc Rnaking Kursow.
     */

    public void rankingClicked(View view) {


        Intent rankingKursow = new Intent(getApplicationContext(),RankingKursow.class);

        rankingKursow.putExtra("Bundle",bundle);

        startActivity(rankingKursow);

    }

    /**
     * Prejdz ddo attywnosc budzik.
     */

    public void budzikClicked(View view) {

        Intent budzik = new Intent(getApplicationContext(),Budzik.class);
        budzik.putExtra("Bundle",bundle);
        startActivity(budzik);

    }
}
