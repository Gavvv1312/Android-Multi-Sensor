package com.example.androideyy

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.androideyy.ui.theme.AndroideyyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroideyyTheme {
                SensorDashboardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorDashboardScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Sensor Manager instance
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    // Sensors
    val accelerometer = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }
    val lightSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    // States for Accelerometer (X, Y, Z)
    var accelX by remember { mutableFloatStateOf(0f) }
    var accelY by remember { mutableFloatStateOf(0f) }
    var accelZ by remember { mutableFloatStateOf(0f) }

    // State for Light Sensor (Lux)
    var lightLux by remember { mutableFloatStateOf(0f) }

    // Sensor Event Listeners
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        accelX = event.values[0]
                        accelY = event.values[1]
                        accelZ = event.values[2]
                    }
                    Sensor.TYPE_LIGHT -> {
                        lightLux = event.values[0]
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not needed for this implementation
            }
        }
    }

    // Register / Unregister sensors correctly using lifecycle
    DisposableEffect(lifecycleOwner, sensorManager, accelerometer, lightSensor) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                accelerometer?.let {
                    sensorManager.registerListener(
                        sensorEventListener,
                        it,
                        SensorManager.SENSOR_DELAY_UI,
                    )
                }
                lightSensor?.let {
                    sensorManager.registerListener(
                        sensorEventListener,
                        it,
                        SensorManager.SENSOR_DELAY_UI,
                    )
                }
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    // Dynamic UI reaction: Background tint based on light level (Lux)
    val targetBackgroundColor = when {
        lightLux < 50f -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        lightLux > 3000f -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.background
    }

    val animatedBgColor by animateColorAsState(
        targetValue = targetBackgroundColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "bgColorAnimation",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Multi-Sensor Dashboard", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedBgColor)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Check & Warning for missing sensors
            if (accelerometer == null) {
                SensorMissingWarning(sensorName = "Accelerometer (Motion Sensor)")
            }
            if (lightSensor == null) {
                SensorMissingWarning(sensorName = "Ambient Light Sensor (Environment Sensor)")
            }

            // 1. Motion Sensor Section (Accelerometer)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.MotionPhotosOn,
                            contentDescription = "Motion",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "Motion Sensor (Accelerometer)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        text = "Constants: Sensor.TYPE_ACCELEROMETER",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )

                    // Values Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        SensorValueBadge(label = "X", value = accelX)
                        SensorValueBadge(label = "Y", value = accelY)
                        SensorValueBadge(label = "Z", value = accelZ)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Interactive Tilt Visualizer (Tilt your device/emulator):",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )

                    // Interactive Ball Canvas responding to X and Y
                    TiltVisualizerBox(accelX = accelX, accelY = accelY)
                }
            }

            // 2. Environment Sensor Section (Light Sensor)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.LightMode,
                            contentDescription = "Light",
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                        Text(
                            text = "Environment Sensor (Light)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        text = "Constants: Sensor.TYPE_LIGHT",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = "Ambient Brightness",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = when {
                                    lightLux < 50f -> "Condition: Dark / Low Light 🌙"
                                    lightLux > 3000f -> "Condition: Very Bright / Sunlight ☀️"
                                    else -> "Condition: Normal Indoor Lighting 💡"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                        ) {
                            Text(
                                text = "%.1f lx".format(lightLux),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                            )
                        }
                    }

                    // Linear progress indicator for lux (capped at 5000 for scale)
                    val progress = (lightLux / 5000f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.tertiary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun SensorValueBadge(label: String, value: Float) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.width(96.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "%.2f".format(value),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun TiltVisualizerBox(accelX: Float, accelY: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f
            val maxOffset = minOf(centerX, centerY) - 30f

            val offsetX = ((-accelX / 9.81f) * maxOffset).coerceIn(-maxOffset, maxOffset)
            val offsetY = ((accelY / 9.81f) * maxOffset).coerceIn(-maxOffset, maxOffset)

            // Draw center crosshair
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(centerX, 0f),
                end = Offset(centerX, canvasHeight),
                strokeWidth = 2f,
            )
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(0f, centerY),
                end = Offset(canvasWidth, centerY),
                strokeWidth = 2f,
            )

            // Draw rolling ball
            drawCircle(
                color = Color(0xFF6750A4),
                radius = 24f,
                center = Offset(centerX + offsetX, centerY + offsetY),
            )
            drawCircle(
                color = Color(0xFFEADDFF),
                radius = 8f,
                center = Offset(centerX + offsetX - 6f, centerY + offsetY - 6f),
            )
        }

        Text(
            text = "Tilt Phone to Move Ball",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
fun SensorMissingWarning(sensorName: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = MaterialTheme.colorScheme.error,
            )
            Text(
                text = "Warning: $sensorName is not available on this device/emulator.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}
