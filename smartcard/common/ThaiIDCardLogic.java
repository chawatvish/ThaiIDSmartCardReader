package smartcard.common;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Base64;

/**
 * ThaiIDCardLogic
 * 
 * Platform-independent logic for parsing and handling Thai National ID card
 * data.
 * Use this class in both desktop and Android projects by providing your own
 * card communication and image decoding adapters.
 * 
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
public class ThaiIDCardLogic {

    /**
     * Gender enum for Thai ID card.
     */
    public enum Gender {
        MALE, FEMALE, NOT_SPECIFIED, UNKNOWN
    }

    /**
     * Data model for Thai people information from the ID card.
     */
    public static class ThaiPeople {
        public String id; // Citizen ID
        public String nameTH; // Thai full name
        public String nameEN; // English full name
        public LocalDate birthday; // Birthday (CE)
        public Gender gender; // Gender
        public String address; // Address (Thai)
        public LocalDate issueDate; // Card issue date (CE)
        public LocalDate expiryDate;// Card expiry date (CE)
        public String issuer; // Card issuer
        public String photo; // Photo as base64 JPEG string
    }

    /**
     * Converts BE date string (yyyyMMdd) to LocalDate (CE).
     */
    public static LocalDate beDateStringToLocalDate(String beDate) {
        if (beDate == null || !beDate.matches("\\d{8}")) {
            return null;
        }
        try {
            int yearBE = Integer.parseInt(beDate.substring(0, 4));
            int yearCE = yearBE - 543;
            int month = Integer.parseInt(beDate.substring(4, 6));
            int day = Integer.parseInt(beDate.substring(6, 8));
            return LocalDate.of(yearCE, month, day);
        } catch (NumberFormatException | DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Converts a byte array to a TIS-620 string.
     */
    public static String tis620ToString(byte[] data) {
        try {
            return new String(data, "TIS620").trim();
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("TIS620 encoding not supported", e);
        }
    }

    /**
     * Converts a byte array to a base64 string.
     */
    public static String toBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Gender mapping from Thai ID card raw value.
     */
    public static Gender parseGender(String rawGender) {
        if (rawGender == null)
            return Gender.UNKNOWN;
        switch (rawGender.trim()) {
            case "1":
                return Gender.MALE;
            case "2":
                return Gender.FEMALE;
            case "3":
                return Gender.NOT_SPECIFIED;
            default:
                return Gender.UNKNOWN;
        }
    }
}