package com.beginner.translite

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.beginner.translite.databinding.Screen1Binding
import com.google.android.material.textfield.TextInputEditText
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : AppCompatActivity() {

    private lateinit var binding:Screen1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Screen1Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}



