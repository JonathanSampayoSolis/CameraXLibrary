package com.sampa.camerax.ui.preview;

import com.sampa.camerax.arch.preview.IPreviewModel;
import com.sampa.camerax.arch.preview.IPreviewPresenter;
import com.sampa.camerax.arch.preview.IPreviewView;
import com.sampa.camerax.model.PreviewModel;

import java.io.File;

public class PreviewPresenter implements IPreviewPresenter, IPreviewModel.IPreviewModelCallback {

    private IPreviewView view;
    
    private IPreviewModel model;

    PreviewPresenter(IPreviewView view) {
        this.view = view;
        this.model = new PreviewModel();
    }

    @Override
    public void onRestoreClick() {
        view.launchCameraView();
    }
	
	@Override
	public void onSaveClick(String tempFile, String newPath) {
		model.movePhoto(tempFile, newPath, this);
	}

    @Override
    public void onLeftView(String file) {
    	model.removeTempPhoto(file);
    }
	
	@Override
	public void onMovePhotoSuccess(File photo) {
		view.finishCamera(photo);
	}
	
	@Override
	public void onMovePhotoFailure(Throwable t) {
		view.showError(t);
	}
	
}
