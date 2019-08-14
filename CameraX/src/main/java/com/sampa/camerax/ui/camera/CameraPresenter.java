package com.sampa.camerax.ui.camera;

import com.sampa.camerax.arch.camera.ICameraPresenter;
import com.sampa.camerax.arch.camera.ICameraView;

import java.io.File;

public class CameraPresenter implements ICameraPresenter {
	
	private ICameraView view;
	
	CameraPresenter(ICameraView view) {
		this.view = view;
	}
	
	@Override
	public void validatePath(String path) {
		File file = new File(path);
		try {
			if (!file.exists())
				if (!file.mkdirs()) {
					view.showError("No se pudo crear la carpeta temporal");
					return;
				}

			view.showProgress(true);
			view.takePicture();
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	@Override
	public void onFlashClick() {
		view.toggleFlash();
	}

	@Override
	public void onToggleCameraClick() {
		view.toggleCamera();
	}
}