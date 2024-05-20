package com.example.weatherapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
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
    TextView tempAfter3H;
    TextView tempAfter6H;
    TextView tempAfter9H;
    TextView tempAfter12H;
    String town;
    double lat,lon; // координаты

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

        tempAfter3H = findViewById(R.id.tempAfter3);
        tempAfter6H = findViewById(R.id.tempAfter6);
        tempAfter9H = findViewById(R.id.tempAfter9);
        tempAfter12H = findViewById(R.id.tempAfter12);

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

                    JSONObject jsonObjectCoord = jsonObjectResponse.getJSONObject("coord"); // получаем корды
                    lat = jsonObjectCoord.getDouble("lat");
                    lon = jsonObjectCoord.getDouble("lon");
                    getWeatherInfoAfter12H(lat,lon);

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

    public void getWeatherInfoAfter12H(double lat,double lon){
        String URL5days = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=58a88bc8fa24706f76fd408cbbd6e0ca";
        StringRequest stringRequestTwo = new StringRequest(Request.Method.POST, URL5days, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String tempAfter3 = "";
                String tempAfter6 = "";
                String tempAfter9 = "";
                String tempAfter12 = "";

                try {

                    JSONObject jsonObjectResponse = new JSONObject(response);
                    JSONArray jsonArrayWeatherList = jsonObjectResponse.getJSONArray("list"); // зашли в лист

                    // Данные через 3 часа
                    JSONObject jsonObjectWeatherTemp1 = jsonArrayWeatherList.getJSONObject(1);
                    JSONObject jsonObjectMain1 = jsonObjectWeatherTemp1.getJSONObject("main");
                    double temp1 = jsonObjectMain1.getDouble("temp") - 272;

// Данные через 6 часа
                    JSONObject jsonObjectWeatherTemp2 = jsonArrayWeatherList.getJSONObject(2);
                    JSONObject jsonObjectMain2 = jsonObjectWeatherTemp2.getJSONObject("main");
                    double temp2 = jsonObjectMain2.getDouble("temp") - 272;

// Данные через 9 часа
                    JSONObject jsonObjectWeatherTemp3 = jsonArrayWeatherList.getJSONObject(3);
                    JSONObject jsonObjectMain3 = jsonObjectWeatherTemp3.getJSONObject("main");
                    double temp3 = jsonObjectMain3.getDouble("temp") - 272;

// Данные через 12 часов
                    JSONObject jsonObjectWeatherTemp4 = jsonArrayWeatherList.getJSONObject(4);
                    JSONObject jsonObjectMain4 = jsonObjectWeatherTemp4.getJSONObject("main");
                    double temp4 = jsonObjectMain4.getDouble("temp") - 272;

// Установка значений
                    tempAfter3 += dfOne.format(temp1);
                    tempAfter6 += dfOne.format(temp2);
                    tempAfter9 += dfOne.format(temp3);
                    tempAfter12 += dfOne.format(temp4);

                    tempAfter3H.setText(tempAfter3 + "°");
                    tempAfter6H.setText(tempAfter6 + "°");
                    tempAfter9H.setText(tempAfter9 + "°");
                    tempAfter12H.setText(tempAfter12 + "°");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //Log.d("response",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(getApplicationContext(), volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueueTwo = Volley.newRequestQueue(getApplicationContext());
        requestQueueTwo.add(stringRequestTwo);
    }
}
