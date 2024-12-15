package com.example.pmdm_tarea_3;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btnGrabar;
    private Button btnReproducir;
    private Button btnPausa;
    private Button btnParar;
    private Button btnAvanzar;
    private Button btnRetroceder;
    private MediaRecorder mediaRecorder;
    private String audioFilePath; //ruta de almacenamiento del archivo de audio.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vinculo los botones del XML
        btnGrabar = findViewById(R.id.btnGrabar);
        btnReproducir = findViewById(R.id.btnReproducir);
        btnParar = findViewById(R.id.btnParar);
        btnPausa = findViewById(R.id.btnPausa);
        btnAvanzar = findViewById(R.id.btnAvanzar);
        btnRetroceder = findViewById(R.id.btnRetroceder);

        //Desactivo el botón de reproducir al inicio.
        btnReproducir.setActivated(false);

        //Configuro la ruta donde se almacenará el archivo.
        File audioFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Grabación.3gp");
        audioFilePath = audioFile.getAbsolutePath();

        //Cargar audio desde almacenamiento externo.
        audioAlmacenamiento();

    }

    private void audioAlmacenamiento() {


    }


}