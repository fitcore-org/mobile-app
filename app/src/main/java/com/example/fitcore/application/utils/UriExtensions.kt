package com.example.fitcore.application.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream

/**
 * Extensão que converte um URI para um arquivo temporário.
 * Útil quando precisamos converter um URI da galeria para um arquivo que pode ser enviado via API.
 */
fun Uri.toTempFile(contentResolver: ContentResolver): File? {
    return try {
        // Obtém o tipo MIME do conteúdo
        val mimeType = contentResolver.getType(this)
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType) ?: ""

        // Cria um arquivo temporário com a extensão correta
        val tempFile = File.createTempFile(
            "upload_",
            ".$extension",
            File(System.getProperty("java.io.tmpdir"))
        )

        // Copia o conteúdo do URI para o arquivo temporário
        contentResolver.openInputStream(this)?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Obtém o caminho real do arquivo a partir de um URI de mídia.
 */
fun Uri.getRealPath(contentResolver: ContentResolver): String? {
    var path: String? = null
    try {
        // Se for um URI de mídia, tenta obter o caminho real do arquivo
        contentResolver.query(
            this, arrayOf(MediaStore.Images.Media.DATA),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = cursor.getString(columnIndex)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return path
}
