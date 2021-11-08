package com.almaharapvtltd.serkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.slider.AlphaSlider;
import com.flask.colorpicker.slider.LightnessSlider;

public class ColorChooserActivity extends AppCompatActivity {

    ColorPickerView colorPicker;
    LightnessSlider lightnessSlider;
    AlphaSlider alphaSlider;
    TextView colorName;
    Button backButton,onButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_chooser);
        colorPicker = findViewById(R.id.color_picker);
        lightnessSlider = findViewById(R.id.lightness_slider);
        alphaSlider = findViewById(R.id.alpha_slider);
        colorPicker.setAlphaSlider(alphaSlider);
        colorPicker.setLightnessSlider(lightnessSlider);

        colorName = findViewById(R.id.color_name);
        backButton = findViewById(R.id.back_button_color_chooser);
        onButton = findViewById(R.id.on_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        colorPicker.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                colorName.setText(Integer.toHexString(selectedColor));
            }
        });

        colorPicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {

                onButton.setBackgroundColor(selectedColor);
            }
        });
        lightnessSlider.setColorPicker(colorPicker);

    }
    private void ColorSelector(){

    }
}
