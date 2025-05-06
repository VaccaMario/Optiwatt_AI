package com.example.optiwatt_ai

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OptiwattApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val db = AppDatabase.getInstance(this)

        Log.d("OptiwattApp", "Database Room inizializzato: ${db.openHelper.databaseName}")

        // Usiamo una coroutine per accedere a funzioni suspend del DAO
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = db.bollettaDao()
                val bollette = dao.prendiTutte()

                if (bollette.isEmpty()) {
                    dao.inserisci(
                        Bolletta(
                            uriImmagine = "init",
                            consumoTotale = 0.0,
                            costoTotale = 0.0,
                            emissioniCO2 = 0.0,
                            previsioneCosto = 0.0,
                            nomeFornitore = "default",
                            previsioneConsumo = 0.0,
                            data = "2025-01-01",
                            tip1 = "init",
                            tip2 = "init",
                            tip3 = "init"
                        )
                    )
                    Log.d("OptiwattApp", "Bolletta dummy inserita")
                } else {
                    Log.d("OptiwattApp", "Database già popolato: ${bollette.size} bollette")
                }
            } catch (e: Exception) {
                Log.e("OptiwattApp", "Errore durante l'inserimento iniziale: ${e.message}")
            }
        }
    }
}
