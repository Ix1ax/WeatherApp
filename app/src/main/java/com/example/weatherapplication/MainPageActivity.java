package com.example.weatherapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import java.time.LocalTime;

public class MainPageActivity extends AppCompatActivity {

    TextView city;
    TextView windMainInfo;
    TextView tempWeather;
    TextView tempFeelWeather;
    TextView pressureWeather;
    TextView humidityWeather;
    TextView windSpeedWeather;

    TextView tempAfter3H;
    TextView tempAfter6H;
    TextView tempAfter9H;
    TextView timeAfter3H;
    TextView timeAfter6H;
    TextView timeAfter9H;
    ImageView imageAfter3H;
    ImageView imageAfter6H;
    ImageView imageAfter9H;

    String town;

    ImageView imageWeather;

    LocalTime currentTime = LocalTime.now();
    int hours = currentTime.getHour();
    int[] timeArray = new int[3];

    double lat,lon; // координаты

    private SharedPreferences sharedPreferences;


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

        getWindow().setStatusBarColor(ContextCompat.getColor(MainPageActivity.this,R.color.statusBar));
        getWindow().setNavigationBarColor(ContextCompat.getColor(MainPageActivity.this,R.color.statusBar));

        tempWeather = findViewById(R.id.temp);
        tempFeelWeather = findViewById(R.id.tempfeel);
        humidityWeather = findViewById(R.id.humidity);
        windSpeedWeather = findViewById(R.id.speed);
        pressureWeather = findViewById(R.id.pressure);

        imageWeather = findViewById(R.id.imageWeather);

        windMainInfo = findViewById(R.id.windMainInfo);

        tempAfter3H = findViewById(R.id.tempAfter3);
        tempAfter6H = findViewById(R.id.tempAfter6);
        tempAfter9H = findViewById(R.id.tempAfter9);

        timeAfter3H = findViewById(R.id.timeAfter3);
        timeAfter6H = findViewById(R.id.timeAfter6);
        timeAfter9H = findViewById(R.id.timeAfter9);

        imageAfter3H = findViewById(R.id.imageAfter3);
        imageAfter6H = findViewById(R.id.imageAfter6);
        imageAfter9H = findViewById(R.id.imageAfter9);


        city = findViewById(R.id.city);

        town = getIntent().getStringExtra("TOWN");


