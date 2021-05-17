package cn.skullmind.mbp.mymeng.chrome

import android.content.Intent
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_GRAVITY
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.os.Bundle
import android.util.Half.EPSILON
import androidx.appcompat.app.AppCompatActivity
import cn.skullmind.mbp.mymeng.R
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


fun startEyeActivity(context: AppCompatActivity) {
    val intent = Intent(context, EyeActivity::class.java)
    context.startActivity(intent)
}

class EyeActivity : AppCompatActivity() {
    private val NS2S = 1.0f / 1000000000.0f
    private val deltaRotationVector = FloatArray(4)
    private lateinit var sensorManager: SensorManager
    private var timestamp: Long = 0
    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.also {
                if (timestamp != 0L) {
                    val dT = (event.timestamp - timestamp) * NS2S
                    // Axis of the rotation sample, not normalized yet.
                    var axisX = it.values[0]
                    var axisY = it.values[1]
                    var axisZ = it.values[2]

                    // Calculate the angular speed of the sample
                    val omegaMagnitude = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ)

                    // Normalize the rotation vector if it's big enough to get the axis
                    if (omegaMagnitude > EPSILON) {
                        axisX /= omegaMagnitude
                        axisY /= omegaMagnitude
                        axisZ /= omegaMagnitude
                    }

                    // Integrate around this axis with the angular speed by the time step
                    // in order to get a delta rotation from this sample over the time step
                    // We will convert this axis-angle representation of the delta rotation
                    // into a quaternion before turning it into the rotation matrix.
                    val thetaOverTwo = omegaMagnitude * dT / 2.0f
                    val sinThetaOverTwo = sin(thetaOverTwo)
                    val cosThetaOverTwo = cos(thetaOverTwo)
                    deltaRotationVector[0] = sinThetaOverTwo * axisX
                    deltaRotationVector[1] = sinThetaOverTwo * axisY
                    deltaRotationVector[2] = sinThetaOverTwo * axisZ
                    deltaRotationVector[3] = cosThetaOverTwo
                }
                timestamp = it.timestamp
                val deltaRotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector)
            }

        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eye_view)
        initSensorManger()
    }

    private fun initSensorManger() {
        if (!this::sensorManager.isInitialized) {
            val sensorManager = getSystemService(SENSOR_SERVICE)
            if (sensorManager is SensorManager) {
                this.sensorManager = sensorManager
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val sensor = sensorManager.getDefaultSensor(TYPE_GRAVITY)
        sensorManager.registerListener(sensorListener, sensor, SENSOR_DELAY_NORMAL)
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }
}