package com.sampa.camerax.arch;

import java.io.File;

public interface ICameraPresenter {
	
	void onTakePicturePressed();
	
	boolean validatePath(String path);
	
}
