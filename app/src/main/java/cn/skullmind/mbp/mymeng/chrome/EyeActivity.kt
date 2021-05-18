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
import cn.skullmind.mbp.mymeng.widget.EyeView


fun startEyeActivity(context: AppCompatActivity) {
    val intent = Intent(context, EyeActivity::class.java)
    context.startActivity(intent)
}

class EyeActivity : AppCompatActivity() {
    private val gyroScopeConverter = EyeView.GyroScopeConverter()
    private lateinit var eyeView: EyeView
    private lateinit var sensorManager: SensorManager
    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.also {
                gyroScopeConverter.convert(it, this@EyeActivity::deviateAxisAngle)
            }

        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

    }

    fun deviateAxisAngle(gx:Float, gy:Float, gz:Float){
        eyeView.refreshRotate(gx,gy,gz)
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