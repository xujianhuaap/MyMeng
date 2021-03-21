package cn.skullmind.mbp.mymeng.pick_picture

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.skullmind.mbp.mymeng.R
import java.lang.Exception

fun startPickPictureActivity(context: AppCompatActivity){
    val intent = Intent(context,PickPictureActivity::class.java)
    context.startActivity(intent)
}


class PickPictureActivity:AppCompatActivity() {
    private lateinit var preview:PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_picture)
        preview = findViewById(R.id.preview);
        if(allPermissionsGranted()){
            openCamera()
        }else {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, REQUEST_CODE_CAMERA)
        }
    }


    private fun openCamera(){

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val previewFace = Preview.Builder().build().also {
                it.setSurfaceProvider(this.preview.createSurfaceProvider())
            }

            val cameraIndex = CameraSelector.DEFAULT_BACK_CAMERA;

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraIndex,previewFace)
            }catch (e: Exception){
                e.printStackTrace();
            }

        },ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(REQUEST_CODE_CAMERA == requestCode){
            if(allPermissionsGranted()){
                openCamera()
            }else{
                Toast.makeText(this, "please allow camera permission",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private fun allPermissionsGranted() = REQUEST_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(this,it) == PackageManager.PERMISSION_GRANTED;
    }

    companion object{
        private  val REQUEST_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_CAMERA = 0x1;

    }
}

