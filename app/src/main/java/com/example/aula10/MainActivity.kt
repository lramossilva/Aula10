package com.example.aula10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.aula10.data.ArquivoRepository
import com.example.aula10.data.ContadorRepository
import com.example.aula10.data.DataStoreRepository
import com.example.aula10.data.MemoriaRepository
import com.example.aula10.data.SharedPrefsRepository
import com.example.aula10.ui.ContadorScreen
import com.example.aula10.ui.theme.Aula10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Criamos as instâncias dos repositórios nativos disponíveis
        val sharedPrefsRepo = SharedPrefsRepository(this)
        val arquivoRepo = ArquivoRepository(this)
        val dataStoreRepo = DataStoreRepository(this)
        val memoriaRepo = MemoriaRepository()

        val repositorios = listOf<ContadorRepository>(
            sharedPrefsRepo,
            arquivoRepo,
            dataStoreRepo,
            memoriaRepo
        )

        setContent {
            Aula10Theme {
                // Estado da opção de repositório selecionada
                var opcaoSelecionada by remember { mutableIntStateOf(0) }

                // O repositório ativo selecionado
                val repositorioAtivo = repositorios[opcaoSelecionada]

                // A UI recebe a interface `ContadorRepository` e não sabe de onde vem o dado!
                ContadorScreen(
                    repository = repositorioAtivo,
                    opcaoSelecionada = opcaoSelecionada,
                    onTrocarMecanismo = { novaOpcao ->
                        opcaoSelecionada = novaOpcao
                    }
                )
            }
        }
    }
}
