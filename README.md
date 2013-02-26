NFC
===

NFC Projektarbete

Hela appen:
-------------------------------------------------------------------
Appen använder inte det mordernare Android Beam utan Ndef Push Protocol (NPP) då det är det som biblioteket för Arduinon
använder.

-Om det finns en annan nfc-enhet i närheten kommer "Tryck här för att överföra" upp.

MainActivity.java
-------------------------------------------------------------------
Funktionalitet:
- Huvudaktivitet
- Sköter kommunikation
- Tvinga användaren att sätta på NFC vid start.
- Knapp till inställningar.

SuccessActivity.java
-------------------------------------------------------------------
Om det har skett en lyckad upplåsning startar SuccessActivity


FailActivity.java
-------------------------------------------------------------------
Om det inte går att låsa upp låset startar FailActivity + felkod


SettingsActivity.java
-------------------------------------------------------------------
Inställningsmeny


PasswordActivity.java
-------------------------------------------------------------------
Aktivitet för att välja och byta lösenord




