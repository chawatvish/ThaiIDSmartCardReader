import javax.smartcardio.CardTerminal;
import java.util.List;

import smartcard.desktop.DesktopSmartCardAdapter;
import smartcard.common.ImageDecoder;
import smartcard.common.SmartCardAdapter;
import smartcard.common.ThaiIDCardLogic.ThaiPeople;
import smartcard.common.ThaiIDCardService;
import smartcard.desktop.DesktopImageDecoder;

public class Main {

    /**
     * Main method to run the Thai ID card reading service.
     * Lists available card terminals, connects to the first one,
     * reads the Thai ID card data, and prints the results.
     */
    public static void main(String[] args) {
        try {
            // List available card terminals
            List<CardTerminal> terminals = DesktopSmartCardAdapter.listTerminals();
            if (terminals.isEmpty()) {
                System.out.println("No card readers found.");
                return;
            }

            // Use the first terminal
            SmartCardAdapter adapter = new DesktopSmartCardAdapter(terminals.get(0));
            ImageDecoder decoder = new DesktopImageDecoder();

            // Create the service
            ThaiIDCardService service = new ThaiIDCardService(adapter, decoder);

            // Read Thai ID card data
            ThaiPeople person = service.readThaiPeople();

            // Print results
            System.out.println("ID: " + person.id);
            System.out.println("Name (TH): " + person.nameTH);
            System.out.println("Name (EN): " + person.nameEN);
            System.out.println("Birthday: " + person.birthday);
            System.out.println("Photo (base64): " + person.photo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}