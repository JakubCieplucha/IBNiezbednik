package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Aktywnosc odpowiadajaca za logowanie do aplikacji.
 */
public class Logowanie extends Activity {


    public static Connection con;
    private ConnectionClass connectionClass;

    private EditText uzy;
    private EditText has;
    private Button logowanie;

    private String uzytkownik;
    private   String haslo;

    private boolean isSuccess=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        connectionClass = new ConnectionClass();
        uzy=(EditText) findViewById(R.id.nazwaUzytkownikaTxt);
        has=(EditText) findViewById(R.id.haslooTxt);
        logowanie=(Button) findViewById(R.id.zalogujBtn);


        logowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uzytkownik=uzy.getText().toString();
                haslo=has.getText().toString();

                Loguj loguj=new Loguj();
                loguj.execute("");

            }
        });



    }

    /**
     *Przejscie do aktywnosci rejestracji.
     */

    public void stworzKonto(View view) {

        Intent rejestracja = new Intent(getApplicationContext(), Rejestracja.class);



        startActivity(rejestracja);

    }

    /**
     * Odpowiada za porownanie podanych danych z istniejacymi w aplikacji i w razie powodzenia przejscia do kolejnej aktywwnosci.
     */

    public class Loguj extends AsyncTask<String,String,String> {

        String z="";


        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(Logowanie.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                //przejscie do aktywnosci


                Intent wyborSemestru= new Intent(getApplicationContext(),WyborSemestru.class);

                Bundle bundle = new Bundle();
                bundle.putString("uzytkownik",uzytkownik);
                wyborSemestru.putExtra("Bundle",bundle);
                startActivity(wyborSemestru);


            }


        }



        @Override
        protected String doInBackground(String... strings) {

            String hasloDoPorownania="";
            int zablokowanie=0;

            try{

                con = connectionClass.CONN();


                if(con==null){

                    z="Blad przy laczeniu z baza danych ";
                }else{

                    String sprawdzanie = "SELECT haslo FROM Uzytkownicy where nazwa_uzytkownika='"+uzytkownik+"'";
                    Statement porownanie = con.createStatement();
                    ResultSet rs= porownanie.executeQuery(sprawdzanie);

                    String zabezpieczenie = "Select zabezpieczenie from Uzytkownicy  where nazwa_uzytkownika='"+uzytkownik+"'";
                    Statement sprawdz = con.createStatement();
                    ResultSet blokada = sprawdz.executeQuery(zabezpieczenie);

                    while(blokada.next()){
                        zablokowanie=blokada.getInt("zabezpieczenie");
                    }

                    if (!rs.isBeforeFirst() ) {

                        z="Uzytkownik nie istnieje! ";

                    }else if(zablokowanie>3){

                        z="Bledne haslo zostalo wprowadzone zbyt duzo razy!Skontaktuj sie z administratorem.";

                    }
                    else{

                        while(rs.next()) {

                            hasloDoPorownania = rs.getString("haslo");

                        }
                        if(haslo.equals(hasloDoPorownania)){


                            z="Haslo poprawne ";

                            isSuccess=true;

                        }else{

                            String blad= "use NiezbednikIB EXEC dbo.PodaneBledneHaslo '" + uzytkownik + "'" ;
                            Statement bledneHaslo = con.createStatement();
                            bledneHaslo.execute(blad);

                            z="Bledne  haslo";

                        }


                    }// uzytkownik istnieje

                }//koniec else




            }catch(Exception e){

                z="Niespodziewany blad";
                e.printStackTrace();
            }

            return z;
        }
    }

}
