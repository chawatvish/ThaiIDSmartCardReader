package smartcard.common;

/**
 * SmartCardAdapter
 * 
 * Platform-independent interface for smart card communication.
 * Implement this interface for each platform (e.g., Desktop, Android).
 * 
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
public interface SmartCardAdapter {
    /**
     * Connect to the smart card.
     * 
     * @throws Exception if connection fails
     */
    void connect() throws Exception;

    /**
     * Transmit an APDU command and return the response bytes.
     * 
     * @param apdu APDU command bytes
     * @return response bytes
     * @throws Exception if transmit fails
     */
    byte[] transmit(byte[] apdu) throws Exception;

    /**
     * Disconnect from the smart card.
     * 
     * @throws Exception if disconnect fails
     */
    void disconnect() throws Exception;
}