package com.sampa.camerax.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.sampa.camerax.CameraXLibrary;
import com.sampa.camerax.R;

public class CameraXActivity extends AppCompatActivity {
	
	public static final String EXTRA_STORAGE_PATH = "EXTRA_STORAGE_PATH";
	
	public static final String EXTRA_TEMP_PATH = "EXTRA_STORAGE_PATH";
	
	public static final String EXTRA_PHOTO_NAME = "EXTRA_PHOTO_NAME";
	
	public static final String EXTRA_PHOTO_FORMAT = "EXTRA_PHOTO_FORMAT";
	
	public static final String EXTRA_CAMERAX_CONTRACT = "EXTRA_CAMERAX_CONTRACT";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_camerax);
		
		Navigation.findNavController(findViewById(R.id.nav_host_fragment))
				.setGraph(R.navigation.camerax_navigation, fetchArgs());
	}
	
	// region:: PRIVATE METHODS
	
	private Bundle fetchArgs() {
		Bundle args = new Bundle();
		
		if (getIntent().getExtras() != null) {
			args.putString(EXTRA_STORAGE_PATH, getIntent().getExtras().getString(EXTRA_STORAGE_PATH));
			args.putString(EXTRA_TEMP_PATH, getIntent().getExtras().getString(EXTRA_TEMP_PATH));
			args.putString(EXTRA_PHOTO_NAME, getIntent().getExtras().getString(EXTRA_PHOTO_NAME));
			args.putString(EXTRA_PHOTO_FORMAT, getIntent().getExtras().getString(EXTRA_PHOTO_FORMAT));
			
			args.putParcelable(EXTRA_CAMERAX_CONTRACT, (CameraXLibrary) getIntent().getExtras().get(EXTRA_CAMERAX_CONTRACT));
		}
		
		return args;
	}
	
	// endregion
	
	
}