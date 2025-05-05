package com.example.optiwatt_ai.bolletta

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import com.example.optiwatt_ai.bolletta.Bolletta
import com.example.optiwatt_ai.bolletta.AppDatabase

fun salvaBollettaNelDatabase(context: Context, uri: Uri) {
    val nuovaBolletta = Bolletta(
        uriImmagine = uri.toString(),
        consumoTotale = 0.0,
        costoTotale = 0.0,
        emissioniCO2 = 0.0,
        previsioneCosto = 0.0,
        nomeFornitore = "",
        previsioneConsumo = 0.0,
        tip1 = "",
        tip2 = "",
        tip3 = ""
    )

    CoroutineScope(Dispatchers.IO).launch {
        AppDatabase.getInstance(context).bollettaDao().inserisci(nuovaBolletta)
    }
}
