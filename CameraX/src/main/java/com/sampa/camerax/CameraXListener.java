package com.sampa.camerax;

import java.io.File;

public interface CameraXListener {
	
	void onSuccess(File file);
	
	void onFailure(Throwable throwable);
	
}
