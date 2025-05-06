package com.example.optiwatt_ai

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class PopUpLoad : AppCompatActivity() {

    private lateinit var immagine: ImageView
    private lateinit var bottoneSeleziona: Button
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_load)

        immagine = findViewById(R.id.Immagine)
        bottoneSeleziona = findViewById(R.id.btnSelezionaImmagine)

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                immagine.setImageURI(it)
                salvaBollettaNelDatabase(this, it)
                bottoneSeleziona.text = "Immagine caricata ✅"
            } ?: Toast.makeText(this, "Nessuna immagine selezionata", Toast.LENGTH_SHORT).show()
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                pickImageLauncher.launch("image/*")
            } else {
                Toast.makeText(this, "Permesso negato. Impossibile aprire la galleria.", Toast.LENGTH_LONG).show()
            }
        }

        bottoneSeleziona.setOnClickListener {
            controllaPermessiEOttieniImmagine()
        }
    }

    private fun controllaPermessiEOttieniImmagine() {
        val permesso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val statoPermesso = ContextCompat.checkSelfPermission(this, permesso)

        if (statoPermesso == PermissionChecker.PERMISSION_GRANTED) {
            pickImageLauncher.launch("image/*")
        } else {
            requestPermissionLauncher.launch(permesso)
        }
    }

    private fun salvaBollettaNelDatabase(context: Context, imageUri: Uri) {
        val db = AppDatabase.getInstance(context)
        val dao = db.bollettaDao()

        val nuovaBolletta = Bolletta(
            uriImmagine = imageUri.toString(),
            consumoTotale = 350.0,
            costoTotale = 78.5,
            emissioniCO2 = 20.4,
            previsioneCosto = 82.0,
            nomeFornitore = "Edison",
            previsioneConsumo = 365.0,
            data = "12/05/2023",
            tip1 = "Spegni gli elettrodomestici in standby",
            tip2 = "Preferisci i cicli eco",
            tip3 = "Controlla l’isolamento termico"
        )

        lifecycleScope.launch {
            dao.inserisci(nuovaBolletta)
            Toast.makeText(context, "Bolletta salvata nel database!", Toast.LENGTH_SHORT).show()
        }
    }
}
