package com.example.preferencias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etnombre, etclave;
    Button btnsesion, btnReg;
    CheckBox cbsesion;

    RequestQueue rq;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor;

    String llave = "sesion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etnombre = findViewById(R.id.etnombre);
        etclave = findViewById(R.id.etclave);
        btnsesion = findViewById(R.id.btnsesion);
        btnReg = findViewById(R.id.btnReg);
        cbsesion = findViewById(R.id.cbsesion);

        rq = Volley.newRequestQueue(this);

        iniciarElementos();

        // Verificar si hay una sesión activa
        if (checarSesion()) {
            // Validar la sesión con el servidor
            validarSesion();
        } else {
            Toast.makeText(this, "Iniciar Sesion", Toast.LENGTH_LONG).show();
        }

        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity3.class));
            }
        });
    }

    private void iniciarElementos() {
        preferencias = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferencias.edit();
    }

    private void guardarSesion(boolean checked, String usuario, String pass) {
        editor.putBoolean(llave, checked);
        editor.putString("usuario", usuario);
        editor.putString("contraseña", pass);
        editor.apply();
    }

    private boolean checarSesion() {
        return preferencias.getBoolean(llave, false);
    }

    private void validarSesion() {
        String usuario = preferencias.getString("usuario", "");
        String pass = preferencias.getString("contraseña", "");

        if (!usuario.isEmpty() && !pass.isEmpty()) {
            String url = "https://preferences-9cc8dfc7ffda.herokuapp.com/index.php";
            JsonArrayRequest requerimento = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {

                    boolean validado = false;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject objeto = jsonArray.getJSONObject(i);
                            String Nombre = objeto.getString("Nombre");
                            String Password = objeto.getString("Password");

                            if (usuario.equals(Nombre) && pass.equals(Password)) {
                                validado = true;
                                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                                break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!validado) {
                        Toast.makeText(MainActivity.this, "Sesión inválida. Por favor, inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
                        preferencias.edit().clear().apply(); // Limpiar preferencias si la sesión es inválida
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            rq.add(requerimento);
        }
    }

    private void login() {
        String usuario = String.valueOf(etnombre.getText());
        String pass = String.valueOf(etclave.getText());
        if (usuario.isEmpty()) {
            Toast.makeText(this, "Ingrese el usuario", Toast.LENGTH_SHORT).show();
        } else if (pass.isEmpty()) {
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
        } else {
            String url = "https://preferences-9cc8dfc7ffda.herokuapp.com/index.php";
            JsonArrayRequest requerimento = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {

                    boolean userFound = false;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject objeto = jsonArray.getJSONObject(i);
                            String Nombre = objeto.getString("Nombre");
                            String Password = objeto.getString("Password");

                            if (usuario.equals(Nombre)) {
                                userFound = true;
                                if (pass.equals(Password)) {
                                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                    guardarSesion(cbsesion.isChecked(), usuario, pass);
                                    startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                                    break;
                                } else {
                                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!userFound) {
                        Toast.makeText(MainActivity.this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            rq.add(requerimento);
        }
    }
}
