package smartcard.android

import android.nfc.tech.IsoDep
import smartcard.common.SmartCardAdapter

/**
 * AndroidSmartCardAdapter
 *
 * Adapter for smart card communication using Android NFC (IsoDep).
 * Implements SmartCardAdapter interface for platform-independent logic.
 *
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
class AndroidSmartCardAdapter(private val isoDep:IsoDep):SmartCardAdapter
{

    init {
        requireNotNull(isoDep) { "IsoDep must not be null." }
    }

    @Throws(Exception::class)
    override fun

    connect() {
        if (!isoDep.isConnected) {
            isoDep.connect()
        }
    }

    @Throws(Exception::class)
    override fun

    transmit(apdu: ByteArray): ByteArray {
        return isoDep.transceive(apdu)
    }

    @Throws(Exception::class)
    override fun

    disconnect() {
        if (isoDep.isConnected) {
            isoDep.close()
        }
    }
}