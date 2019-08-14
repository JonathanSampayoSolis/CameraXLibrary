package com.sampa.camerax.arch.camera;

public interface ICameraModel {

	void validatePath(String path, ICameraModel.ICameraModelCallback callback);
	
	interface ICameraModelCallback {
		
		void onValidatePathSuccess();
		
		
		void onValidatePathFailure(Throwable t);
	}

}
