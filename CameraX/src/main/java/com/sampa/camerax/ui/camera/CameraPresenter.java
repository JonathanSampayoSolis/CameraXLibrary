package com.sampa.camerax.ui.camera;

import com.sampa.camerax.arch.ICameraPresenter;
import com.sampa.camerax.arch.ICameraView;

import java.io.File;

public class CameraPresenter implements ICameraPresenter {
	
	private ICameraView cameraView;
	
	CameraPresenter(ICameraView cameraView) {
		this.cameraView = cameraView;
	}
	
	@Override
	public void onTakePicturePressed() {
		cameraView.takePicture();
	}
	
	@Override
	public boolean validatePath(String path) throws NullPointerException {
		File file = new File(path);
		
		if (!file.exists())
			return file.mkdirs();
			
		return true;
	}
	
}