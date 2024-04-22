package abhishek.pathak.mapsdemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar

fun Context.hasPermission(permission: String): Boolean {
    if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION) {
        return true
    }
    return ActivityCompat.checkSelfPermission(this, permission)== PackageManager.PERMISSION_GRANTED
}

fun Activity.requirePermissionWithRationale(
    permission: String,
    requestCode: Int,
    snackBar: Snackbar
) {
    val provideRationale = shouldShowRequestPermissionRationale(permission)
    if (provideRationale) {
        snackBar.show()
    } else {
        requestPermissions(arrayOf(permission), requestCode)
    }
}