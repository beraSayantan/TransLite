package com.beginner.translite

import android.annotation.SuppressLint
import android.app.Dialog
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
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)

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
                        TakesLcodePutsTheTranslationInTheTV(lcode, inputtext, translatedheading)
                    }
                }
                .addOnFailureListener {
                    // Model couldn’t be loaded or other internal error.
                    // ...
                }
//

        }


    }

    private fun TakesLcodePutsTheTranslationInTheTV(
        lcode: String,
        inputtext: TextInputEditText,
        translatedheading: TextView
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(lcode)!!)
            .setTargetLanguage(TranslateLanguage.FINNISH)
            .build()

        val translator = Translation.getClient(options)
        getLifecycle().addObserver(translator)

        //showProgressDialog("Loading, please wait for a few millenia")
        val conditions = DownloadConditions.Builder().build()

        showProgressDialog("Loading please wait\nIt may take a minute or two")
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {

                translateFromModelAndPutInTheTextView(translator, inputtext, translatedheading)
                hideProgressDialog()

            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
            }
    }


    fun showProgressDialog(text: String){                                                         //WHAT IS THISSS??
        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun translateFromModelAndPutInTheTextView(translator: Translator, inputtext:TextView, translatedheading:TextView){
        translator.translate(inputtext.text.toString())
            .addOnSuccessListener { translatedText ->
                translatedheading.text = translatedText
            }
            .addOnFailureListener { exception ->
            }
    }
}


