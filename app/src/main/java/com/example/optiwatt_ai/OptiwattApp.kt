package com.example.optiwatt_ai

import android.app.Application
import android.util.Log

class OptiwattApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inizializza il database Room all’avvio dell’app
        val db = AppDatabase.getInstance(this)

        // Log per confermare che il database è stato inizializzato o aperto
        Log.d("OptiwattApp", "Database Room inizializzato: ${db.openHelper.databaseName}")
    }
}
