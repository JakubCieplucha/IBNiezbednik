package com.example.ibniezbednik;

import android.app.Activity;
import android.os.Bundle;

/**
 * Aktywnosc wyswietlajaca mape kampusu Politechniki.
 */
public class Mapa extends Activity {


    private String specjalizacja;
    private  String uzytkownik;
    private Integer semestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
    }
}
