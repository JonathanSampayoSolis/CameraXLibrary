package com.sampa.camerax.ui.preview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sampa.camerax.databinding.FragmentPreviewBinding;

public class PreviewFragment extends Fragment {
	
	private FragmentPreviewBinding binding;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return (binding = FragmentPreviewBinding.inflate(inflater, container, false)).getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		binding.textview.setVisibility(View.VISIBLE);
	}
	
}
