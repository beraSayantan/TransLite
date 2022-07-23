package com.beginner.translite

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.beginner.translite.databinding.FragmentHistoryBinding
import com.beginner.translite.databinding.FragmentTransBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class TransFragment : Fragment() {


    private lateinit var translate_query:TransQuery
    private lateinit var mProgressDialog: Dialog
    private lateinit var binding: FragmentTransBinding
    private lateinit var currContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

                                                                                    //COPIED BLOCK STARTs here BEWARE

        binding = DataBindingUtil.inflate<FragmentTransBinding>(inflater
            ,R.layout.fragment_trans,container,false)

        setHasOptionsMenu(true)

        mProgressDialog = Dialog(currContext)
        mProgressDialog.setContentView(R.layout.dialog_progress)

        var to_lang:String
        lateinit var to_code:String

        binding.translatedHeading.visibility=View.INVISIBLE

        binding.langTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?
                                        , view: View?, position: Int, id: Long) {
                to_lang = adapterView?.getItemAtPosition(position).toString()
                to_code = getLangCode(to_lang)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                to_code = getLangCode("ARABIC")
            }
        }


        binding.translateButton.setOnClickListener {

            val languageIdentifier = LanguageIdentification.getClient()
            languageIdentifier.identifyLanguage(binding.inputText.text.toString())
                .addOnSuccessListener { languageCode ->
                    if (languageCode == "und") {
                        Toast.makeText(currContext
                            , "Can't identify language", Toast.LENGTH_LONG).show()
                    } else {
                        binding.selectedLang.text = getLang(languageCode)
                        Toast.makeText(currContext
                            , "${getLang(languageCode)} language detected", Toast.LENGTH_LONG).show()
                        TakesLcodePutsTheTranslationInTheTV(languageCode, to_code
                            , binding.inputText, binding.translatedHeading)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(currContext
                        , "Can't identify language", Toast.LENGTH_LONG).show()
                }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {               //Fragment me context lene ki meri indigenous ninja technique
        currContext = context;
        super.onAttach(context)
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

    fun translateFromModelAndPutInTheTextView(translator: Translator, inputtext: TextView, translatedheading: TextView){
        translator.translate(inputtext.text.toString())
            .addOnSuccessListener { translatedText ->
                translatedheading.text = translatedText
                binding.translatedHeading.visibility= View.VISIBLE
                translate_query = TransQuery(inputtext.text.toString(),translatedText)
                storeData(inputtext.text.toString(),translatedText)
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

    fun storeData(input:String, output:String){

        var transQuery = TransQuery(input,output)
        Database.history.add( Database.history.size , transQuery)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }
}