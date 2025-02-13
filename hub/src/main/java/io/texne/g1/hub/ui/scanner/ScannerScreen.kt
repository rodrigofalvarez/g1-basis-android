import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.texne.g1.basis.client.G1ServiceCommon
import io.texne.g1.hub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    scanning: Boolean,
    error: Boolean,
    nearbyGlasses: List<G1ServiceCommon.Glasses>?,
    scan: () -> Unit,
    connect: (id: String) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = scanning,
        onRefresh = scan,
        state = pullToRefreshState,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = if (
                nearbyGlasses.isNullOrEmpty()
            ) Arrangement.Center else Arrangement.spacedBy(32.dp)
        ) {
            when {
                nearbyGlasses.isNullOrEmpty().not() -> {
                    items(nearbyGlasses!!.size) {
                        GlassesItem(
                            nearbyGlasses[it],
                            { connect(nearbyGlasses[it].id) }
                        )
                    }
                }

                scanning -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Scanning for nearby glasses...")
                        }
                    }
                }

                error -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("An error ocurred. Please try again.")
                        }
                    }
                }

                nearbyGlasses != null -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No glasses were found nearby.")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GlassesItem(
    glasses: G1ServiceCommon.Glasses,
    connect: () -> Unit
) {
    Box(
        Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            Modifier.fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.5f)
                    .padding(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().weight(1f)
                ) {
                    Box(Modifier.weight(1f)) {
                        Image(
                            modifier = Modifier
                                .padding(8.dp),
                            painter = painterResource(R.drawable.glasses_a),
                            contentDescription = "Image of glasses"
                        )
                    }
                    Box(
                        Modifier.weight(1f).fillMaxHeight().padding(8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        when {
                            glasses.status == G1ServiceCommon.GlassesStatus.CONNECTING || glasses.status == G1ServiceCommon.GlassesStatus.DISCONNECTING -> {
                                CircularProgressIndicator(
                                    color = Color.Black
                                )
                            }

                            glasses.status != G1ServiceCommon.GlassesStatus.CONNECTED -> {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(6, 64, 43, 255),
                                        contentColor = Color.White
                                    ),
                                    onClick = { connect() }
                                ) {
                                    Text("CONNECT")
                                }
                            }

                            else -> {
                            }
                        }
                    }
                }
                Row(
                    Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy((-8).dp)
                    ) {
                        Text(
                            text = glasses.name,
                            fontSize = 24.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Black
                        )
                        Text(glasses.id, fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

