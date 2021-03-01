package com.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.StringSearch;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;

public class Voicerec extends Activity {

    private Intent intentRecognizer;
    private SpeechRecognizer speechRecognizer;
    private TextView status;

    private ArrayList<String> tempPage = new ArrayList<String>(
            Arrays.asList("temperature", "humidity", "weather","degree","degrees"));
    private ArrayList<String> locklightPage = new ArrayList<String>(
            Arrays.asList("light","lights","lock","security"));
    private ArrayList<String> alarmPage = new ArrayList<String>(
            Arrays.asList("alarm","intruder"));
    private ArrayList<String> airqualityPage = new ArrayList<String>(
            Arrays.asList("fan","air","quality"));
    private ArrayList<String> settingsPage = new ArrayList<String>(
            Arrays.asList("settings","password"));



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicerec_page);

        ActivityCompat.requestPermissions(this,new String[]{RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        status = findViewById(R.id.startRec);

        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                String errorM = "Error code: "+ error;
                status.setText(errorM);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                System.out.println("Word found " + matches.get(0));
                String match = "";
                String errorM = "Command not recognized";
                if(matches!=null){
                    match = matches.get(0);
                    String className = checkSpeech(match);
                    if(className.equals("false")){
                        status.setText(errorM);
                    }
                    else {
                        status.setText(match);
                        try {
                            Class<?> c = Class.forName("com.example."+className);
                            Intent intent = new Intent(Voicerec.this, c);
                            startActivity(intent);
                            if (speechRecognizer != null) {
                                speechRecognizer.destroy();
                            }
                            finish();
                        } catch (ClassNotFoundException ignored) {
                            status.setText(errorM);
                        }
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        if (!((MyApplication) this.getApplication()).checkNull()) {
            ImageButton recordVoice = (ImageButton) findViewById(R.id.recordVoice);
            recordVoice.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        speechRecognizer.startListening(intentRecognizer);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        speechRecognizer.stopListening();
                    }
                    return false;
                }
            });
        }
    }

    public String checkSpeech(String match){
        for(String word: tempPage){
            if(match.contains(word)){
                return("TempControl");
            }
        }
        for(String word: locklightPage){
            if(match.contains(word)){
                return("Security");
            }
        }
        for(String word: alarmPage){
            if(match.contains(word)){
                return("Alarm");
            }
        }
        for(String word: airqualityPage){
            if(match.contains(word)){
                return("AirQuality");
            }
        }
        for(String word: settingsPage){
            if(match.contains(word)){
                return("Settings");
            }
        }
        return("false");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Voicerec.this, FunctionMenu.class);
        startActivity(i);
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        finish();
    }
}