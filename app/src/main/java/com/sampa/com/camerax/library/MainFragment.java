package com.sampa.com.camerax.library;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.sampa.camerax.CameraXLibrary;
import com.sampa.camerax.CameraXListener;
import com.sampa.com.camerax.library.databinding.FragmentMainBinding;

import java.io.File;
import java.util.Objects;

public class MainFragment extends Fragment implements CameraXListener {
	
	private CameraXLibrary cameraXLibrary;

	private FragmentMainBinding binding;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		cameraXLibrary = new CameraXLibrary.Builder(Objects.requireNonNull(getContext()))
				.build();
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return (binding = FragmentMainBinding.inflate(inflater, container, false)).getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		FragmentMainBinding binding = DataBindingUtil.getBinding(view);
		
		if (binding != null) {
			binding.btnTakePicture.setOnClickListener(v -> cameraXLibrary.launch(this));
		}
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	public void onSuccess(File file) {
		binding.textViewSummary.setText("Archivo creado en: " + file.getAbsolutePath());
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onFailure(Throwable throwable) {
		binding.textViewSummary.setText("Hubo un problema el guardar la foto, vuelvelo a intentar:\n" + throwable.getMessage());
	}
	
}
