package com.sampa.com.camerax.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.sampa.camerax.CameraXActivity;
import com.sampa.com.camerax.library.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return FragmentMainBinding.inflate(inflater, container, false).getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		FragmentMainBinding binding = DataBindingUtil.getBinding(view);
		
		if (binding != null) {
			binding.btnTakePicture.setOnClickListener(v -> startActivity(new Intent(getContext(), CameraXActivity.class)));
		}
	}
	
}
