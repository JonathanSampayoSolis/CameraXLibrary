package com.sampa.camerax.arch.preview;

public interface IPreviewPresenter {
	
    void onRestoreClick();
	
    void onSaveClick(String tempFile, String newPath);
	
    void onLeftView(String file);
	
}
