package com.sampa.camerax.arch.camera;

public interface ICameraView {
	
	void takePicture();
	
	void showProgress(boolean show);
	
	void showError(Throwable error);
	
	void toggleFlash();
	
	void toggleCamera();
	
}
