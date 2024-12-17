package com.example.pmdm_tarea_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnGrabar;
    private Button btnReproducir;
    private Button btnParar;
    private Button btnAvanzar;
    private Button btnRetroceder;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath; //ruta de almacenamiento del archivo de audio.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            //Solicito los permisos si no los tengo.
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1); // '1' es el código de solicitud de permiso
        } else {
            // Si ya tienes permisos, puedes comenzar la grabación
            empezarGrabacion();
        }

        //Vinculo los botones del XML
        btnGrabar = findViewById(R.id.btnGrabar);
        btnReproducir = findViewById(R.id.btnReproducir);
        btnParar = findViewById(R.id.btnParar);
        btnAvanzar = findViewById(R.id.btnAvanzar);
        btnRetroceder = findViewById(R.id.btnRetroceder);

        //Desactivo el botón de reproducir al inicio.
        btnReproducir.setActivated(false);

        //Configuro la ruta donde se almacenará el archivo.
        File audioFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Grabación.3gp");
        audioFilePath = audioFile.getAbsolutePath();

        //Configuación del botón Grabar.
        btnGrabar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (estaGrabando){
                    pararGrabacion();
                    btnGrabar.setText("Listo para Grabar");
                    btnReproducir.setActivated(true); //Activo reproducción.
                }else {
                    empezarGrabacion();
                    btnGrabar.setText("Grabando...");
                    btnReproducir.setActivated(false); //Desactivo reproducción durante la grabación.
                }

                estaGrabando = !estaGrabando;
            }

            private boolean estaGrabando = false;
        });

        //Configuro la acción del botón Reproducir.
        btnReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empezarReproduccion();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) { // Este es el código que usamos al pedir permisos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();

            } else {
                // Si el permiso fue denegado, mostrar un mensaje
                Toast.makeText(this, "Permiso de grabación de audio no concedido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void empezarReproduccion() {
        if (audioFilePath == null || !(new File(audioFilePath).exists())) {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_LONG).show();
            return;
        }

        mediaPlayer = new MediaPlayer();

        try{
        mediaPlayer.setDataSource(audioFilePath);  // Configurar la ruta del archivo de audio
        mediaPlayer.prepare(); // Prepara el reproductor
        mediaPlayer.start(); // Inicia la reproducción
        }catch (IOException e){
        e.printStackTrace();
        }
        //Configuro un listener para liberar recursos al finalizar
        mediaPlayer.setOnCompletionListener(mp ->{
            mediaPlayer.release();
            mediaPlayer = null;
        }
        );

    }

    private void empezarGrabacion() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //Fuente del micrófono.
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Formato de salida
        mediaRecorder.setOutputFile(audioFilePath);  // Ruta del archivo
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // Codificador de audio

        try {
            mediaRecorder.prepare(); // Preparar el grabador
            mediaRecorder.start(); // Iniciar la grabación
            Toast.makeText(this,"Grabando audio...", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Error al inicial la grabación", Toast.LENGTH_LONG).show();
        }

    }

    private void pararGrabacion() {
        if (mediaRecorder != null){
            try {
                mediaRecorder.stop(); //Detengo la grabación.
            }catch(RuntimeException e){
                e.printStackTrace();
                Toast.makeText(this, "Error al detener la grabación", Toast.LENGTH_LONG).show();
            }finally {
                //mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }

    }

}