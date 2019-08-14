
package com.sampa.camerax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringDef;

import com.sampa.camerax.ui.CameraXActivity;
import com.sampa.camerax.util.CameraXContract;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

public class CameraXLibrary implements CameraXContract {
	
	private String storagePath;
	
	private String tempPath;
	
	private String photoName;
	
	@Formats
	private String photoFormat;
	
	private static CameraXListener cameraXListener;
	
	private Context context;
	
	public static final Parcelable.Creator<CameraXLibrary> CREATOR = new Creator<CameraXLibrary>() {
		@Override
		public CameraXLibrary createFromParcel(Parcel in) {
			return new CameraXLibrary(in);
		}
		
		@Override
		public CameraXLibrary[] newArray(int size) {
			return new CameraXLibrary[size];
		}
	};
	
	private CameraXLibrary(Builder builder) {
		this.context = builder.context;
		this.storagePath = builder.storagePath;
		this.tempPath = builder.tempPath;
		this.photoName = builder.photoName;
		this.photoFormat = builder.photoFormat;
	}
	
	private CameraXLibrary(Parcel in) { }
	
	public String getStoragePath() {
		return storagePath;
	}
	
	public String getTempPath() {
		return tempPath;
	}
	
	public String getPhotoName() {
		return photoName;
	}
	
	public CameraXListener getCameraXListener() {
		return cameraXListener;
	}
	
	public @Formats String getPhotoFormat() {
		return photoFormat;
	}
	
	public Context getContext() {
		return context;
	}
	
	public void launch(CameraXListener _cameraXListener) {
		cameraXListener = _cameraXListener;
		Intent intent = new Intent(context, CameraXActivity.class);
		
		intent.putExtra(CameraXActivity.EXTRA_STORAGE_PATH, this.storagePath);
		intent.putExtra(CameraXActivity.EXTRA_TEMP_PATH, this.tempPath);
		intent.putExtra(CameraXActivity.EXTRA_PHOTO_NAME, this.photoName);
		intent.putExtra(CameraXActivity.EXTRA_PHOTO_FORMAT, this.photoFormat);
		
		intent.putExtra(CameraXActivity.EXTRA_CAMERAX_CONTRACT, this);
		
		context.startActivity(intent);
	}
	
	@Override
	public void capture(Activity activity, File file, Throwable throwable) {
		if (file == null) {
			cameraXListener.onFailure(throwable);
			activity.finish();
			return;
		}
		
		cameraXListener.onSuccess(file);
		activity.finish();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int i) { }
	
	public static class Builder {
		
		private String storagePath;
		
		private String tempPath;
		
		private String photoName;
		
		@Formats
		private String photoFormat;
		
		private Context context;
		
		public Builder(Context context) {
			File picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			File tempDir = context.getExternalCacheDir();
			
			if (picturesDir == null)
				throw new NullPointerException("Storage path can't be accessible");
			
			if (tempDir == null)
				throw new NullPointerException("Temporal path can't be accessible");
			
			this.context = context;
			this.storagePath = picturesDir.getAbsolutePath() + "/CameraX/";
			this.tempPath = tempDir.getAbsolutePath() + "/temp_CameraX/";
			this.photoFormat = Formats.JPG;
			this.photoName = UUID.randomUUID().toString().toUpperCase();
		}
		
		public Builder setStoragePath(String storagePath) {
			this.storagePath = storagePath;
			return this;
		}
		
		public Builder setTempPath(String tempPath) {
			this.tempPath = tempPath;
			return this;
		}
		
		public Builder setPhotoName(String photoName) {
			this.photoName = photoName;
			return this;
		}
		
		public void setPhotoFormat(@Formats String photoFormat) {
			this.photoFormat = photoFormat;
		}
		
		public CameraXLibrary build() {
			return new CameraXLibrary(this);
		}
		
	}
	
	@StringDef({Formats.JPG, Formats.PNG})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Formats {
		
		String JPG = ".jpg";
		
		String PNG = ".png";
		
	}
	
}
