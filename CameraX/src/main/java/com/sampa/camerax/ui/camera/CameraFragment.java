package com.sampa.camerax.ui.camera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.fragment.app.Fragment;

import com.sampa.camerax.R;
import com.sampa.camerax.databinding.FragmentCameraBinding;
import com.sampa.camerax.util.PermissionsHelper;

import java.util.Objects;

public class CameraFragment extends Fragment implements PermissionsHelper.PermissionsHelperCallback {
	
	private static final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
	
	private final int PERMISSION_CODE = 1;
	
	private FragmentCameraBinding binding;
	
	private ImageCapture imageCapture;

	private Preview preview;
	
	private PermissionsHelper permissionsHelper;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		permissionsHelper = new PermissionsHelper(this, PERMISSION_CODE, PERMISSIONS, this);
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return (binding = FragmentCameraBinding.inflate(inflater, container, false)).getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		permissionsHelper.resolvePermissions();

		binding.fabFlash.setOnClickListener(v -> {
            preview.enableTorch(!preview.isTorchOn());
            binding.fabFlash.setImageResource(preview.isTorchOn() ? R.drawable.ic_flash_off_white : R.drawable.ic_flash_on_white);
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == PERMISSION_CODE)
			permissionsHelper.resolvePermissions();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_CODE)
			permissionsHelper.resolvePermissions();
	}
	
	@Override
	public void onPermissionsGranted() {
		binding.textureView.post(this::startCamera);
	}
	
	// region: REFERENCE METHODS
	
	private void startCamera() {
		binding.textureView.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> updateTransform());
		
		CameraX.bindToLifecycle(getViewLifecycleOwner(), (preview = createPreview()), createImageAnalysis(), (imageCapture = createImageCapture()));
	}
	
	// endregion
	
	// region:: PRIVATE METHODS
	
	private Preview createPreview() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		Size size = new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
		
		Rational rational = new Rational(displayMetrics.widthPixels, displayMetrics.heightPixels);
		
		Preview preview = new Preview(
				new PreviewConfig.Builder()
						.setLensFacing(CameraX.LensFacing.BACK)
						.setTargetResolution(size)
						.setTargetAspectRatio(rational)
						.setTargetRotation(binding.textureView.getDisplay().getRotation())
						.build()
		);
		
		preview.setOnPreviewOutputUpdateListener(output -> {
			ViewGroup viewGroup = (ViewGroup) binding.textureView.getParent();
			
			viewGroup.removeView(binding.textureView);
			viewGroup.addView(binding.textureView, 0);
			
			binding.textureView.setSurfaceTexture(output.getSurfaceTexture());
			updateTransform();
		});
		
		return preview;
	}
	
	private ImageAnalysis createImageAnalysis() {
		return new ImageAnalysis(
				new ImageAnalysisConfig.Builder()
						.build()
		);
	}
	
	private ImageCapture createImageCapture() {
		return new ImageCapture(
				new ImageCaptureConfig.Builder()
						.setTargetRotation(Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getRotation())
						.build()
		);
	}
	
	private void updateTransform() {
		Matrix matrix = new Matrix();
		
		float centerX = binding.textureView.getWidth() / 2f;
		float centerY = binding.textureView.getHeight() / 2f;
		
		float rotationDegrees;
		switch (binding.textureView.getDisplay().getRotation()) {
			case Surface.ROTATION_0:
				rotationDegrees = 0;
				break;
			case Surface.ROTATION_90:
				rotationDegrees = 90;
				break;
			case Surface.ROTATION_180:
				rotationDegrees = 180;
				break;
			case Surface.ROTATION_270:
				rotationDegrees = 270;
				break;
			default:
				return;
		}
		
		matrix.postRotate(-rotationDegrees, centerX, centerY);
		
		binding.textureView.setTransform(matrix);
	}
	
	// endregion
	
}
