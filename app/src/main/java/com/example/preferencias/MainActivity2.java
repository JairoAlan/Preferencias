package com.example.preferencias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    Button btncerrar;

    TextView tvUsuario,tvPass;
    SharedPreferences preferencias; // Objeto para acceder a preferencias compartidas
    SharedPreferences.Editor editor; // Editor para modificar preferencias compartidas

    String llave = "sesion"; // Clave para guardar el estado de la sesión

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btncerrar = (Button) findViewById(R.id.btncerrar);
        tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        tvPass = (TextView) findViewById(R.id.tvPass);

        // Inicializa las preferencias compartidas
        preferencias = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferencias.edit();

        // Obtiene los valores de usuario y contraseña después de inicializar las preferencias compartidas
        String usuario = preferencias.getString("usuario", "");
        String contraseña = preferencias.getString("contraseña", "");

        tvUsuario.setText("Nombre de Usuario: " + usuario);
        //tvPass.setText("Contraseña: " + contraseña);

        btncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(llave,false);
                editor.clear();
                //editor.remove("contraseña");
                editor.apply();
                Toast.makeText(MainActivity2.this,"La Sesion Fue Cerrada",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

}