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
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.fragment.app.Fragment;

import com.sampa.camerax.R;
import com.sampa.camerax.arch.ICameraView;
import com.sampa.camerax.databinding.FragmentCameraBinding;
import com.sampa.camerax.ui.CameraXActivity;
import com.sampa.camerax.util.CameraXContract;
import com.sampa.camerax.util.PermissionsHelper;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class CameraFragment extends Fragment implements PermissionsHelper.PermissionsHelperCallback, ICameraView {
	
	private static final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
	
	private final int PERMISSION_CODE = 1;
	
	private FragmentCameraBinding binding;
	
	private ImageCapture imageCapture;
	
	private Preview preview;
	
	private PermissionsHelper permissionsHelper;
	
	private AtomicBoolean isLenBackActive;
	
	private CameraPresenter presenter;
	
	private CameraXContract cameraXListener;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		presenter = new CameraPresenter(this);
		
		permissionsHelper = new PermissionsHelper(this, PERMISSION_CODE, PERMISSIONS, this);
		isLenBackActive = new AtomicBoolean(true);
		
		if (getArguments() != null)
			cameraXListener = getArguments().getParcelable(CameraXActivity.EXTRA_CAMERAX_CONTRACT);
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return (binding = FragmentCameraBinding.inflate(inflater, container, false)).getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		binding.setPresenter(presenter);
		permissionsHelper.resolvePermissions();
		
		binding.fabFlash.setOnClickListener(v -> {
			preview.enableTorch(!preview.isTorchOn());
			binding.fabFlash.setImageResource(preview.isTorchOn() ? R.drawable.ic_flash_off_white : R.drawable.ic_flash_on_white);
		});
		
		binding.fabChangeCamera.setOnClickListener(v ->
				bindCamera(isLenBackActive.getAndSet(!isLenBackActive.get()) ? CameraX.LensFacing.FRONT : CameraX.LensFacing.BACK)
		);
		
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
	
	@Override
	public void takePicture() {
		try {
			
			if (getArguments() != null) {
				String tempPath = getArguments().getString(CameraXActivity.EXTRA_TEMP_PATH);
				
				if (presenter.validatePath(tempPath)) {
					imageCapture.takePicture(new File(
							tempPath +
									getArguments().getString(CameraXActivity.EXTRA_PHOTO_NAME) +
									getArguments().getString(CameraXActivity.EXTRA_PHOTO_FORMAT)
					), createTakePictureListener());
				} else {
					cameraXListener.capture(null, new IllegalAccessError("Can't create temp folder"));
				}
				
			} else {
				cameraXListener.capture(null, new NoSuchFieldError("Arguments can't be read"));
			}
			
		} catch (Exception e) {
			cameraXListener.capture(null, e);
		}
		
	}
	
	// region: REFERENCE METHODS
	
	private void startCamera() {
		binding.textureView.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> updateTransform());
		bindCamera(CameraX.LensFacing.BACK);
	}
	
	// endregion
	
	// region:: PRIVATE METHODS
	
	private void bindCamera(CameraX.LensFacing lensFacing) {
		CameraX.unbindAll();
		CameraX.bindToLifecycle(getViewLifecycleOwner(), createPreview(lensFacing), createImageCapture(lensFacing));
	}
	
	private Preview createPreview(CameraX.LensFacing lensFacing) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		Size size = new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
		
		Rational rational = new Rational(displayMetrics.widthPixels, displayMetrics.heightPixels);
		
		Preview preview = new Preview(
				new PreviewConfig.Builder()
						.setLensFacing(lensFacing)
						.setTargetResolution(size)
						.setTargetAspectRatio(rational)
						.setTargetRotation(Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getRotation())
						.build()
		);
		
		preview.setOnPreviewOutputUpdateListener(output -> {
			ViewGroup viewGroup = (ViewGroup) binding.textureView.getParent();
			
			viewGroup.removeView(binding.textureView);
			viewGroup.addView(binding.textureView, 0);
			
			binding.textureView.setSurfaceTexture(output.getSurfaceTexture());
			updateTransform();
		});
		
		return this.preview = preview;
	}
	
	private ImageCapture createImageCapture(CameraX.LensFacing lensFacing) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		Size size = new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
		
		Rational rational = new Rational(displayMetrics.widthPixels, displayMetrics.heightPixels);
		
		return this.imageCapture = new ImageCapture(
				new ImageCaptureConfig.Builder()
						.setLensFacing(lensFacing)
						.setTargetResolution(size)
						.setTargetAspectRatio(rational)
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
	
	private ImageCapture.OnImageSavedListener createTakePictureListener() {
		return new ImageCapture.OnImageSavedListener() {
			@Override
			public void onImageSaved(@NonNull File file) {
				cameraXListener.capture(file, null);
			}
			
			@Override
			public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
				cameraXListener.capture(null, new Exception(message, cause));
			}
		};
	}
	
	// endregion
	
}
