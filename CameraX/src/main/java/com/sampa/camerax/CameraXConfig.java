package com.sampa.camerax;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

@SuppressWarnings("unchecked")
public abstract class CameraXConfig<T> {
	
	String storagePath;
	
	String tempPath;
	
	String photoName;
	
	@CameraXLibrary.Formats
	String photoFormat;
	
	Context context;
	
	CameraXConfig(Context context) {
		File picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File tempDir = context.getExternalCacheDir();
		
		if (picturesDir == null)
			throw new NullPointerException("Storage path can't be accessible");
		
		if (tempDir == null)
			throw new NullPointerException("Temporal path can't be accessible");
		
		this.context = context;
		this.storagePath = picturesDir.getAbsolutePath() + "/CameraX/";
		this.tempPath = tempDir.getAbsolutePath() + "/temp_CameraX/";
		this.photoFormat = CameraXLibrary.Formats.JPG;
		this.photoName = UUID.randomUUID().toString().toUpperCase();
	}
	
	public T setStoragePath(String storagePath) {
		this.storagePath = storagePath;
		return (T) this;
	}
	
	public T setTempPath(String tempPath) {
		this.tempPath = tempPath;
		return (T) this;
	}
	
	public T setPhotoName(String photoName) {
		this.photoName = photoName;
		return (T) this;
	}
	
	public T setPhotoFormat(@CameraXLibrary.Formats String photoFormat) {
		this.photoFormat = photoFormat;
		return (T) this;
	}
	
}
