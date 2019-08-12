package com.sampa.camerax.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sampa.camerax.databinding.FragmentCameraBinding;

public class CameraFragment extends Fragment {
	
	private FragmentCameraBinding binding;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return (binding = FragmentCameraBinding.inflate(inflater, container, false)).getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		binding.textview.setVisibility(View.VISIBLE);
	}
	
}
