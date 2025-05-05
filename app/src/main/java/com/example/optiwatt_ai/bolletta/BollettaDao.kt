import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BollettaDao {
    @Insert
    suspend fun inserisci(bolletta: Bolletta)

    @Query("SELECT * FROM Bolletta ORDER BY id DESC")
    suspend fun prendiTutte(): List<Bolletta>
}
// DADDA: tutte le query che l’intelligenza artificiale dovrà fare al database vanno scritte QUI.
// Se devi filtrare bollette per costo, consumo, fornitore, periodo, ecc. scrivilo usando @Query
// È anche qui che puoi creare query personalizzate per recuperare i dati che l’IA deve analizzare.
// Ogni query può restituire una lista, un valore, o anche modificare le righe.
//
// Esempio per Dada (da cancellare dopo):
// @Query("SELECT * FROM Bolletta WHERE costoTotale > 100")
// suspend fun bolletteCostose(): List<Bolletta>