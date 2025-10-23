package mx.apb.beneficios_juventud.model.API.response

import com.google.gson.annotations.SerializedName

data class QrResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: QrData?,
    @SerializedName("message") val message: String?
)

data class QrData(
    @SerializedName("qrCode") val qrCodeDataUrl: String, // "data:image/png;base64,...."
    @SerializedName("expiresAt") val expiresAtEpochSec: Long
)