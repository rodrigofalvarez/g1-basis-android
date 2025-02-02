package io.texne.g1.basis.example.ui.scanner

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.texne.g1.basis.client.G1ServiceClient
import io.texne.g1.basis.client.G1ServiceClient.Glasses
import io.texne.g1.basis.client.G1ServiceClient.GlassesStatus
import io.texne.g1.basis.client.G1ServiceClient.ServiceStatus

@Composable
fun ServiceState(status: ServiceStatus) {
    Text(
        when(status) {
            ServiceStatus.LOOKING -> "Scanning..."
            ServiceStatus.LOOKED -> "Ready"
            ServiceStatus.READY -> "Ready"
            ServiceStatus.ERROR -> "Error"
            else -> "Not Ready"
        }
    )
}

@Composable
fun GlassesItem(glasses: Glasses, onConnect: () -> Unit, onDisconnect: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(-4.dp)
        ) {
            Text(glasses.name)
            Text(glasses.id, fontSize = 10.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        if(glasses.status == GlassesStatus.CONNECTING || glasses.status == GlassesStatus.DISCONNECTING) {
            CircularProgressIndicator(
                modifier = Modifier.padding(vertical = 4.dp)
            )
        } else if(glasses.status != GlassesStatus.CONNECTED) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = onConnect
            ) { Text("CONNECT") }
        } else {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = onDisconnect
            ) { Text("DISCONNECT") }
        }
    }
}

@Composable
fun GlassesList(status: ServiceStatus, glasses: List<Glasses>, onConnect: (id: String) -> Unit, onDisconnect: (id: String) -> Unit) {
    if (status == ServiceStatus.LOOKING || (status == ServiceStatus.LOOKED && glasses.isNotEmpty())) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            glasses.forEach { pair ->
                GlassesItem(pair, onConnect = {
                    onConnect(pair.id)
                }, onDisconnect = {
                    onDisconnect(pair.id)
                })
            }
        }
    }
}

@Composable
fun GlassesScanner() {
    val viewModel = hiltViewModel<GlassesScannerViewModel>()
    val state = viewModel.state.collectAsState().value

    if(state == null) {
        Box {
            Text("Initializing...")
        }
    } else {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.border(1.dp, Color.White, RoundedCornerShape(16.dp)),
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(state.status == ServiceStatus.LOOKING) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                    ServiceState(state.status)
                    Spacer(Modifier.weight(1f))
                    Button(
                        enabled = state.status != ServiceStatus.LOOKING,
                        onClick = { viewModel.startLooking() }
                    ) {
                        Text("Scan")
                    }
                }
            }
            Box(
                modifier = Modifier.weight(1f).border(1.dp, Color.White, RoundedCornerShape(16.dp)).fillMaxWidth()
            ) {
                GlassesList(
                    state.status,
                    state.glasses,
                    onConnect = {
                        viewModel.connectGlasses(it)
                    },
                    onDisconnect = {
                        viewModel.disconnectGlasses(it)
                    }
                )
            }
        }
    }
}
