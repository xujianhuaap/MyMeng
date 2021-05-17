package cn.skullmind.mbp.mymeng.chrome

import android.content.Intent
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_GYROSCOPE
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.DegreeUtils
import cn.skullmind.mbp.mymeng.widget.EyeView


fun startEyeActivity(context: AppCompatActivity) {
    val intent = Intent(context, EyeActivity::class.java)
    context.startActivity(intent)
}

class EyeActivity : AppCompatActivity() {
    private var gx = 0f
    private  var gy = 0f
    private  var gz = 0f
    private val NS2S = 1.0f / 1000000000.0f
    private val angle = FloatArray(3)
    private lateinit var eyeView: EyeView
    private lateinit var sensorManager: SensorManager
    private var timestamp: Long = 0
    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.also {
                if (timestamp != 0L) {
                    val dT: Float = (it.timestamp - timestamp) * NS2S
                    angle[0] += it.values[0] * dT
                    angle[1] += it.values[1] * dT
                    angle[2] += it.values[2] * dT
                    val anglex = DegreeUtils.toDegree(angle[0].toDouble()).toFloat()
                    val angley = DegreeUtils.toDegree(angle[1].toDouble()).toFloat()
                    val anglez = DegreeUtils.toDegree(angle[2].toDouble()).toFloat()

                    if (gx !== 0f) {
                        val c: Float = gx - anglex
                        if (Math.abs(c) >= 0.5) {
                            gx = anglex
                        }
                    } else {
                        gx = anglex
                    }
                    if (gy !== 0f) {
                        val c: Float = gy - angley
                        if (Math.abs(c) >= 0.5) {
                            gy = angley
                        }
                    } else {
                        gy = angley
                    }

                    gz = anglez
                }
                timestamp = it.timestamp

                eyeView.refreshRotate(gx,gy)

            }

        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eye_view)
        eyeView = findViewById<EyeView>(R.id.view_eye)
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
        val sensor = sensorManager.getDefaultSensor(TYPE_GYROSCOPE)
        sensorManager.registerListener(sensorListener, sensor, SENSOR_DELAY_NORMAL)
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }
}