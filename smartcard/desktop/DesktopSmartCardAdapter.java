package smartcard.desktop;

import smartcard.common.SmartCardAdapter;
import javax.smartcardio.*;
import java.util.List;

/**
 * DesktopSmartCardAdapter
 * 
 * Adapter for smart card communication using javax.smartcardio on Java Desktop.
 * Implements SmartCardAdapter interface for platform-independent logic.
 * 
 * Author: Chawatvish Worrapoj
 * License: MIT
 */
public class DesktopSmartCardAdapter implements SmartCardAdapter {

    private CardTerminal terminal;
    private Card card;
    private CardChannel channel;

    /**
     * List all available card terminals.
     * 
     * @return List of CardTerminal
     * @throws Exception if no terminals found
     */
    public static List<CardTerminal> listTerminals() throws Exception {
        TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();
        if (terminals.isEmpty()) {
            throw new IllegalStateException("No card terminals found.");
        }
        return terminals;
    }

    /**
     * Create adapter for a specific terminal.
     * 
     * @param terminal CardTerminal to use
     */
    public DesktopSmartCardAdapter(CardTerminal terminal) {
        if (terminal == null)
            throw new IllegalArgumentException("CardTerminal must not be null.");
        this.terminal = terminal;
    }

    /**
     * Connect to the card in the terminal.
     * 
     * @throws Exception if card not present or connection fails
     */
    @Override
    public void connect() throws Exception {
        if (!terminal.isCardPresent()) {
            throw new IllegalStateException("Insert smart card.");
        }
        card = terminal.connect("T=0");
        channel = card.getBasicChannel();
    }

    /**
     * Transmit an APDU command and return the response bytes.
     * 
     * @param apdu APDU command bytes
     * @return response bytes
     * @throws Exception if transmit fails
     */
    @Override
    public byte[] transmit(byte[] apdu) throws Exception {
        if (channel == null)
            throw new IllegalStateException("Not connected to card.");
        CommandAPDU command = new CommandAPDU(apdu);
        ResponseAPDU response = channel.transmit(command);
        if (response.getSW() != 0x9000) {
            throw new CardException("APDU failed. SW=" + Integer.toHexString(response.getSW()));
        }
        return response.getData();
    }

    /**
     * Disconnect from the card.
     * 
     * @throws Exception if disconnect fails
     */
    @Override
    public void disconnect() throws Exception {
        if (card != null) {
            card.disconnect(false);
            card = null;
            channel = null;
        }
    }

    /**
     * Get the CardChannel for advanced use.
     */
    public CardChannel getChannel() {
        return channel;
    }
}