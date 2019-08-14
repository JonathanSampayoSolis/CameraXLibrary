package com.sampa.camerax.ui.preview;

import com.sampa.camerax.arch.preview.IPreviewPresenter;
import com.sampa.camerax.arch.preview.IPreviewView;

import java.io.File;
import java.util.MissingResourceException;
import java.util.Objects;

public class PreviewPresenter implements IPreviewPresenter {

    private IPreviewView view;

    PreviewPresenter(IPreviewView view) {
        this.view = view;
    }

    @Override
    public void onRestoreClick() {
        view.launchCameraView();
    }

    @Override
    public void movePhoto(String file, String newDir) {
        File finalFile = new File(newDir);
        File finalFileDir = new File(Objects.requireNonNull(finalFile.getParent()));

        if (!finalFileDir.exists())
            if (!finalFileDir.mkdirs()) {
                view.showError(new IllegalAccessError("No se pudo crear la carpeta temporal"));
                return;
            }

        File mFile = new File(file);

        if (mFile.exists() && mFile.renameTo(finalFile)) {
            view.finishCamera(mFile);
        } else {
            view.showError(new MissingResourceException(
                    "No se ha podido encontrar el archivo temporal",
                    "File.class",
                    PreviewFragment.class.getSimpleName()));
        }
    }

    @Override
    public void deletePhoto(String file) {
        File mFile = new File(file);
        if (mFile.exists())
            //noinspection ResultOfMethodCallIgnored
            mFile.delete();
    }

}
