package com.sampa.camerax.ui.camera;

import com.sampa.camerax.arch.camera.ICameraModel;
import com.sampa.camerax.arch.camera.ICameraPresenter;
import com.sampa.camerax.arch.camera.ICameraView;
import com.sampa.camerax.model.CameraModel;

public class CameraPresenter implements ICameraPresenter, ICameraModel.ICameraModelCallback {
	
	private ICameraView view;
	
	private ICameraModel model;
	
	CameraPresenter(ICameraView view) {
		this.view = view;
		this.model = new CameraModel();
	}
	
	@Override
	public void onCaptureClick(String path) {
		model.validatePath(path, this);
	}

	@Override
	public void onFlashClick() {
		view.toggleFlash();
	}

	@Override
	public void onToggleCameraClick() {
		view.toggleCamera();
	}
	
	@Override
	public void onValidatePathSuccess() {
		view.showProgress(true);
		view.takePicture();
	}
	
	@Override
	public void onValidatePathFailure(Throwable throwable) {
		view.showError(throwable);
	}
}