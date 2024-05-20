package com.example.weatherapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FindCityActivity extends AppCompatActivity {

    TextView tver;
    TextView moscow;
    TextView krasnodar;
    TextView sochi;
    String town;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.find_city);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tver = findViewById(R.id.Tver);
        moscow = findViewById(R.id.Moscow);
        krasnodar = findViewById(R.id.Krasnodar);
        sochi = findViewById(R.id.Sochi);

        tver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                town = "Тверь";

                Intent intent = new Intent(FindCityActivity.this, MainPageActivity.class);
                // Intent используют для перехода на другую страницу
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent); // Начинаем метод перехода

                Intent intent2 = new Intent(getBaseContext(), MainPageActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent2.putExtra("TOWN", town);
                startActivity(intent2);
            }
        });

        moscow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                town = "Москва";

                Intent intent = new Intent(FindCityActivity.this, MainPageActivity.class);
                // Intent используют для перехода на другую страницу
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent); // Начинаем метод перехода

                Intent intent2 = new Intent(getBaseContext(), MainPageActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent2.putExtra("TOWN", town);
                startActivity(intent2);

            }
        });

        krasnodar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                town = "Краснодар";

                Intent intent = new Intent(FindCityActivity.this, MainPageActivity.class);
                // Intent используют для перехода на другую страницу
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent); // Начинаем метод перехода

                Intent intent2 = new Intent(getBaseContext(), MainPageActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent2.putExtra("TOWN", town);
                startActivity(intent2);

            }
        });

        sochi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                town = "Сочи";
                Intent intent = new Intent(FindCityActivity.this, MainPageActivity.class);
                // Intent используют для перехода на другую страницу
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent); // Начинаем метод перехода

                Intent intent2 = new Intent(getBaseContext(), MainPageActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent2.putExtra("TOWN", town);
                startActivity(intent2);

            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FindCityActivity.this, MainPageActivity.class);
        // Intent используют для перехода на другую страницу
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent); // Начинаем метод перехода
    }

}