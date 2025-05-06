package com.example.optiwatt_ai

import android.Manifest
import android.content.Context
import android.content.Intent
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
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class INPUT_img : AppCompatActivity() {

    lateinit var immagine: ImageView
    lateinit var bottoneSeleziona: Button
    lateinit var ImageLauncher: ActivityResultLauncher<String>
    lateinit var PermessiL: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_img)

        immagine = findViewById(R.id.Immagine)
        bottoneSeleziona = findViewById(R.id.btnSelezionaImmagine)

        ImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                immagine.setImageURI(it)
                salvaBollettaNelDatabase(this, it)
                bottoneSeleziona.text = "Immagine caricata"
            } ?: Toast.makeText(this, "Nessuna immagine selezionata", Toast.LENGTH_SHORT).show()
        }

        PermessiL = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                ImageLauncher.launch("image/*")
            } else {
                Toast.makeText(this, "Permesso negato. Impossibile aprire la galleria.", Toast.LENGTH_LONG).show()
            }
        }

        bottoneSeleziona.setOnClickListener {
            controllaPermessiEOttieniImmagine()
        }
        val bottoneind = findViewById<Button>(R.id.BottoneIndietro)
        bottoneind.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)        }
    }

    fun controllaPermessiEOttieniImmagine() {
        val permesso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val statoPermesso = ContextCompat.checkSelfPermission(this, permesso)

        if (statoPermesso == PermissionChecker.PERMISSION_GRANTED) {
            ImageLauncher.launch("image/*")
        } else {
            PermessiL.launch(permesso)
        }
    }



    fun copiaImmagineInternamente(context: Context, imageUri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            val fileName = "bolletta_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun salvaBollettaNelDatabase(context: Context, imageUri: Uri) {
        val db = AppDatabase.getInstance(context)
        val dao = db.bollettaDao()

        val pathLocale = copiaImmagineInternamente(context, imageUri)

        if (pathLocale != null) {
            val nuovaBolletta = Bolletta(
                uriImmagine = pathLocale,
                consumoTotale = 0.0,
                costoTotale = 0.0,
                emissioniCO2 = 0.0,
                previsioneCosto = 0.0,
                nomeFornitore = " ",
                previsioneConsumo = 0.0,
                data = " ",
                tip1 = " ",
                tip2 = " ",
                tip3 = " "
            )

            lifecycleScope.launch {
                dao.inserisci(nuovaBolletta)
                Toast.makeText(context, "Bolletta salvata internamente!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Errore nel salvataggio dell'immagine", Toast.LENGTH_SHORT).show()
        }
    }
}
