package com.example.fitcore.application.config

import android.content.Context
import coil.ImageLoader
import coil.decode.DataSource
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import okhttp3.OkHttpClient
import java.io.File

object CoilConfig {
    fun getImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .diskCache {
                DiskCache.Builder()
                    .directory(File(context.cacheDir, "image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024) // 50MB
                    .build()
            }
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizeBytes(16 * 1024 * 1024) // 16MB
                    .build()
            }
            .respectCacheHeaders(false) // Ignore server cache headers
            .crossfade(true)
            .build()
    }

    fun getImageRequest(context: Context, url: String?): ImageRequest {
        return ImageRequest.Builder(context)
            .data(url)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .listener(
                onError = { _, result ->
                    // Log any errors without crashing
                    result.throwable.printStackTrace()
                }
            )
            .build()
    }
}
