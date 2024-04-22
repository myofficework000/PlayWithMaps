package abhishek.pathak.mapsdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


fun vectorToBitmapDesc(context: Context, vectorId: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorId)!!.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }

    val bitMap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitMap)
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitMap)
}