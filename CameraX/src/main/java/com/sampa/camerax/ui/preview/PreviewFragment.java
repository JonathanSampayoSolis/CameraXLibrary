package com.sampa.camerax.ui.preview;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sampa.camerax.R;
import com.sampa.camerax.arch.preview.IPreviewPresenter;
import com.sampa.camerax.arch.preview.IPreviewView;
import com.sampa.camerax.databinding.FragmentPreviewBinding;
import com.sampa.camerax.ui.CameraXActivity;
import com.sampa.camerax.util.CameraXContract;

import java.io.File;

public class PreviewFragment extends Fragment implements IPreviewView {

    public static final String ARG_IMG_URI = "ARG_IMG_URI";

    private FragmentPreviewBinding binding;

    private String imgURI;

    private CameraXContract cameraXContract;

    private IPreviewPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            imgURI = getArguments().getString(ARG_IMG_URI);
            cameraXContract = getArguments().getParcelable(CameraXActivity.EXTRA_CAMERAX_CONTRACT);
        }

        presenter = new PreviewPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentPreviewBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setPresenter(presenter);
        binding.setFile(imgURI);
        binding.setNewDir(constructFileName());

        binding.imgPhoto.setImageURI(Uri.parse(imgURI));
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onLeftView(imgURI);
    }

    @Override
    public void launchCameraView() {
        NavHostFragment.findNavController(PreviewFragment.this)
                .navigate(R.id.action_previewFragment_to_cameraFragment, getArguments());
    }

    @Override
    public void finishCamera(File file) {
        cameraXContract.capture(getActivity(), file, null);
    }

    @Override
    public void showError(Throwable throwable) {
        cameraXContract.capture(getActivity(), null, throwable);
    }

    // region:: PRIVATE METHODS

    private String constructFileName() {
        String fileName = "";

        if (getArguments() != null) {
            String fileDir = getArguments().getString(CameraXActivity.EXTRA_STORAGE_PATH);

            fileName = fileDir +
                    getArguments().getString(CameraXActivity.EXTRA_PHOTO_NAME) +
                    getArguments().getString(CameraXActivity.EXTRA_PHOTO_FORMAT);
        }

        return fileName;
    }

    // endregion

}
