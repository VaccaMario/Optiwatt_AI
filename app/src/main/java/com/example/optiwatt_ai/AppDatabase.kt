package com.example.optiwatt_ai// Questo file definisce il database Room dell’applicazione
// Contiene la configurazione generale e restituisce l’accesso al DAO (Data Access Object)

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Definiamo il database Room
// - entities = la lista delle tabelle che il database gestisce (in questo caso solo "Bolletta")
// - version = numero di versione del DB. Va aumentato se cambiamo la struttura.
@Database(entities = [Bolletta::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Funzione astratta che ci restituisce il DAO per accedere alla tabella Bolletta
    abstract fun bollettaDao(): BollettaDao

    companion object {
        // L'istanza singleton del database per evitare che venga creata più volte
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Funzione che restituisce l'istanza del database
        // Se non esiste, la crea una sola volta in modo thread-safe
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Costruzione vera e propria del database usando Room
                val instance = Room.databaseBuilder(
                    context.applicationContext,           // usiamo il contesto dell'app per evitare memory leak
                    AppDatabase::class.java,              // classe che estende RoomDatabase
                    "bolletta_db"                         // nome fisico del file del database
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
//https://developer.android.com/training/data-storage/room?hl=it link da cui preso il File