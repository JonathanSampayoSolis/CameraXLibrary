package com.sampa.camerax.model;

import com.sampa.camerax.arch.camera.ICameraModel;

import java.io.File;

public class CameraModel implements ICameraModel {
	
	@Override
	public void validatePath(String path, ICameraModelCallback callback) {
		File file = new File(path);
		
		try {
			if (!file.exists())
				if (!file.mkdirs()) {
					callback.onValidatePathFailure(new IllegalAccessException("Temp directory couldn't be created. Check permissions."));
					return;
				}
				callback.onValidatePathSuccess();
		} catch (Exception e) {
			callback.onValidatePathFailure(e);
		}
		
	}
	
}
