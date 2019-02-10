package com.example.ibniezbednik;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.ibniezbednik.Logowanie.con;

/**
 * Aktywnosc odpowiedzialna za obliczanie czasu zmarnowanego w ciagu tygodnia przez uzytkownika.
 */

public class Len extends AppCompatActivity {




    private  LenIB lenIB = new LenIB();
    private String specjalizacja;
    private Integer semestr;
    private EditText czas;
    private   EditText ilosc;
    private   EditText seri;
    private   EditText fil;
    private  EditText gry;


    private String uzytkownik;

    private TextView wynik;

    private CheckBox yt;
    private CheckBox fb;
    private CheckBox in;
    private CheckBox sn;

    private  Integer youtube=0;
    private  Integer facebook=0;
    private  Integer snapchat=0;
    private  Integer instagram=0;

    private  double czasNaUczelni;
    private  double cnps;
    private double wzgleCzWolny;
    private  double bezCzWolny;
    private  double czasZmarnowany;
    private double iloscHGry=0;

    private  double czasDojazdu=0;
    private  double iloscDrzemek=0;
    private  double iloscSeriali=0;
    private  double iloscFilmow=0;

    private  boolean done=false;





    ArrayList<String> kursy = new ArrayList<>();
    ArrayList<Integer> cnpsKursu=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_len);
        if(getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("Bundle");
                specjalizacja = bundle.getString("specjalizacja");
                semestr = bundle.getInt("semestr");
                uzytkownik = bundle.getString("uzytkownik");




            }catch (NullPointerException e){
                System.out.println("Brak bundle");
            }
        }

        czas = (EditText) findViewById(R.id.dojazdTxt);
        ilosc = (EditText) findViewById(R.id.drzemkiTxt);
        seri = (EditText) findViewById(R.id.serialeTxt);
        fil = (EditText) findViewById(R.id.filmyTxt);
        gry= (EditText) findViewById(R.id.gryTxt) ;

        wynik = (TextView) findViewById(R.id.wynnikView);

        yt = (CheckBox) findViewById(R.id.checkYT);
        fb = (CheckBox) findViewById(R.id.checkFB);
        in = (CheckBox) findViewById(R.id.checkInsta);
        sn = (CheckBox) findViewById(R.id.checkSnap);


        yt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    youtube = 1;

                }

            }
        });

        fb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                facebook = 1;

            }
        });


        in.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                instagram = 1;

            }
        });

        sn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                snapchat = 1;

            }
        });


    }


    /**
     * Oblicza czas zmarnowany.
     */

    public class ObliczZmarnowany extends AsyncTask<String,String,String> {

        String z = "";



        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(),r,Toast.LENGTH_SHORT).show();

            String zmarnowany="Czas zmarnowany w ciagu tygodnia: ";




            int hZmarnowane=((int) czasZmarnowany);
            double minZmanrnowane= (czasZmarnowany - hZmarnowane) * 60 ;



            wynik.setText(zmarnowany +  hZmarnowane + " h " + (int)minZmanrnowane + " min." );


        }



        @Override
        protected String doInBackground(String... strings) {
            z="";

            String kursyZsemestru="";

            try{


                if(specjalizacja.equals("Informatyka Medyczna")  || specjalizacja.equals("Elektronika Medyczna")  ){

                    kursyZsemestru="select  cnps,liczba_h,nazwa_kursu , semestr from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' or specjalizacja ='Informatyka-Elektronika' )";



                }else{

                    kursyZsemestru="select  cnps,liczba_h,nazwa_kursu , semestr from Kurs where semestr =" + semestr + "" +
                            " and (specjalizacja='" + specjalizacja + "' or specjalizacja='Ogolne' )";
                }



                Statement godziny= con.createStatement();
                ResultSet resultSet = godziny.executeQuery(kursyZsemestru);


                while(resultSet.next()){

                    kursy.add(resultSet.getString("nazwa_kursu"));
                    cnpsKursu.add(resultSet.getInt("cnps"));
                    czasNaUczelni = czasNaUczelni + resultSet.getInt("liczba_h");
                    cnps = cnps + resultSet.getInt("cnps");

                }



                String  kursyUzytkownika="select k.liczba_h ,k.cnps, k.nazwa_kursu from  Ustawienia u join Kurs k on u.kurs_wybieralny = k.nazwa_kursu " +

                        "where u.semestr=" + semestr + " and nazwa_uzytkownika='" + uzytkownik +"'  and u.specjalizacja ='" + specjalizacja+ "'";


                Statement godz= con.createStatement();
                ResultSet resultSet1 = godz.executeQuery(kursyUzytkownika);

                try {

                    while (resultSet1.next()) {

                        kursy.add(resultSet1.getString("nazwa_kursu"));
                        cnpsKursu.add(resultSet1.getInt("cnps"));
                        czasNaUczelni = czasNaUczelni + resultSet1.getInt("liczba_h");
                        cnps = cnps + resultSet1.getInt("cnps");

                    }

                }catch (SQLException e ){

                    e.printStackTrace();
                    System.out.println("Brak kursow wybierlanych");
                }




                bezCzWolny = lenIB.bezwzglednyCzasWolny(czasNaUczelni, czasDojazdu, iloscDrzemek);
                wzgleCzWolny = lenIB.wzglednyCzasWolny(czasNaUczelni, czasDojazdu, iloscDrzemek);

                czasZmarnowany = lenIB.czasZmarnowany(iloscSeriali, iloscFilmow, iloscHGry, facebook, youtube, snapchat, instagram);



                String sprawdzRanking ="select * from RankinLen where uzytkownik='" + uzytkownik + "'";

                Statement ranking=con.createStatement();
                ResultSet wynikRankingu=ranking.executeQuery(sprawdzRanking);

                if (!wynikRankingu.isBeforeFirst() ) {

                    String wprowadzMax="insert into RankinLen(uzytkownik, max_czas_zmarnowany, data_wpisania) " +
                            "values('" + uzytkownik + "',"+czasZmarnowany+",GETDATE())";

                    Statement wprowadz=con.createStatement();
                    wprowadz.execute(wprowadzMax);

                    z="Dodano do rankingu";

                }else {

                    float doPorownania=0;

                    while (wynikRankingu.next()) {
                        doPorownania=wynikRankingu.getFloat("max_czas_zmarnowany");


                    }

                    if (czasZmarnowany > doPorownania) {

                        String zmienMaxWynik="update RankinLen set " +
                                "max_czas_zmarnowany=" + (float)czasZmarnowany +"," +
                                "data_wpisania=GETDATE() " +
                                "where uzytkownik='" + uzytkownik +"'";


                        Statement zwieksz= con.createStatement();
                        zwieksz.execute(zmienMaxWynik);

                        z="Nowy najepszy wynik";

                    }else {

                        z = "Nie najlepszy wynik";
                    }
                }

            }catch (Exception e) {


                e.printStackTrace();
                z="Blad";
            }

            return z;
        }
    }

    /**
     * Czynnosci wykonywane po wcisnieciu oblicz.
     */


    public void obliczClicked(View view) {

        done = true;

        try {
            czasDojazdu = Double.parseDouble(czas.getText().toString());
        }catch (Exception e){
            System.out.println("Nie podano czasu dojazdu na uczelnie");
        }


        try {
            iloscDrzemek = Double.parseDouble(ilosc.getText().toString());
        }catch (Exception e){
            System.out.println("Nie podano ilosci drzemek");
        }


        try {
            iloscFilmow = Double.parseDouble(fil.getText().toString());
        }catch (Exception e){
            System.out.println("Nie podano ilosci filmow");
        }


        try {
            iloscSeriali = Double.parseDouble(seri.getText().toString());
        }catch (Exception e){
            System.out.println("Nie podano ilosci seriali");
        }


        try {
            iloscHGry = Double.parseDouble(gry.getText().toString());

        }catch (Exception e){
            System.out.println("Nie podano godzin gry ");
        }

        ObliczZmarnowany oblicz=new ObliczZmarnowany();
        oblicz.execute("");

    }
    /**
     * Odpowiada za przejscie do kolejnej aktywnosci.
     */

    public void dalejClicked(View view) {

        if (done) {

            Bundle dane = new Bundle();

            dane.putDouble("czasNaUczelni", czasNaUczelni);
            dane.putDouble("cnps", cnps);
            dane.putDouble("bezCzWolny", bezCzWolny);
            dane.putDouble("wzgleCzWolny", wzgleCzWolny);
            dane.putDouble("czasZmarnowany", czasZmarnowany);
            dane.putSerializable("kursy",kursy);
            dane.putSerializable("cnpsKursu",cnpsKursu);
            dane.putString("uzytkownik",uzytkownik);
            dane.putString("specjalizacja",specjalizacja);
            dane.putInt("semestr",semestr);

            Intent wykresy= new Intent(getApplicationContext(),WykresyLen.class);
            wykresy.putExtra("Bundle",dane);
            startActivity(wykresy);


        }

    }


}



