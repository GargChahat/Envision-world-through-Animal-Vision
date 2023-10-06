package com.example.animalvision

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.core.internal.utils.ImageUtil
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.animalvision.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class liveActivity (val c: Context):  Fragment() {
    private var _binding: FragmentHomeBinding?=null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageCapture: ImageCapture
    private lateinit var rotated:Bitmap
    private  lateinit var imgBitmap: Bitmap
    private lateinit var imgCaptureExecutor: ExecutorService
    private  var animalSelect="default"
    private val binding get() = _binding!!
    private var isTakePhoto=false
    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                startCamera()
            } else {
                Snackbar.make(
                    binding.root,
                    "The camera permission is necessary",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)


        cameraProviderFuture =  ProcessCameraProvider.getInstance(c)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        cameraPermissionResult.launch(android.Manifest.permission.CAMERA)

        binding.frogBtn.setOnClickListener {
            animalSelect="frog"
        }

        binding.squirrelBtn.setOnClickListener {
            animalSelect="squirrel"
        }
        binding.snakeBtn.setOnClickListener {
            animalSelect="snake"
        }
        binding.caterpillarBtn.setOnClickListener {
            animalSelect="caterpilar"
        }
        binding.defaultBtn.setOnClickListener {
            animalSelect="default"
        }
        binding.imgCaptureBtn.setOnClickListener {
            isTakePhoto = true
        }

        binding.switchBtn.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }


            startCamera()
        }
        binding.galleryBtn.setOnClickListener {
            val intent = Intent(activity, GalleryActivity::class.java)
            startActivity(intent)
        }
return binding.root
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            var imageAnalysis: ImageAnalysis  =
                ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

            imageAnalysis.setAnalyzer(imgCaptureExecutor ) { image ->
                val rotation = image.imageInfo.rotationDegrees

                imgBitmap = image.image?.toBitmap()!!
                var imageBitmap =imgBitmap!!.copy(imgBitmap.getConfig(), true)
                val width: Int = imageBitmap!!.width
                val height: Int = imageBitmap!!.height

                for (x in 0..width - 1) {
                    for (y in 0..height - 1) {
                        var pixel = imageBitmap!!.getPixel(x, y)
                        var red = Color.red(pixel)
                        var blue = Color.blue(pixel)
                        var green = Color.green(pixel)
                        when(animalSelect) {
                            "default"->
                                imageBitmap.setPixel(x, y, Color.rgb(red, green, blue))

                            "frog"->{
                                imageBitmap.setPixel(x, y, Color.rgb(blue,blue,255))
                            }
                            "squirrel"->
                            {
                                imageBitmap.setPixel(x, y, Color.rgb(100,100,blue))
                            }
                            "snake"->{
                                imageBitmap.setPixel(x, y, Color.rgb(255-red,255-green,blue-50))
                            }
                            "caterpilar"->{
                                imageBitmap.setPixel(x, y, Color.rgb(255-red, 255-green, 255-blue))
                            }
                        }
                    }
                }


                val matrix = Matrix()

                matrix.postRotate(90F)
                if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                    matrix.preScale(-1.0f, 1.0f);
                }

                rotated = Bitmap.createBitmap(
                    imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(),
                    matrix, true
                )
                imgBitmap=rotated
                binding.preview.post({
                    binding.preview.setImage(imgBitmap)
                    if(isTakePhoto){
                        takePhoto()
                    }
                } )

                image.close()
            }

//
//            val preview = Preview.Builder().build().also {
//                it.setSurfaceProvider(binding.prev.surfaceProvider)
//            }

//            imageCapture = ImageCapture.Builder().build()


            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(c))
    }


    private fun takePhoto() {
//        Thread{
            val file = File(c.getExternalFilesDir(null),"${System.currentTimeMillis()}save.png")

            ImageUtils.save(imgBitmap,file.absoluteFile,Bitmap.CompressFormat.PNG)
            ToastUtils.showShort("clicked")
            Log.i(TAG, "The image has been saved in ${file.toUri()}")
            isTakePhoto = false
//        }.start()
    }

//    private fun takePhoto() {
//        imageCapture.let {
//            val fileName = "JPEG_${System.currentTimeMillis()}"
//            val file = File( c.getExternalFilesDir(null),fileName)
//            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
//
//            it.takePicture(
//                outputFileOptions,
//                imgCaptureExecutor,
//                object : ImageCapture.OnImageSavedCallback {
//                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                        Log.i(TAG, "The image has been saved in ${file.toUri()}")
//                    }
//
//                    override fun onError(exception: ImageCaptureException) {
//                        Toast.makeText(
//                            binding.root.context,
//                            "Error taking photo",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        Log.d(TAG, "Error taking photo:$exception")
//                    }
//
//                })
//        }
//    }



    companion object {
        val TAG = "MainActivity"
    }

    private fun Image.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }


}

