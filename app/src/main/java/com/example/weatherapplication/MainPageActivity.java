package com.example.weatherapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainPageActivity extends AppCompatActivity {

    TextView city1;
    TextView city2;
    TextView tempWeather;
    TextView tempFeelWeather;
    TextView pressureWeather;
    TextView humidityWeather;
    TextView windSpeedWeather;
    String town;

    DecimalFormat dfOne = new DecimalFormat("#");
    DecimalFormat dfTwo = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tempWeather = findViewById(R.id.temp);
        tempFeelWeather = findViewById(R.id.tempfeel);
        humidityWeather = findViewById(R.id.humidity);
        windSpeedWeather = findViewById(R.id.speed);
        pressureWeather = findViewById(R.id.pressure);
        city1 = findViewById(R.id.city1);
        city2 = findViewById(R.id.city2);

        town = getIntent().getStringExtra("TOWN");
        city1.setOnClickListener(new View.OnClickListener() { // Обработчик нажатий
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, FindCityActivity.class);
                // Intent используют для перехода на другую страницу
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent); // Начинаем метод перехода
            }
        });

        city2.setOnClickListener(new View.OnClickListener() { // Обработчик нажатий
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, FindCityActivity.class);
                // Intent используют для перехода на другую страницу
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent); // Начинаем метод перехода
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        town = getIntent().getStringExtra("TOWN");
        getWeatherInfo();
    }

    public void getWeatherInfo(){
        String URL = "https://api.openweathermap.org/data/2.5/weather?q=" + town +"&appid=58a88bc8fa24706f76fd408cbbd6e0ca&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String tempWea = ""; // Текущай температура
                String tempFeelWea = ""; // Температура как ощущается
                String speedWea = "";
                String humidityWea = "";
                String pressureWea = "";

                try {
                    JSONObject jsonObjectResponse = new JSONObject(response);

                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description"); // Получаем описание

                    JSONObject jsonObjectMain = jsonObjectResponse.getJSONObject("main"); // Заходим в Main
                    double temp = jsonObjectMain.getDouble("temp"); // Получаем температуру
                    double feelTemp = jsonObjectMain.getDouble("feels_like"); // Получаем температуру ту, которая ощущается
                    double humidity = jsonObjectMain.getDouble("humidity");
                    double pressure = jsonObjectMain.getDouble("pressure");

                    JSONObject jsonObjectWind = jsonObjectResponse.getJSONObject("wind"); // Заходим в папку с ветром
                    double speedWind = jsonObjectWind.getDouble("speed"); // Получаем скорость ветра


                    tempWea += dfOne.format(temp);
                    tempFeelWea += dfOne.format(feelTemp);
                    speedWea += dfTwo.format(speedWind);
                    humidityWea +=dfOne.format(humidity);
                    pressureWea += dfOne.format(pressure);
                    tempWeather.setText(tempWea + "°"); // Устанавливаем температуру
                    tempFeelWeather.setText("Ощущается " + tempFeelWea + "°");
                    windSpeedWeather.setText("Скорость ветра: " + speedWea + " м/с");
                    pressureWeather.setText("Давление: " + pressureWea);
                    humidityWeather.setText("Влажность: " + humidityWea + "%");

                    city2.setText(town);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Toast.makeText(getApplicationContext(), volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
