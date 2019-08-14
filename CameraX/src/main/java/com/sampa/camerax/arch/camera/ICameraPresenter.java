package com.sampa.camerax.arch.camera;

public interface ICameraPresenter {
	
	void validatePath(String path);

	void onFlashClick();

	void onToggleCameraClick();
	
}
