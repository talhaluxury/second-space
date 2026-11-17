package com.secondspace.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    state: UiState,
    onCreateProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismissMessage: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Status card: Exists / Not created
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.profileExists) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.status_title),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = stringResource(
                            if (state.profileExists) R.string.status_exists else R.string.status_missing
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Disabled once a profile already exists
            Button(
                onClick = onCreateProfile,
                enabled = !state.profileExists,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_create))
            }

            OutlinedButton(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_open_settings))
            }

            // How to use (English + Urdu)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.howto_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(stringResource(R.string.howto_en))
                    Text(stringResource(R.string.howto_ur))
                }
            }

            // Warning card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.warning_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(stringResource(R.string.warning_en))
                    Text(stringResource(R.string.warning_ur))
                }
            }
        }
    }

    MessageDialog(state.message, onDismissMessage)
}

@Composable
private fun MessageDialog(message: UiMessage, onDismiss: () -> Unit) {
    val (title, body) = when (message) {
        UiMessage.NONE -> return
        UiMessage.NOT_ALLOWED -> R.string.dlg_not_allowed_title to R.string.dlg_not_allowed_body
        UiMessage.SUCCESS -> R.string.dlg_success_title to R.string.dlg_success_body
        UiMessage.CANCELLED -> R.string.dlg_cancelled_title to R.string.dlg_cancelled_body
        UiMessage.NO_ACTIVITY -> R.string.dlg_no_activity_title to R.string.dlg_no_activity_body
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(title)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(stringResource(body))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.ok)) }
        }
    )
}
