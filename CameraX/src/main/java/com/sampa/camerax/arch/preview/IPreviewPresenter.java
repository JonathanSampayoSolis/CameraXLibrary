package com.sampa.camerax.arch.preview;

public interface IPreviewPresenter {

    void onRestoreClick();

    void movePhoto(String file, String newDir);

    void deletePhoto(String file);

}
