package smartcard.android

import android.graphics.BitmapFactory
import smartcard.common.ImageDecoder

/**
 * AndroidImageDecoder
 *
 * Image decoder for Android using BitmapFactory.
 *
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
class AndroidImageDecoder:ImageDecoder
{
    override fun isValidImage(imageBytes: ByteArray?): Boolean {
        if (imageBytes == null || imageBytes.isEmpty()) {
            return false
        }
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size) != null
    }
}