package com.sampa.camerax.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.sampa.camerax.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PermissionsHelper {
	
	private final Fragment fragment;
	
	private final Activity activity;
	
	private final int permissionCode;
	
	private final String[] permissions;
	
	private PermissionsHelperCallback callback;
	
	public PermissionsHelper(Fragment fragment, int permissionCode, String[] permissions, PermissionsHelperCallback callback) {
		this.fragment = fragment;
		this.activity = fragment.getActivity();
		this.permissionCode = permissionCode;
		this.permissions = permissions;
		this.callback = callback;
	}
	
	public void resolvePermissions() {
		List<String> notGrantedPermissions = new ArrayList<>();
		
		for (String permission : permissions) {
			if (!hasPermission(permission)) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
					showAdviceDialog();
					return;
				}
				
				notGrantedPermissions.add(permission);
			}
			
		}
		
		if (notGrantedPermissions.isEmpty())
			callback.onPermissionsGranted();
		else
			//noinspection ToArrayCallWithZeroLengthArrayArgument
			fragment.requestPermissions(notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]), permissionCode);
	}
	
	private void showAdviceDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		builder.setTitle(R.string.advice)
				.setMessage(R.string.permissions_advice_content)
				.setCancelable(false)
				.setPositiveButton(R.string.ok, (a, b) -> launchDetailSettings())
				.create().show();
	}
	
	private void launchDetailSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null));
		fragment.startActivityForResult(intent, permissionCode);
	}
	
	private boolean hasPermission(String permission) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return true;
		
		return ContextCompat.checkSelfPermission(Objects.requireNonNull(fragment.getContext()), permission) == PackageManager.PERMISSION_GRANTED;
	}
	
	public interface PermissionsHelperCallback {
		
		void onPermissionsGranted();
		
	}
	
}
