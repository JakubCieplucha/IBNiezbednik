package com.example.ibniezbednik;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

/**
 * Sluzy do graficznej reprezentacji uzyskanego wyniku czasu zmarnowanego.
 */
public class WykresyLen extends Activity {


    private  double czasNaUczelni;
    private  double cnps;
    private double wzgleCzWolny;
    private  double bezCzWolny;
    private  double czasZmarnowany;
    private String uzytkownik;
    private  int semestr;
    private String specjalizacja;


    private ArrayList<String> kursy = new ArrayList<>();
    private ArrayList<Integer> cnpsKursu=new ArrayList<>();

    private TextView wyniki;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wykresy_len);

        if(getIntent().hasExtra("Bundle")) {
            try {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("Bundle");
                czasNaUczelni = bundle.getDouble("czasNaUczelni");
                cnps = bundle.getDouble("cnps");
                bezCzWolny = bundle.getDouble("bezCzWolny");
                wzgleCzWolny = bundle.getDouble("wzgleCzWolny");
                czasZmarnowany = bundle.getDouble("czasZmarnowany");
                kursy = (ArrayList<String>) bundle.getSerializable("kursy");
                cnpsKursu = (ArrayList<Integer>) bundle.getSerializable("cnpsKursu");
                uzytkownik = bundle.getString("uzytkownik");
                semestr = bundle.getInt("semestr");
                specjalizacja = bundle.getString("specjalizacja");


            } catch (NullPointerException e) {
                System.out.println("Brak bundle");
            }
        }//

        wyniki=(TextView) findViewById(R.id.wynikiTxt);

        wyniki.append("W czasie ktory zmanrowales, mozna bylo zrealizowac : \n");

        for (int i = 0 ; i<kursy.size();i++){

            double procent = 0;

            procent=  (czasZmarnowany/ cnpsKursu.get(i)) * 100;


            wyniki.append((int)procent + " % kursu " + kursy.get(i) + "\n");
        }





    }// koniec create

    /**
     * Tworzy wykres okreslajacy czesc czasu bezwzglednego zmarnowanego przez uzytkownika.
     */

    public void wykresBezw(View view) {


        double zmarnowano = (czasZmarnowany/bezCzWolny)*100;
        double reszta = 100 - zmarnowano;


        // Pie Chart Section Names
        String[] code = new String[] {
                "Czas zmarnowany : " + (int)zmarnowano +"%" , "Reszta :" + (int)reszta + "%"
        };

        // Pie Chart Section Value
        double[] distribution = {zmarnowano,reszta } ;

        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.RED,
        };

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(" Bezwzgledny czas wolny");
        for(int i=0 ;i < distribution.length;i++){
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
        }



        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distribution.length;i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle(" Bezwzgledny czas wolny");
        defaultRenderer.setChartTitleTextSize(50);
        defaultRenderer.setLabelsTextSize(40);
        defaultRenderer.setLegendTextSize(40);
        defaultRenderer.setZoomButtonsVisible(true);

        // wyswietlanie w linearLayout
        LinearLayout chartLayout=(LinearLayout) findViewById(R.id.lv);
        chartLayout.removeAllViews();

        GraphicalView chartView = ChartFactory.getPieChartView(getBaseContext(), distributionSeries , defaultRenderer);
        chartLayout.addView(chartView);
    }

    /**
     * Tworzy wykres okreslajacy czesc Calkowitego Nakladu Pracy Studenta zmarnowanego przez uzytkownika.
     */

    public void wykresCnps(View view) {


        double zmarnowano = (czasZmarnowany/cnps)*100;
        double reszta = 100 - zmarnowano;


        // Pie Chart Section Names
        String[] code = new String[] {
                "Czas zmarnowany : " + (int)zmarnowano +"%" , "Reszta :" + (int)reszta + "%"
        };

        // Pie Chart Section Value
        double[] distribution = {zmarnowano,reszta } ;

        // Color of each Pie Chart Sections
        int[] colors = { Color.MAGENTA, Color.GREEN,
        };

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(" Calkowity naklad pracy studenta w semestrze");
        for(int i=0 ;i < distribution.length;i++){
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
        }



        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distribution.length;i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle(" Calkowity naklad pracy studenta w semestrze");
        defaultRenderer.setChartTitleTextSize(50);
        defaultRenderer.setLabelsTextSize(40);
        defaultRenderer.setLegendTextSize(40);
        defaultRenderer.setZoomButtonsVisible(true);

        // wyswietlanie w linearLayout
        LinearLayout chartLayout=(LinearLayout) findViewById(R.id.lv);
        chartLayout.removeAllViews();

        GraphicalView chartView = ChartFactory.getPieChartView(getBaseContext(), distributionSeries , defaultRenderer);
        chartLayout.addView(chartView);

    }

    /**
     * Przechodzi do aktywnosci RankingLen.
     */
    public void rankingClicked(View view) {

        Intent ranking = new Intent(getApplicationContext(),RankingLen.class);

        Bundle bundle=new Bundle();

        bundle.putString("specjalizacja",specjalizacja);
        bundle.putString("uzytkownik",uzytkownik);
        bundle.putInt("semestr",semestr);

        ranking.putExtra("Bundle",bundle);

        startActivity(ranking);


    }





}// koniec klasy

