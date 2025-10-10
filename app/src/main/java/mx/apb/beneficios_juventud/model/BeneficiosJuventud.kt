package mx.apb.beneficios_juventud.model

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * Modelo de la aplicación
 */

class BeneficiosJuventud {
    var correo: String = ""
        //private set, PORQUE LO QUEREMOS PRIVATE???????
    var celular: String = ""
        private set
}