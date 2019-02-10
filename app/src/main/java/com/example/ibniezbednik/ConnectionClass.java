package com.example.ibniezbednik;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Odpowiada za nawiazywanie lacznosci z baza danych.
 */

public class ConnectionClass {


    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {



            ConnURL=  "jdbc:jtds:sqlserver://230331sqlserverniezbednik.database.windows.net:1433;databaseName=NiezbednikIB;user=User@230331sqlserverniezbednik;password=Password;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            conn = DriverManager.getConnection(ConnURL);

        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());

        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }



}

