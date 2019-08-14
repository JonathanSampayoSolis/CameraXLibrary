package com.sampa.camerax.model;

import com.sampa.camerax.arch.preview.IPreviewModel;
import com.sampa.camerax.ui.preview.PreviewFragment;

import java.io.File;
import java.util.MissingResourceException;
import java.util.Objects;

public class PreviewModel implements IPreviewModel {
	
	@Override
	public void movePhoto(String photo, String newPath, IPreviewModelCallback callback) {
		File finalFile = new File(newPath);
		File finalFileDir = new File(Objects.requireNonNull(finalFile.getParent()));
		
		if (!finalFileDir.exists())
			if (!finalFileDir.mkdirs()) {
				callback.onMovePhotoFailure(new IllegalAccessError("No se pudo crear la carpeta pública. Verifica los permisios."));
				return;
			}
		
		File filePhoto = new File(photo);
		
		if (filePhoto.exists() && filePhoto.renameTo(finalFile))
			callback.onMovePhotoSuccess(filePhoto);
		else
			callback.onMovePhotoFailure(new MissingResourceException(
					"No se ha podido encontrar el archivo temporal",
					"File.class",
					PreviewFragment.class.getSimpleName())
			);
	}
	
	@Override
	public void removeTempPhoto(String photo) {
		File mFile = new File(photo);
		if (mFile.exists())
			//noinspection ResultOfMethodCallIgnored
			mFile.delete();
	}
	
}