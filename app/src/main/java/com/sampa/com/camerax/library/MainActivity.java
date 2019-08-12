package com.sampa.com.camerax.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.sampa.camerax.CameraXActivity;
import com.sampa.com.camerax.library.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	
	ActivityMainBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		
		binding.btnTakePicture.setOnClickListener(v -> {
			Intent intent = new Intent(this, CameraXActivity.class);
			startActivity(intent);
		});
	}
	
}
