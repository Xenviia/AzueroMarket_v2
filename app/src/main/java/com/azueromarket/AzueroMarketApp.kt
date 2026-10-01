package com.azueromarket

import android.app.Application
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

class AzueroMarketApp : Application() {

    companion object {
        const val SUPABASE_URL      = "https://xtqxvnwwetxzrxtbxjyf.supabase.co"
        const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh0cXh2bnd3ZXR4enJ4dGJ4anlmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODI4Njk3NjAsImV4cCI6MjA5ODQ0NTc2MH0.AwyyVvV7p2XsSfcBY08lrutl7iFyre-5obRLlDX96lo"

        // Tasas de la plataforma — se aplican AL CLIENTE, no al productor
        const val CARGO_SERVICIO_PORCENT = 0.05   // 5%
        const val ITBMS_PORCENT          = 0.07   // 7% (ley panameña)

        lateinit var instance: AzueroMarketApp
            private set
    }

    val supabase by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(GoTrue)       // Auth (gotrue-kt en v2.1.4)
            install(Postgrest)    // Base de datos
            install(Realtime)     // Chat en tiempo real
            install(Storage)      // Fotos de productos
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
