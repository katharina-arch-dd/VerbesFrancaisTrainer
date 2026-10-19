package de.katharina.verbesfrancais.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class FrenchSpeaker {
    private static final String TAG = "FrenchSpeaker";
    private TextToSpeech tts;
    private volatile boolean ready = false;

    public FrenchSpeaker(Context context) {
        if (context != null) {
            Context appContext = context.getApplicationContext();
            tts = new TextToSpeech(appContext != null ? appContext : context, status -> {
                if (status == TextToSpeech.SUCCESS && tts != null) {
                    try {
                        int r = tts.setLanguage(Locale.FRENCH);
                        ready = (r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to set language for TTS", e);
                        ready = false;
                    }
                }
            });
        }
    }

    public boolean isReady() {
        return ready;
    }

    public void speak(String text) {
        if (!ready || tts == null || text == null || text.trim().isEmpty()) return;
        try {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utt_" + System.currentTimeMillis());
        } catch (Exception e) {
            Log.e(TAG, "Error in speak", e);
        }
    }

    public void speakSlowly(String text) {
        if (!ready || tts == null || text == null || text.trim().isEmpty()) return;
        try {
            tts.setSpeechRate(0.7f);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utt_" + System.currentTimeMillis());
            tts.setSpeechRate(1.0f);
        } catch (Exception e) {
            Log.e(TAG, "Error in speakSlowly", e);
        }
    }

    public void stop() {
        if (tts != null) {
            try {
                tts.stop();
            } catch (Exception e) {
                Log.e(TAG, "Error in stop", e);
            }
        }
    }

    public void shutdown() {
        ready = false;
        if (tts != null) {
            try {
                tts.stop();
                tts.shutdown();
            } catch (Exception e) {
                Log.e(TAG, "Error in shutdown", e);
            }
            tts = null;
        }
    }
}
