package com.sampa.camerax.arch.camera;

public interface ICameraPresenter {
	
	void onCaptureClick(String path);
	
	void onFlashClick();
	
	void onToggleCameraClick();
	
}
