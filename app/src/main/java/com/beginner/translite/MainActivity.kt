package com.beginner.translite

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var lcode = "und"
        val langto:Spinner = findViewById(R.id.lang_to)
        var to_lang = "HINDI"
        val inputtext:TextInputEditText = findViewById(R.id.input_text)
        val translatebutton:Button = findViewById(R.id.translate_button)
        val translatedheading:TextView = findViewById(R.id.translated_heading)

        langto.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                to_lang = adapterView?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Not applicable here
            }
        }

        translatebutton.setOnClickListener {

            val languageIdentifier = LanguageIdentification.getClient()
            languageIdentifier.identifyLanguage(inputtext.text.toString())
                .addOnSuccessListener { languageCode ->
                    lcode = languageCode
                    if (languageCode == "und") {
                        Toast.makeText(applicationContext, "Can't identify language", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, lcode, Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    // Model couldn’t be loaded or other internal error.
                    // ...
                }

            val options = TranslatorOptions.Builder()
                //.setSourceLanguage(TranslateLanguage.fromLanguageTag(lcode)!!)
                .setSourceLanguage(lcode)
                .setTargetLanguage(TranslateLanguage.FRENCH)
                .build()
            val translator = Translation.getClient(options)
            getLifecycle().addObserver(translator)

            val conditions = DownloadConditions.Builder()
                .build()
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    translatedheading.text = "Translating..."
                }
                .addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    // ...
                }

            translator.translate(inputtext.text.toString())
                .addOnSuccessListener { translatedText ->
                    translatedheading.text = translatedText
                }
                .addOnFailureListener { exception ->
                }
        }

    }
}


