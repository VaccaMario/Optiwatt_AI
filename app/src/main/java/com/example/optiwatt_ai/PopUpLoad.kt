package com.example.optiwatt_ai

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

class PopUpLoad : AppCompatActivity() {

    lateinit var Immagine: ImageView
    lateinit var INPUTImage: Button


    lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pop_up_load)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        Immagine = findViewById(R.id.Immagine)
        INPUTImage = findViewById(R.id.btnSelezionaImmagine)


        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Immagine.setImageURI(it)
                salvaBollettaNelDatabase(this, it)
            }
        }


        INPUTImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }
}
