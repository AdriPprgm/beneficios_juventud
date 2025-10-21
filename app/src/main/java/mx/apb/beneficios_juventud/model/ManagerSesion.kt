package mx.apb.beneficios_juventud.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Maneja el token de la sesion del usuario utilizando un Jetpack DataStore.
 * Esta clase maneja el guardado del auth token, la obtencion del auth token y
 * la eliminacion del auth token.
 *
 * @param context Contexto de la aplicacion.
 * @author Adrián Proaño Bernal
 */
class ManagerSesion(private val context: Context){
    companion object{
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    /**
     * Salva el token de autenticacion a Datastore
     * Se hace suspend fun ction porque las operaciones de DataStore son asincronas
     * @param token Token de autenticacion a guardar.
     * @author Adrian Proaño Bernal
     */
    suspend fun saveAuthToken(token: String){
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    /**
     * Obtiene el token de autenticacion de DataStore como un Flow
     * @return Un flow que emite el token de autenticacion o nulo si no existe.
     * @author Adrian Proaño Bernal
     */
    val authToken: Flow<String?> = context.dataStore.data.map{ preferences ->
        preferences[AUTH_TOKEN_KEY]
    }

    /**
     * Elimina el token de autenticacion de DataStore
     * @author Adrian Proaño Bernal
     */
    suspend fun clearAuthToken(){
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }
}
