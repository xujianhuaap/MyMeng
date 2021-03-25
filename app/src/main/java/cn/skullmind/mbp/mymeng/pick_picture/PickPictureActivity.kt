package cn.skullmind.mbp.mymeng.pick_picture

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.skullmind.mbp.mymeng.R
import cn.skullmind.mbp.mymeng.utils.getExternalDir
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun startPickPictureActivity(context: AppCompatActivity) {
    val intent = Intent(context, PickPictureActivity::class.java)
    context.startActivity(intent)
}


class PickPictureActivity : AppCompatActivity() {
    private lateinit var preview: PreviewView
    private lateinit var btnTakePhoto: View
    private lateinit var btnIso:View

    private lateinit var photoDir: File
    private var imageCapture: ImageCapture? = null
    private val listener:CameraConfigChangeListener = object :CameraConfigChangeListener{
        override fun onChange(value: Int) {
            Toast.makeText(this@PickPictureActivity,"${value}",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_picture)

        initData()
        initView()
        initFragment()

        if (allPermissionsGranted()) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, REQUEST_CODE_CAMERA)
        }
    }

    private fun initData() {
        photoDir = getExternalDir(this, "", Environment.DIRECTORY_PICTURES)
    }


    private fun initView() {
        preview = findViewById(R.id.preview)
        btnTakePhoto = findViewById(R.id.btn_take_photo)
        btnTakePhoto.setOnClickListener {
            takePhoto()
        }

        btnIso = findViewById(R.id.btn_config_iso)
        btnIso.setOnClickListener {
            showISODialog()
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        initFragment()
    }
    private fun showISODialog() {
        supportFragmentManager.findFragmentById(R.id.fragment_config)?.also {
            if(!it.isResumed || it.isHidden) supportFragmentManager.beginTransaction().show(it).commit()
        }

    }

    private fun initFragment(){
        supportFragmentManager.findFragmentById(R.id.fragment_config)?.also {
            val cameraConfigFragment = it as CameraConfigFragment
            cameraConfigFragment.changeListener = cameraConfigFragment.changeListener?:listener
        }

    }

    private fun takePhoto() {
        val imageCapture = this.imageCapture ?: return
        val photoFile = File(
            photoDir,
            SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(Date()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@PickPictureActivity,
                        "save photo failure",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        this@PickPictureActivity,
                        "save photo success",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        )


    }

    private fun openCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val previewFace = Preview.Builder().build().also {
                it.setSurfaceProvider(this.preview.createSurfaceProvider())
            }

            val cameraIndex = CameraSelector.DEFAULT_BACK_CAMERA

            this.imageCapture = this.imageCapture ?: ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraIndex, previewFace, imageCapture)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (REQUEST_CODE_CAMERA == requestCode) {
            if (allPermissionsGranted()) {
                openCamera()
            } else {
                Toast.makeText(this, "please allow camera permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUEST_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUEST_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_CAMERA = 0x1

    }
}

