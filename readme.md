# Thai National ID Card Reader (Java/Android)

A cross-platform library for reading all personal data from a Thai National ID card using Java (Desktop) or Android (NFC/USB).  
Supports modular adapters for smart card communication and image decoding.

---

## Features

- Read all fields from Thai National ID card (CID, names, birthday, gender, address, issue/expiry date, issuer, photo)
- Platform-independent business logic (`ThaiIDCardLogic`)
- Adapter interfaces for smart card and image decoding
- Desktop support via PC/SC (`javax.smartcardio`)
- Android support via NFC (`IsoDep`) or USB (implement your own)
- Logging for debugging and troubleshooting

---

## Project Structure

```
/common      # Platform-independent logic and interfaces
/desktop     # Desktop (Java SE) adapters
/android     # Android adapters (NFC/USB)
main.java    # Example usage for desktop
```

---

## Quick Start (Desktop)

### Requirements

- Java 8 or later (with `javax.smartcardio`)
- PC/SC-compatible smart card reader and drivers
- Thai National ID card

### Example

```java
import javax.smartcardio.CardTerminal;
import java.util.List;
import smartcard.desktop.DesktopSmartCardAdapter;
import smartcard.desktop.DesktopImageDecoder;
import smartcard.common.ThaiIDCardService;
import smartcard.common.ThaiIDCardLogic.ThaiPeople;

public class Main {
    public static void main(String[] args) {
        try {
            List<CardTerminal> terminals = DesktopSmartCardAdapter.listTerminals();
            if (terminals.isEmpty()) {
                System.out.println("No card readers found.");
                return;
            }
            DesktopSmartCardAdapter adapter = new DesktopSmartCardAdapter(terminals.get(0));
            DesktopImageDecoder decoder = new DesktopImageDecoder();
            ThaiIDCardService service = new ThaiIDCardService(adapter, decoder);

            ThaiPeople person = service.readThaiPeople();
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
```

---

## Quick Start (Android)

### Requirements

- Android device with NFC (or USB Host for USB readers)
- Thai National ID card

### Example (NFC)

```kotlin
val isoDep = IsoDep.get(tag) // tag from NFC intent
val adapter = AndroidSmartCardAdapter(isoDep)
val decoder = AndroidImageDecoder()
val service = ThaiIDCardService(adapter, decoder)

val person = service.readThaiPeople() // Call in background thread!
```

---

## How It Works

- **Business logic** (`ThaiIDCardLogic`, `ThaiIDCardService`) is platform-independent.
- **Adapters** (`SmartCardAdapter`, `ImageDecoder`) abstract platform-specific APIs.
- **Desktop**: Use `DesktopSmartCardAdapter` and `DesktopImageDecoder`.
- **Android**: Use `AndroidSmartCardAdapter` and `AndroidImageDecoder`.

---

## Extending

- Implement your own `SmartCardAdapter` for other platforms (e.g., USB on Android).
- Implement your own `ImageDecoder` if needed.

---

---

## Future Work

[] Create example implementation on Android side.

---

## License

MIT License

---

## Author

Chawatvish Worrapoj

---

## Contributions

Pull requests and issues are welcome!