        int time = 0;
        for(int i = 0; i < 3; i++){
                for(int j = 0; j < 24; j++){
                    if(hours % 3 == 0){
                        break;
                    }
                    hours-=1;
                }
                time +=3;
                timeArray[i] = (hours + time) % 24;
            }



//        city1.setOnClickListener(new View.OnClickListener() { // Обработчик нажатий
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainPageActivity.this, FindCityActivity.class);
//                // Intent используют для перехода на другую страницу
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent); // Начинаем метод перехода
//            }
//        });



        city.setOnClickListener(new View.OnClickListener() { // Обработчик нажатий
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
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        town = sharedPreferences.getString("town", "");
        if(city.getText().equals("Ваш город") && !(town.equals(""))) city.setText("");
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
                String degWind = "";

                try {
                    JSONObject jsonObjectResponse = new JSONObject(response);

                    JSONArray jsonArrayWeather = jsonObjectResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String weatherMain = jsonObjectWeather.getString("main");
                    String descriptionMain = jsonObjectWeather.getString("description"); // Описание погоды (состояние на небе)

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
                    double degreeWind = jsonObjectWind.getDouble("deg");

                    if(weatherMain.equalsIgnoreCase("Thunderstorm")) windMainInfo.setText("Гроза");
                    else if(weatherMain.equalsIgnoreCase("Drizzle")) windMainInfo.setText("Мелкий дождь");
                    else if(weatherMain.equalsIgnoreCase("Rain")) windMainInfo.setText("Дождь");
                    else if(weatherMain.equalsIgnoreCase("Snow")) windMainInfo.setText("Снег");
                    else if(weatherMain.equalsIgnoreCase("Clear")) windMainInfo.setText("Чистое небо");
                    else if(weatherMain.equalsIgnoreCase("Clouds")) windMainInfo.setText("Облачно");
                    else windMainInfo.setText("Туман");

                    if(descriptionMain.equalsIgnoreCase("clear sky")) imageWeather.setImageDrawable(getDrawable(R.drawable.sunny));
                    else if(descriptionMain.equalsIgnoreCase("few clouds")) imageWeather.setImageDrawable(getDrawable(R.drawable.fewcloud));
                    else if(weatherMain.equalsIgnoreCase("Clouds")) imageWeather.setImageDrawable(getDrawable(R.drawable.clouds));
                    else if(descriptionMain.equalsIgnoreCase("shower rain")) imageWeather.setImageDrawable(getDrawable(R.drawable.somerain));
                    else if(weatherMain.equalsIgnoreCase("Rain")) imageWeather.setImageDrawable(getDrawable(R.drawable.rainy));
                    else if(weatherMain.equalsIgnoreCase("Thunderstorm")) imageWeather.setImageDrawable((getDrawable(R.drawable.thunderstorm)));
                    else if(weatherMain.equalsIgnoreCase("Snow")) imageWeather.setImageDrawable((getDrawable(R.drawable.snow)));
                    else imageWeather.setImageDrawable((getDrawable(R.drawable.mist)));


                    tempWea += dfOne.format(temp);
                    tempFeelWea += dfOne.format(feelTemp);
                    speedWea += dfTwo.format(speedWind);
                    humidityWea +=dfOne.format(humidity);
                    pressureWea += dfOne.format(pressure);
                    tempWeather.setText(tempWea + "°С"); // Устанавливаем температуру
                    tempFeelWeather.setText("Ощущается " + tempFeelWea + "°С");
                    windSpeedWeather.setText("Скорость ветра: " + speedWea + " м/с");
                    pressureWeather.setText("Давление: " + pressureWea);
                    humidityWeather.setText("Влажность: " + humidityWea + "%");

                    city.setText(town);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

               // Log.d("response",response);

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

                    JSONObject jsonObjectWeatherTemp1 = jsonArrayWeatherList.getJSONObject(1); // Нужный элемент нам в списке
                    JSONObject jsonObjectMain1 = jsonObjectWeatherTemp1.getJSONObject("main");
                    double temp1 = jsonObjectMain1.getDouble("temp") - 272;

                    JSONObject jsonObjectWeatherInfo1 = jsonArrayWeatherList.getJSONObject(2);
                    JSONArray jsonArrayWeatherStatus1 = jsonObjectWeatherInfo1.getJSONArray("weather");
                    JSONObject jsonObjectWeatherStatus1 = jsonArrayWeatherStatus1.getJSONObject(0);
                    String main1 = jsonObjectWeatherStatus1.getString("main");
                    String description1 = jsonObjectWeatherStatus1.getString("description"); // Получаем значение description из объекта weather
                    // description - состояние о погоде (что на небе)

                    // Данные через 6 часа

                    JSONObject jsonObjectWeatherTemp2 = jsonArrayWeatherList.getJSONObject(2);
                    JSONObject jsonObjectMain2 = jsonObjectWeatherTemp2.getJSONObject("main");
                    double temp2 = jsonObjectMain2.getDouble("temp") - 272;

                    JSONObject jsonObjectWeatherInfo2 = jsonArrayWeatherList.getJSONObject(2);
                    JSONArray jsonArrayWeatherStatus2 = jsonObjectWeatherInfo2.getJSONArray("weather");
                    JSONObject jsonObjectWeatherStatus2 = jsonArrayWeatherStatus2.getJSONObject(0);
                    String main2 = jsonObjectWeatherStatus2.getString("main");
                    String description2 = jsonObjectWeatherStatus2.getString("description");

                    // Данные через 9 часа
                    JSONObject jsonObjectWeatherTemp3 = jsonArrayWeatherList.getJSONObject(3);
                    JSONObject jsonObjectMain3 = jsonObjectWeatherTemp3.getJSONObject("main");
                    double temp3 = jsonObjectMain3.getDouble("temp") - 272;

                    JSONObject jsonObjectWeatherInfo3 = jsonArrayWeatherList.getJSONObject(2);
                    JSONArray jsonArrayWeatherStatus3 = jsonObjectWeatherInfo3.getJSONArray("weather");
                    JSONObject jsonObjectWeatherStatus3 = jsonArrayWeatherStatus3.getJSONObject(0);
                    String main3 = jsonObjectWeatherStatus3.getString("main");
                    String description3 = jsonObjectWeatherStatus3.getString("description");

//                    // Данные через 12 часов
//                    JSONObject jsonObjectWeatherTemp4 = jsonArrayWeatherList.getJSONObject(4);
//                    JSONObject jsonObjectMain4 = jsonObjectWeatherTemp4.getJSONObject("main");
//                    double temp4 = jsonObjectMain4.getDouble("temp") - 272;
//
//                    JSONObject jsonObjectWeatherInfo4 = jsonArrayWeatherList.getJSONObject(2);
//                    JSONArray jsonArrayWeatherStatus4 = jsonObjectWeatherInfo4.getJSONArray("weather");
//                    JSONObject jsonObjectWeatherStatus4 = jsonArrayWeatherStatus4.getJSONObject(0);
//                    String description4 = jsonObjectWeatherStatus4.getString("description");

                    // Установка значений
                    tempAfter3 += dfOne.format(temp1);
                    tempAfter6 += dfOne.format(temp2);
                    tempAfter9 += dfOne.format(temp3);
                //    tempAfter12 += dfOne.format(temp4);

                    tempAfter3H.setText(tempAfter3 + "°");
                    tempAfter6H.setText(tempAfter6 + "°");
                    tempAfter9H.setText(tempAfter9 + "°");

                    timeAfter3H.setText(timeArray[0] + ":00");
                    timeAfter6H.setText(timeArray[1] + ":00");
                    timeAfter9H.setText(timeArray[2] + ":00");
                //    tempAfter12H.setText(tempAfter12 + "°");


                    if(description1.equalsIgnoreCase("clear sky")) imageAfter3H.setImageDrawable(getDrawable(R.drawable.sunny));
                    else if(main1.equalsIgnoreCase("Clear")) imageAfter3H.setImageDrawable(getDrawable(R.drawable.sunny));
                    else if(description1.equalsIgnoreCase("few clouds")) imageAfter3H.setImageDrawable(getDrawable(R.drawable.fewcloud));
                    else if(main1.equalsIgnoreCase("Clouds")) imageAfter3H.setImageDrawable(getDrawable(R.drawable.clouds));
                    else if(description1.equalsIgnoreCase("shower rain")) imageAfter3H.setImageDrawable(getDrawable(R.drawable.somerain));
                    else if(main1.equalsIgnoreCase("Rain")) imageAfter3H.setImageDrawable(getDrawable(R.drawable.rainy));
                    else if(main1.equalsIgnoreCase("Thunderstorm")) imageAfter3H.setImageDrawable((getDrawable(R.drawable.thunderstorm)));
                    else if(main1.equalsIgnoreCase("Snow")) imageAfter3H.setImageDrawable((getDrawable(R.drawable.snow)));
                    else imageAfter3H.setImageDrawable((getDrawable(R.drawable.mist)));

                    if(description2.equalsIgnoreCase("clear sky")) imageAfter6H.setImageDrawable(getDrawable(R.drawable.sunny));
                    else if(description2.equalsIgnoreCase("few clouds")) imageAfter6H.setImageDrawable(getDrawable(R.drawable.fewcloud));
                    else if(main2.equalsIgnoreCase("Clouds")) imageAfter6H.setImageDrawable(getDrawable(R.drawable.clouds));
                    else if(description2.equalsIgnoreCase("shower rain")) imageAfter6H.setImageDrawable(getDrawable(R.drawable.somerain));
                    else if(main2.equalsIgnoreCase("Rain")) imageAfter6H.setImageDrawable(getDrawable(R.drawable.rainy));
                    else if(main2.equalsIgnoreCase("Thunderstorm")) imageAfter6H.setImageDrawable((getDrawable(R.drawable.thunderstorm)));
                    else if(main2.equalsIgnoreCase("Snow")) imageAfter6H.setImageDrawable((getDrawable(R.drawable.snow)));
                    else imageAfter6H.setImageDrawable((getDrawable(R.drawable.mist)));

                    if(description3.equalsIgnoreCase("clear sky")) imageAfter9H.setImageDrawable(getDrawable(R.drawable.sunny));
                    else if(description3.equalsIgnoreCase("few clouds")) imageAfter9H.setImageDrawable(getDrawable(R.drawable.fewcloud));
                    else if(main3.equalsIgnoreCase("Clouds")) imageAfter9H.setImageDrawable(getDrawable(R.drawable.clouds));
                    else if(description3.equalsIgnoreCase("shower rain")) imageAfter9H.setImageDrawable(getDrawable(R.drawable.somerain));
                    else if(main3.equalsIgnoreCase("Rain")) imageAfter9H.setImageDrawable(getDrawable(R.drawable.rainy));
                    else if(main3.equalsIgnoreCase("Thunderstorm")) imageAfter9H.setImageDrawable((getDrawable(R.drawable.thunderstorm)));
                    else if(main3.equalsIgnoreCase("Snow")) imageAfter9H.setImageDrawable((getDrawable(R.drawable.snow)));
                    else imageAfter9H.setImageDrawable((getDrawable(R.drawable.mist)));

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Log.d("response",response);

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
