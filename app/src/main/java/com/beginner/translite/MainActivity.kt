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

    private lateinit var mProgressDialog: Dialog
    private lateinit var binding:Screen1Binding
    private lateinit var translate_query:TransQuery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Screen1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)

        var to_lang:String
        lateinit var to_code:String

        binding.translatedHeading.visibility=View.INVISIBLE

        binding.langTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                to_lang = adapterView?.getItemAtPosition(position).toString()
                to_code = getLangCode(to_lang)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Not applicable here
            }
        }


        binding.translateButton.setOnClickListener {

            val languageIdentifier = LanguageIdentification.getClient()
            languageIdentifier.identifyLanguage(binding.inputText.text.toString())

                .addOnSuccessListener { languageCode ->
                    if (languageCode == "und") {
                        Toast.makeText(applicationContext, "Can't identify language", Toast.LENGTH_LONG).show()
                    } else {
                        binding.selectedLang.text = getLang(languageCode)
                        Toast.makeText(applicationContext, "${getLang(languageCode)} language detected", Toast.LENGTH_LONG).show()
                        TakesLcodePutsTheTranslationInTheTV(languageCode, to_code, binding.inputText, binding.translatedHeading)
                    }
                }
                .addOnFailureListener {
                    // Model couldn’t be loaded or other internal error.
                    // ...
                }
        }

    }

    private fun TakesLcodePutsTheTranslationInTheTV(
        from_code: String,
        to_code:String,
        inputtext: TextInputEditText,
        translatedheading: TextView
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(from_code)!!)
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(to_code)!!)
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
        mProgressDialog.setCanceledOnTouchOutside(false)
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun translateFromModelAndPutInTheTextView(translator: Translator, inputtext:TextView, translatedheading:TextView){
        translator.translate(inputtext.text.toString())
            .addOnSuccessListener { translatedText ->
                translatedheading.text = translatedText
                binding.translatedHeading.visibility= View.VISIBLE
                translate_query = TransQuery(inputtext.text.toString(),translatedText)
            }
            .addOnFailureListener { exception ->
            }
    }

    fun getLangCode(lang:String): String {
        when(lang)
        {
            "ARABIC" -> return "ar"
            "BENGALI" -> return "bn"
            "CHINESE" -> return "zh"
            "DUTCH" -> return "nl"
            "ENGLISH" -> return "en"
            "FRENCH" -> return "fr"
            "GERMAN" -> return "de"
            "GREEK" -> return "el"
            "HINDI" -> return "hi"
            "ITALIAN" -> return "it"
            "JAPANESE" -> return "ja"
            "KANNADA" -> return "kn"
            "KOREAN" -> return "ko"
            "MARATHI" -> return "mr"
            "PORTUGUESE" -> return "pt"
            "RUSSIAN" -> return "ru"
            "SPANISH" -> return "es"
            "TAMIL" -> return "ta"
            "TELUGU" -> return "te"
            "URDU" -> return "ur"
        }
        return ""
    }

    fun getLang(lcode:String): String {
        when(lcode)
        {
            "ar" -> return "ARABIC"
            "bn" -> return "BENGALI"
            "zh" -> return "CHINESE"
            "nl" -> return "DUTCH"
            "en" -> return "ENGLISH"
            "fr" -> return "FRENCH"
            "de" -> return "GERMAN"
            "el" -> return "GREEK"
            "hi" -> return "HINDI"
            "it" -> return "ITALIAN"
            "ja" -> return "JAPANESE"
            "kn" -> return "KANNADA"
            "ko" -> return "KOREAN"
            "mr" -> return "MARATHI"
            "pt" -> return "PORTUGUESE"
            "ru" -> return "RUSSIAN"
            "es" -> return "SPANISH"
            "ta" -> return "TAMIL"
            "te" -> return "TELUGU"
            "ur" -> return "URDU"
        }
        return ""
    }
}



