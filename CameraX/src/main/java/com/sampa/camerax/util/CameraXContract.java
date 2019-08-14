package com.sampa.camerax.util;

import android.app.Activity;
import android.os.Parcelable;

import java.io.File;

public interface CameraXContract extends Parcelable {
	
	void capture(Activity activity, File file, Throwable throwable);
	
}
