package com.example.preferencias;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity3 extends AppCompatActivity {

    EditText etNombre,etPass,etTel,etEdad;
    Button btnRegistrar;

    RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etPass = (EditText) findViewById(R.id.etPass);
        etTel = (EditText) findViewById(R.id.etTel);
        etEdad = (EditText) findViewById(R.id.etEdad);

        rq = Volley.newRequestQueue(this);

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = String.valueOf(etNombre.getText());
                String pass = String.valueOf(etPass.getText());
                String tel = String.valueOf(etTel.getText());
                String edad = String.valueOf(etEdad.getText());

                String url = "https://preferences-9cc8dfc7ffda.herokuapp.com/index.php";

                // Crear el objeto JSON con los datos
                JSONObject postData = new JSONObject();
                try {
                    postData.put("nombre", nom);
                    postData.put("password", pass);
                    postData.put("telefono", tel);
                    postData.put("edad", edad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Crear la solicitud POST
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(MainActivity3.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity3.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ej) {
                            ej.printStackTrace();
                            Log.e("MyApp", "An error occurred", ej);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity3.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("MyApp", "An error occurred", error);
                    }
                });

                // Añadir la solicitud a la cola
                rq.add(request);
            }
        });



    }
}