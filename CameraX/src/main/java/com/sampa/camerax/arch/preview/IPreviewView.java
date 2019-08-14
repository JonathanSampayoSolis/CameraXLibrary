package com.sampa.camerax.arch.preview;

import java.io.File;

public interface IPreviewView {

    void launchCameraView();

    void finishCamera(File file);

    void showError(Throwable throwable);

}
