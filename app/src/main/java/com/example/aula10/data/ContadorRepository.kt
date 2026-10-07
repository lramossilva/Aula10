package com.example.aula10.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * CONTRATO DE ARMAZENAMENTO (INTERFACE)
 *
 * A UI (Jetpack Compose) depende APENAS desta interface.
 * Ela não sabe e não se importa se o dado vem do SharedPreferences,
 * de um Arquivo Texto no SO, do DataStore ou da Memória.
 */
interface ContadorRepository {
    fun ler(): Int
    fun salvar(valor: Int)
    fun getNomeMecanismo(): String
}

/**
 * 🔴 OPÇÃO 1: IMPLEMENTAÇÃO VIA SHAREDPREFERENCES (AULA 08)
 * Armazena pares chave-valor no formato XML interno do Android.
 */
class SharedPrefsRepository(context: Context) : ContadorRepository {
    private val prefs = context.getSharedPreferences("ContadorPrefs", Context.MODE_PRIVATE)

    override fun ler(): Int {
        return prefs.getInt("cliques", 0)
    }

    override fun salvar(valor: Int) {
        prefs.edit().putInt("cliques", valor).apply()
    }

    override fun getNomeMecanismo(): String = "SharedPreferences (XML Nativo)"
}

/**
 * 🟢 OPÇÃO 2: IMPLEMENTAÇÃO VIA ARQUIVO TEXTO NATIVO NO DISCO
 * Escreve e lê diretamente um arquivo privado em `context.filesDir/contador.txt`.
 */
class ArquivoRepository(context: Context) : ContadorRepository {
    private val arquivo = File(context.filesDir, "contador.txt")

    override fun ler(): Int {
        return if (arquivo.exists()) {
            arquivo.readText().trim().toIntOrNull() ?: 0
        } else {
            0
        }
    }

    override fun salvar(valor: Int) {
        arquivo.writeText(valor.toString())
    }

    override fun getNomeMecanismo(): String = "Arquivo Interno (filesDir/contador.txt)"
}

/**
 * 🔵 OPÇÃO 3: IMPLEMENTAÇÃO VIA JETPACK DATASTORE (PADRÃO MODERNO GOOGLE)
 * Substituição oficial do SharedPreferences baseada em Coroutines e Protobuf.
 */
private val Context.dataStore by preferencesDataStore(name = "ContadorDataStore")

class DataStoreRepository(private val context: Context) : ContadorRepository {
    private val CHAVE_CLIQUES = intPreferencesKey("cliques")

    override fun ler(): Int = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[CHAVE_CLIQUES] ?: 0
        }.first()
    }

    override fun salvar(valor: Int) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[CHAVE_CLIQUES] = valor
            }
        }
    }

    override fun getNomeMecanismo(): String = "Jetpack DataStore (Reativo)"
}

/**
 * 🟡 OPÇÃO 4: IMPLEMENTAÇÃO EM MEMÓRIA (VOLÁTIL / SEM DISCO)
 * Útil para testes rápidos ou dados efêmeros.
 */
class MemoriaRepository : ContadorRepository {
    private var contadorMemoria = 0

    override fun ler(): Int = contadorMemoria

    override fun salvar(valor: Int) {
        contadorMemoria = valor
    }

    override fun getNomeMecanismo(): String = "Memória RAM (Volátil)"
}
