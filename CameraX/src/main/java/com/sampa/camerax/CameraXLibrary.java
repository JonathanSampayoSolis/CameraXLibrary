
package com.sampa.camerax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringDef;

import com.sampa.camerax.ui.CameraXActivity;
import com.sampa.camerax.util.CameraXContract;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("unused")
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
	
	public void launch(CameraXListener _cameraXListener, LaunchConfig launchConfig) {
		cameraXListener = _cameraXListener;
		Intent intent = new Intent(context, CameraXActivity.class);
		
		intent.putExtra(CameraXActivity.EXTRA_STORAGE_PATH, launchConfig.getStoragePath());
		intent.putExtra(CameraXActivity.EXTRA_TEMP_PATH, launchConfig.getTempPath());
		intent.putExtra(CameraXActivity.EXTRA_PHOTO_NAME, launchConfig.getPhotoName());
		intent.putExtra(CameraXActivity.EXTRA_PHOTO_FORMAT, launchConfig.getPhotoFormat());
		
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
	
	public static class Builder extends CameraXConfig<Builder> {
		
		public Builder(Context context) {
			super(context);
		}
		
		private Context getContext() {
			return super.context;
		}
		
		public CameraXLibrary build() {
			return new CameraXLibrary(this);
		}
		
	}
	
	public static class LaunchConfig extends CameraXConfig<LaunchConfig> {
		
		public LaunchConfig(Context context) {
			super(context);
		}
		
		private String getStoragePath() {
			return super.storagePath;
		}
		
		private String getTempPath() {
			return super.tempPath;
		}
		
		private String getPhotoName() {
			return super.photoName;
		}
		
		private String getPhotoFormat() {
			return super.photoFormat;
		}
		
	}
	
	@StringDef({Formats.JPG, Formats.PNG})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Formats {
		
		String JPG = ".jpg";
		
		String PNG = ".png";
		
	}
	
}
