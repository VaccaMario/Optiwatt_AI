package com.example.optiwatt_ai.bolletta

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bolletta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uriImmagine: String,
    val consumoTotale: Double,
    val costoTotale: Double,
    val emissioniCO2: Double,
    val previsioneCosto: Double,
    val nomeFornitore: String,
    val previsioneConsumo: Double,
    val tip1: String,
    val tip2: String,
    val tip3: String
)
