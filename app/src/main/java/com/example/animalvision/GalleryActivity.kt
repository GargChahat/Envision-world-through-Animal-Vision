package com.example.animalvision

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.example.animalVersion.GalleryAdapter
import com.example.animalvision.databinding.ActivityGalleryBinding
import java.io.File

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val directory = File(getExternalFilesDir(null)?.absolutePath)
        val files = directory.listFiles() as Array<File>

        val adapter = GalleryAdapter(files.reversedArray())
        binding.viewPager.adapter = adapter
    }
}

