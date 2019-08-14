package com.sampa.camerax.arch.preview;

import java.io.File;

public interface IPreviewModel {
	
	void movePhoto(String photo, String newPath, IPreviewModelCallback callback);
	
	void removeTempPhoto(String photo);
	
	interface IPreviewModelCallback {
		
		void onMovePhotoSuccess(File photo);
		
		void onMovePhotoFailure(Throwable t);
		
	}
	
}
