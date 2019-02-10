package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Aktywnosc odpowiedzialna za obliczenie potencjalnego deficytu studenta.
 */

public class LicznikDeficytu extends Activity {


    private String specjalizacja;
    private  String uzytkownik;
    private Integer semestr;


    private  boolean done=false;

    private CheckBox kurs1;
    private CheckBox kurs2;
    private CheckBox kurs3;
    private CheckBox kurs4;
    private CheckBox kurs5;
    private CheckBox kurs6;
    private CheckBox kurs7;
    private CheckBox kurs8;
    private CheckBox kurs9;
    private CheckBox kurs10;
    private CheckBox kurs11;
    private CheckBox kurs12;
    private CheckBox kurs13;
    private CheckBox kurs14;
    private CheckBox kurs15;
    private CheckBox kurs16;
    private CheckBox kurs17;
    private CheckBox kurs18;


    private ArrayList<CheckBox> boxy = new ArrayList<>();


    private boolean dodatni = true;

    private TextView wynik;


    private  int dopuszczalny=0;

    private int deficyt=0;

    private ArrayList<Integer> dopuszczalnyDef=new ArrayList<>();

    private ArrayList<String> kursy= new ArrayList<>();

    private  ArrayList<Integer> ectsy=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licznik_deficytu);
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
        }


        ObliczDef oblicz = new ObliczDef();
        oblicz.execute("");



    }

    /**
     * Odpowiada za zmiane widocznosci  ilosci check boxow odpoiwadajacej  ilosci reazlizowanych przez studenta kursow.Przypisanie im odpowiedniej nazwy oraz ilosci punktow ECTS.
     */

    public void sprawdz(){

        switch (semestr) {

            case 1:
                dopuszczalny = dopuszczalnyDef.get(0);
                break;

            case 2:
                dopuszczalny = dopuszczalnyDef.get(1);
                break;

            case 3:
                dopuszczalny = dopuszczalnyDef.get(2);
                break;

            case 4:
                dopuszczalny = dopuszczalnyDef.get(3);
                break;

            case 5:
                dopuszczalny = dopuszczalnyDef.get(4);
                break;

            case 6:
                dopuszczalny = dopuszczalnyDef.get(5);
                break;

        }

        kurs1 = (CheckBox) findViewById(R.id.boxKurs1);
        kurs2 = (CheckBox) findViewById(R.id.boxKurs2);
        kurs3 = (CheckBox) findViewById(R.id.boxKurs3);
        kurs4 = (CheckBox) findViewById(R.id.boxKurs4);
        kurs5 = (CheckBox) findViewById(R.id.boxKurs5);
        kurs6 = (CheckBox) findViewById(R.id.boxKurs6);
        kurs7 = (CheckBox) findViewById(R.id.boxKurs7);
        kurs8 = (CheckBox) findViewById(R.id.boxKurs8);
        kurs9 = (CheckBox) findViewById(R.id.boxKurs9);
        kurs10 = (CheckBox) findViewById(R.id.boxKurs10);
        kurs11 = (CheckBox) findViewById(R.id.boxKurs11);
        kurs12 = (CheckBox) findViewById(R.id.boxKurs12);
        kurs13 = (CheckBox) findViewById(R.id.boxKurs13);
        kurs14 = (CheckBox) findViewById(R.id.boxKurs14);
        kurs15 = (CheckBox) findViewById(R.id.boxKurs15);
        kurs16 = (CheckBox) findViewById(R.id.boxKurs16);
        kurs17 = (CheckBox) findViewById(R.id.boxKurs17);
        kurs18 = (CheckBox) findViewById(R.id.boxKurs18);

        boxy.add(kurs1);
        boxy.add(kurs2);
        boxy.add(kurs3);
        boxy.add(kurs4);
        boxy.add(kurs5);
        boxy.add(kurs6);
        boxy.add(kurs7);
        boxy.add(kurs8);
        boxy.add(kurs9);
        boxy.add(kurs10);
        boxy.add(kurs11);
        boxy.add(kurs12);
        boxy.add(kurs13);
        boxy.add(kurs14);
        boxy.add(kurs15);
        boxy.add(kurs16);
        boxy.add(kurs17);
        boxy.add(kurs18);


        int iloscKursow = kursy.size();


        for (int i = 0; i < iloscKursow; i++) {

            boxy.get(i).setVisibility(View.VISIBLE);


        }

        for (int i = 0; i < iloscKursow; i++) {

            boxy.get(i).setText(kursy.get(i));
        }


        kurs1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                deficyt = deficyt + ectsy.get(0);
            }
        });

        kurs2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(1);
            }
        });

        kurs3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(2);
            }
        });

        kurs4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(3);
            }
        });

        kurs5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(4);
            }
        });


        kurs6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(5);
            }
        });


        kurs7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(6);
            }
        });

        kurs8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(7);
            }
        });

        kurs9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(8);
            }
        });

        kurs10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(9);
            }
        });

        kurs11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(10);
            }
        });

        kurs12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(11);
            }
        });

        kurs13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(12);
            }
        });

        kurs14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(13);
            }
        });

        kurs15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(14);
            }
        });

        kurs16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(15);
            }
        });

        kurs17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(16);
            }


        });


        kurs18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deficyt = deficyt + ectsy.get(17);
            }
        });

        wynik = (TextView) findViewById(R.id.txtWynikDef);

    }

    /**
     * Sprawdza czy student przekroczy dopuszczalny deficyt.
     * @return boolean okreslajacy czy student przekroczyl deficyt.
     */
    private  boolean porownaj(){

        if(( dopuszczalny - deficyt ) >= 0){
            dodatni = true;
        }else if (( dopuszczalny - deficyt ) < 0){
            dodatni = false;
        }

        return dodatni;
    }

    /**
     * Wyswietla odpowiednia wiadomosc w zaleznosci od sytuacji studenta.
     * @param view
     */
    public void przeliczDefCklicked(View view) {

        porownaj();

        if(dodatni){
            wynik.setText("Twoj deficyt wynioslby: " + deficyt + " punkty ECTS." + '\n'
                    + "Limit deficytu po " + semestr + " semestrze wynosi " + dopuszczalny + "." + '\n'
                    + "Bez obaw, masz jeszcze szanse zostac inzynierem!"
            );
        }else if (dodatni == false ){
            wynik.setText("Twoj deficyt wynioslby: " + deficyt + "punktow ECTS." + '\n'
                    + "Limit deficytu po " + semestr + " semestrze wynosi " + dopuszczalny + "." + '\n'
                    + "W obecnej sytuacji nie uda Ci sie awansowac na kolejny semestr."
            );


        }




    }

    /**
     * Pobiera dane z bazy.
     */

    public class ObliczDef extends AsyncTask<String,String,String> {

        String z="";

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            done=true;


            sprawdz();

        }// koniec post execute



        @Override
        protected String doInBackground(String... strings) {

            String kursyZsemestru="";


            try {

                String dopuszczalnyDeficyt = "select deficyt from Semestr";
                Statement dop = con.createStatement();
                ResultSet resultSet= dop.executeQuery(dopuszczalnyDeficyt);


                while (resultSet.next()){

                    dopuszczalnyDef.add(resultSet.getInt("deficyt"));
                }


                // dodanie kursow i ectsow

                if(specjalizacja.equals("Informatyka Medyczna")  || specjalizacja.equals("Elektronika Medyczna")  ){

                    kursyZsemestru="select  ects,liczba_h,nazwa_kursu , semestr from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' or specjalizacja ='Informatyka-Elektronika' )";



                }else{

                    kursyZsemestru="select  ects,liczba_h,nazwa_kursu , semestr from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' )";
                }




                Statement kursyy= con.createStatement();
                ResultSet wyynik = kursyy.executeQuery(kursyZsemestru);


                while(wyynik.next()){

                    kursy.add(wyynik.getString("nazwa_kursu"));
                    ectsy.add(wyynik.getInt("ects"));

                }



                String  kursyUzytkownika="select k.liczba_h ,k.ects, k.nazwa_kursu from  Ustawienia u join Kurs k on u.kurs_wybieralny = k.nazwa_kursu " +

                        "where u.semestr=" + semestr + " and nazwa_uzytkownika='" + uzytkownik +"'  and u.specjalizacja ='" + specjalizacja+ "'";


                Statement ku= con.createStatement();
                ResultSet resultSet1 = ku.executeQuery(kursyUzytkownika);

                try {

                    while (resultSet1.next()) {

                        kursy.add(resultSet1.getString("nazwa_kursu"));
                        ectsy.add(resultSet1.getInt("ects"));

                    }

                }catch (SQLException e ){

                    e.printStackTrace();
                    System.out.println("Brak kursow wybierlanych");
                }


                z="niewiedza jest blogoslawienstwem";


            }catch (SQLException e){

                z="blad";

                e.printStackTrace();
            }// try




            return z;
        }// koniec background
    }


}

