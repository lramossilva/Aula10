package com.example.aula10.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aula10.data.ContadorRepository

/**
 * CAMADA DE INTERFACE (UI)
 *
 * Esta função Composable NÃO CONHECE SharedPreferences, Arquivos ou DataStore.
 * Ela recebe apenas uma instância da interface `ContadorRepository`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContadorScreen(
    repository: ContadorRepository,
    opcaoSelecionada: Int,
    onTrocarMecanismo: (Int) -> Unit
) {
    // Estado do contador reativo (atualizado a partir do repositório ativo)
    var cliques by rememberSaveable(repository) { mutableIntStateOf(repository.ler()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aula 09 — Arquitetura de Estado") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // CARD INFORMATIVO DO REPOSITÓRIO ATIVO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Mecanismo de Armazenamento Ativo:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = repository.getNomeMecanismo(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // CONTADOR DE CLIQUES
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$cliques",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (cliques == 1) "clique registrado" else "cliques registrados",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        cliques++
                        repository.salvar(cliques) // Chama a interface única!
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Incrementar e Salvar", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        cliques = 0
                        repository.salvar(0)
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Zerar Contador")
                }
            }

            // SELETOR PARA DEMONSTRAR A TROCA TRANSPARENTE DE REPOSITÓRIO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Trocar Repositório (Demonstração):",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = opcaoSelecionada == 0,
                                onClick = { onTrocarMecanismo(0) },
                                label = { Text("SharedPrefs") },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = opcaoSelecionada == 1,
                                onClick = { onTrocarMecanismo(1) },
                                label = { Text("Arquivo SO") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = opcaoSelecionada == 2,
                                onClick = { onTrocarMecanismo(2) },
                                label = { Text("DataStore") },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = opcaoSelecionada == 3,
                                onClick = { onTrocarMecanismo(3) },
                                label = { Text("Memória RAM") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ContadorScreenPreview() {
    com.example.aula10.ui.theme.Aula10Theme {
        ContadorScreen(
            repository = com.example.aula10.data.MemoriaRepository(),
            opcaoSelecionada = 0,
            onTrocarMecanismo = {}
        )
    }
}
