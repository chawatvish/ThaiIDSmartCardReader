package smartcard.desktop;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

import smartcard.common.ImageDecoder;

/**
 * DesktopImageDecoder
 * 
 * Utility for decoding image bytes (e.g., from Thai ID card) on Java Desktop.
 * Provides methods to check if bytes are a valid image and to decode to
 * BufferedImage.
 * 
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
public class DesktopImageDecoder implements ImageDecoder {

    /**
     * Checks if the given byte array is a valid image.
     * 
     * @param imageBytes image data
     * @return true if valid image, false otherwise
     */
    @Override
    public boolean isValidImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            return false;
        }
        try (ByteArrayInputStream in = new ByteArrayInputStream(imageBytes)) {
            BufferedImage img = ImageIO.read(in);
            return img != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Decodes the given byte array to a BufferedImage.
     * 
     * @param imageBytes image data
     * @return BufferedImage, or null if decoding fails
     */
    public static BufferedImage decode(byte[] imageBytes) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(in);
        } catch (Exception e) {
            return null;
        }
    }
}