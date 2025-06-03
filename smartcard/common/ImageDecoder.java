package smartcard.common;

/**
 * ImageDecoder
 * 
 * Platform-independent interface for validating image data (e.g., photo from
 * Thai ID card).
 * Implement this interface for each platform (e.g., Desktop, Android) to check
 * if a byte array
 * represents a valid image.
 * 
 * Example implementations:
 * - Desktop: Use ImageIO and BufferedImage.
 * - Android: Use BitmapFactory.
 * 
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
public interface ImageDecoder {
    /**
     * Checks if the given byte array is a valid image.
     * 
     * @param imageBytes image data as byte array
     * @return true if valid image, false otherwise
     */
    boolean isValidImage(byte[] imageBytes);
}