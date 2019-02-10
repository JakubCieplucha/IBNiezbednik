package com.example.ibniezbednik;

/**
 * Klasa sluzaca do obliczania parametrow zwiazanych z czasem zmarnowanym przez studenta.
 */

public class LenIB {
    private int czasDrzemki = 30;
    private  int czasSerialu = 35;
    private  int czasFilmu = 120;
    private double facebook = 64.5;
    private double youtube = 50 ;
    private double snapchat = 11 ;
    private  double instagram = 14 ;
    private double czasCalkowity=168;
    private int czasSnu=42;
    private  int podHig=21;

    public double getFacebook() {
        return facebook;
    }

    public double getYoutube() {
        return youtube;
    }

    public double getSnapchat() {
        return snapchat;
    }

    public double getInstagram() {
        return instagram;
    }

    /**
     * Oblicza wzgledny czas wolny.
     * @param czasNaUczelni ilosc czasu w ciagu tygodnia jaka student spedza na uczelni.
     * @param czasDojazdu czas jaki zajmuje studentowi dojechanie na uczelnie.
     * @param iloscDrzemek ilosc drzemek w ciagu tygodnia.
     * @return wzgledny czas wolny
     */

    public double wzglednyCzasWolny(double czasNaUczelni,double czasDojazdu,double iloscDrzemek){

        double czasDrzemek = iloscDrzemek * czasDrzemki /60 ;

        double wzglCzasWolny = czasCalkowity - (( 10 * czasDojazdu) / 60) - (czasSnu) - czasNaUczelni - czasDrzemek ;

        return  wzglCzasWolny;
    }

    /**
     * Oblicza bezwzgledny czas wolny.
     * @param czasNaUczelni ilosc czasu w ciagu tygodnia jaka student spedza na uczelni.
     * @param czasDojazdu czas jaki zajmuje studentowi dojechanie na uczelnie.
     * @param iloscDrzemek ilosc drzemek w ciagu tygodnia.
     * @return bezwzglednny czas wolny
     */

    public double bezwzglednyCzasWolny(double czasNaUczelni,double czasDojazdu,double iloscDrzemek){

        double czasDrzemek = iloscDrzemek * czasDrzemki /60 ;

        double bezwCzasWolny = czasCalkowity - ((10*czasDojazdu)/60) - (czasSnu) - czasNaUczelni - podHig - czasDrzemek;

        return   bezwCzasWolny;

    }

    /**
     * Oblicza czas zmarnowany.
     * @param iloscSeriali ilosc odcinkow seriali.
     * @param iloscFilmow ilosc filmow .
     * @param czasGry czas zmarnowany na inne aktywnosci.
     * @param fb czy student korzystal z Facebooka.
     * @param yt czy student korzystal z Youtuba.
     * @param snap czy student korzystal z Snapchata.
     * @param insta czy student korzystal z Instagrama.
     * @return czas zmarnowany.
     */

    public   double czasZmarnowany(double iloscSeriali,double iloscFilmow,double czasGry ,double fb ,double yt, double snap, double insta ){

        double czasZmarnowany=0;


        czasZmarnowany = czasGry + ((iloscFilmow*czasFilmu)/60) + ((iloscSeriali * czasSerialu)/60) + ((fb * facebook * 7)/60) + ((yt*youtube * 7)/60) + ((snap * snapchat * 7)/60) + ((insta * instagram * 7)/60);

        return czasZmarnowany;
    }




}

