package com.example.shramsarthi.voice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Voice Speech Engine managing Speech-to-Text (STT) and Text-to-Speech (TTS).
 * Built with robust permission handling, error mapping, partial transcripts, and Hindi vernacular speech.
 */
class VoiceSpeechManager(
    private val context: Context,
    private val onFinalSpeechResult: (String) -> Unit
) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsInitialized = false

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _partialTranscript = MutableStateFlow("")
    val partialTranscript: StateFlow<String> = _partialTranscript.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        initializeTts()
    }

    private fun initializeTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val hindiLocale = Locale("hi", "IN")
                val result = textToSpeech?.setLanguage(hindiLocale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fallback to default locale if Hindi voice pack is not installed on device
                    textToSpeech?.language = Locale.getDefault()
                }
                textToSpeech?.setSpeechRate(0.95f) // Slightly slower for clear Hindi pronunciation
                textToSpeech?.setPitch(1.0f)
                isTtsInitialized = true

                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                })
            }
        }
    }

    fun hasMicrophonePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isRecognitionAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }

    fun startListening(language: String = "hi-IN") {
        val isHindi = language.startsWith("hi")
        if (!hasMicrophonePermission()) {
            _errorMessage.value = if (isHindi)
                "माइक्रोफ़ोन अनुमति आवश्यक है। कृपया माइक अनुमति दें।"
            else
                "Microphone permission is required. Please grant mic access."
            return
        }

        if (!isRecognitionAvailable()) {
            _errorMessage.value = if (isHindi)
                "इस फोन पर गूगल स्पीच सेवा उपलब्ध नहीं है। कृपया लिखकर पूछें।"
            else
                "Speech recognition is unavailable on this device. Please type your query."
            return
        }

        stopSpeaking()
        _errorMessage.value = null
        _partialTranscript.value = ""

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(createRecognitionListener())
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language)
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            }

            speechRecognizer?.startListening(intent)
            _isListening.value = true
        } catch (e: Exception) {
            _isListening.value = false
            _errorMessage.value = if (isHindi) "माइक शुरू करने में समस्या हुई: ${e.message}" else "Failed to start microphone: ${e.message}"
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        _isListening.value = false
    }

    fun cancelListening() {
        try {
            speechRecognizer?.cancel()
        } catch (_: Exception) {}
        _isListening.value = false
        _partialTranscript.value = ""
    }

    fun speak(text: String, language: String = "hi-IN") {
        if (!isTtsInitialized || textToSpeech == null) return
        stopListening()
        _isSpeaking.value = true
        try {
            val targetLocale = if (language.startsWith("hi")) Locale("hi", "IN") else Locale("en", "IN")
            textToSpeech?.setLanguage(targetLocale)
        } catch (_: Exception) {}
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ai_saathi_speech_${System.currentTimeMillis()}")
        }
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "ai_saathi_utterance")
    }

    fun stopSpeaking() {
        try {
            textToSpeech?.stop()
        } catch (_: Exception) {}
        _isSpeaking.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun createRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _isListening.value = true
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                _isListening.value = false
            }

            override fun onError(error: Int) {
                _isListening.value = false
                val msg = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "ऑडियो रिकॉर्डिंग में समस्या हुई। कृपया पुनः प्रयास करें।"
                    SpeechRecognizer.ERROR_CLIENT -> "स्पीच क्लाइंट त्रुटि। कृपया पुनः प्रयास करें।"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "माइक्रोफ़ोन अनुमति नहीं मिली।"
                    SpeechRecognizer.ERROR_NETWORK -> "इंटरनेट कनेक्शन की समस्या है। कृपया नेटवर्क जांचें।"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "नेटवर्क टाइमआउट। कृपया दोबारा बोलें।"
                    SpeechRecognizer.ERROR_NO_MATCH -> "आवाज़ स्पष्ट सुनाई नहीं दी। कृपया फिर से बोलें।"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "स्पीच सेवा व्यस्त है। एक क्षण रुकें।"
                    SpeechRecognizer.ERROR_SERVER -> "सर्वर त्रुटि। कृपया पुनः प्रयास करें।"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "कोई आवाज़ नहीं सुनाई दी। माइक दबाकर बोलें।"
                    else -> "पहचान में समस्या हुई (कोड: $error)।"
                }
                _errorMessage.value = msg
            }

            override fun onResults(results: Bundle?) {
                _isListening.value = false
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val topMatch = matches?.firstOrNull()?.trim()
                if (!topMatch.isNullOrBlank()) {
                    _partialTranscript.value = topMatch
                    onFinalSpeechResult(topMatch)
                } else {
                    _errorMessage.value = "कोई आवाज़ नहीं पहचान पाए। कृपया दोबारा बोलें।"
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val partial = matches?.firstOrNull()?.trim()
                if (!partial.isNullOrBlank()) {
                    _partialTranscript.value = partial
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    fun release() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (_: Exception) {}

        try {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
            textToSpeech = null
        } catch (_: Exception) {}
    }
}
