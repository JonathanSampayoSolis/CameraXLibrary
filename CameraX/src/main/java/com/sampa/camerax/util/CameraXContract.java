package com.sampa.camerax.util;

import android.os.Parcelable;

import java.io.File;

public interface CameraXContract {
	
	void capture(File file, Throwable throwable);
	
}
