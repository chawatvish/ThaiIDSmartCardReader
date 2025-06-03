package smartcard.common;

import java.util.logging.Logger;

import smartcard.common.ThaiIDCardLogic.ThaiPeople;

/**
 * ThaiIDCardService
 * 
 * Provides a platform-independent service to read all personal data from a Thai
 * National ID card.
 * Uses a SmartCardAdapter for card communication.
 * 
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
public class ThaiIDCardService {

    private static final Logger LOGGER = Logger.getLogger(ThaiIDCardService.class.getName());

    private final SmartCardAdapter adapter;
    private final ImageDecoder decoder;

    // APDU command to select the Thai ID card application
    private static final byte[] SELECT_FILE_APDU = new byte[] {
            0x00, (byte) 0xA4, 0x04, 0x00, 0x08,
            (byte) 0xA0, 0x00, 0x00, 0x00, 0x54, 0x48, 0x00, 0x01
    };

    /**
     * Constructor for ThaiIDCardService.
     * 
     * @param adapter SmartCardAdapter for card communication
     * @param decoder ImageDecoder for decoding card images
     */
    public ThaiIDCardService(SmartCardAdapter adapter, ImageDecoder decoder) {
        this.adapter = adapter;
        this.decoder = decoder;
    }

    /**
     * Reads all personal data from a Thai National ID card.
     * 
     * @return ThaiPeople object with all fields populated.
     * @throws Exception if card not present or communication fails.
     */
    public ThaiPeople readThaiPeople() throws Exception {
        LOGGER.info("Connecting to smart card...");
        adapter.connect();

        ThaiPeople person = new ThaiPeople();

        // Select file
        LOGGER.fine("Selecting Thai ID card application...");
        adapter.transmit(SELECT_FILE_APDU);

        // APDU commands (same as ThaiIDCardReader)
        LOGGER.fine("Reading CID...");
        byte[] cid = adapter.transmit(ThaiIDCardCommand.CID);
        person.id = ThaiIDCardLogic.tis620ToString(cid);

        LOGGER.fine("Reading Thai full name...");
        byte[] nameTH = adapter.transmit(ThaiIDCardCommand.TH_FULLNAME);
        person.nameTH = ThaiIDCardLogic.tis620ToString(nameTH);

        LOGGER.fine("Reading English full name...");
        byte[] nameEN = adapter.transmit(ThaiIDCardCommand.EN_FULLNAME);
        person.nameEN = ThaiIDCardLogic.tis620ToString(nameEN);

        LOGGER.fine("Reading date of birth...");
        byte[] birth = adapter.transmit(ThaiIDCardCommand.DATE_OF_BIRTH);
        person.birthday = ThaiIDCardLogic.beDateStringToLocalDate(ThaiIDCardLogic.tis620ToString(birth));

        LOGGER.fine("Reading gender...");
        byte[] genderRaw = adapter.transmit(ThaiIDCardCommand.GENDER);
        person.gender = ThaiIDCardLogic.parseGender(ThaiIDCardLogic.tis620ToString(genderRaw));

        LOGGER.fine("Reading address...");
        byte[] address = adapter.transmit(ThaiIDCardCommand.ADDRESS);
        person.address = ThaiIDCardLogic.tis620ToString(address);

        LOGGER.fine("Reading issue date...");
        byte[] issue = adapter.transmit(ThaiIDCardCommand.ISSUE_DATE);
        person.issueDate = ThaiIDCardLogic.beDateStringToLocalDate(ThaiIDCardLogic.tis620ToString(issue));

        LOGGER.fine("Reading expiry date...");
        byte[] expiry = adapter.transmit(ThaiIDCardCommand.EXPIRE_DATE);
        person.expiryDate = ThaiIDCardLogic.beDateStringToLocalDate(ThaiIDCardLogic.tis620ToString(expiry));

        LOGGER.fine("Reading card issuer...");
        byte[] issuer = adapter.transmit(ThaiIDCardCommand.CARD_ISSUER);
        person.issuer = ThaiIDCardLogic.tis620ToString(issuer);

        // Read photo (20 parts, 255 bytes each)
        LOGGER.fine("Reading photo parts...");
        byte[] photoBytes = new byte[20 * 255];
        int offset = 0;
        for (int i = 0; i < 20; i++) {
            LOGGER.finer("Reading photo part " + (i + 1) + "/20...");
            byte[] photoPart = adapter.transmit(ThaiIDCardCommand.photoPart(i));
            System.arraycopy(photoPart, 0, photoBytes, offset, photoPart.length);
            offset += photoPart.length;
        }
        LOGGER.fine("Validating photo image...");
        if (!decoder.isValidImage(photoBytes)) {
            LOGGER.severe("Photo is not a valid image.");
            throw new RuntimeException("Photo is not a valid image.");
        }
        person.photo = ThaiIDCardLogic.toBase64(photoBytes);

        LOGGER.info("All data read successfully.");
        adapter.disconnect();
        return person;
    }

    /**
     * ThaiIDCardCommand: APDU commands for Thai ID card fields.
     */
    public static class ThaiIDCardCommand {
        public static final byte[] CID = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, 0x04, 0x02, 0x00, 0x0D };
        public static final byte[] TH_FULLNAME = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, 0x11, 0x02, 0x00, 0x64 };
        public static final byte[] EN_FULLNAME = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, 0x75, 0x02, 0x00, 0x64 };
        public static final byte[] DATE_OF_BIRTH = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, (byte) 0xD9, 0x02, 0x00,
                0x08 };
        public static final byte[] GENDER = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, (byte) 0xE1, 0x02, 0x00,
                0x01 };
        public static final byte[] CARD_ISSUER = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, (byte) 0xF6, 0x02, 0x00,
                0x64 };
        public static final byte[] ISSUE_DATE = new byte[] { (byte) 0x80, (byte) 0xB0, 0x01, 0x67, 0x02, 0x00, 0x08 };
        public static final byte[] EXPIRE_DATE = new byte[] { (byte) 0x80, (byte) 0xB0, 0x01, 0x6F, 0x02, 0x00, 0x08 };
        public static final byte[] ADDRESS = new byte[] { (byte) 0x80, (byte) 0xB0, 0x15, 0x79, 0x02, 0x00, 0x64 };

        // Generate APDU for photo part (20 parts, 255 bytes each)
        public static byte[] photoPart(int part) {
            return new byte[] {
                    (byte) 0x80, (byte) 0xB0, (byte) (0x01 + part), (byte) (0x7B - part), 0x02, 0x00, (byte) 0xFF
            };
        }
    }
}