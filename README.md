NFC
===

NFC Projektarbete

Hela appen:
-------------------------------------------------------------------
Appen använder inte det mordernare Android Beam utan Ndef Push Protocol (NPP) då det är det som biblioteket för Arduinon
använder.



MainActivity.java
-------------------------------------------------------------------
Funktionalitet:
- Tvinga användaren att sätta på NFC vid start.
- Kolla om deet finns låsinfo. I så fall visa knapp "lås upp" annars inställningar.
-Stor lås-upp-knapp och inställningar-knapp.
-Eventuelt någon lampa som talar om om det finns lås inom radien.





SuccesActivity.java
-------------------------------------------------------------------
Här visas om det gick eller inte. Eventuelt felmddelande.


FailActivity.java
-------------------------------------------------------------------
Fel
Ger felmeddelande


SettingsActivity.java
------------------------------------------------------------------
Alla möjliga tänkbara inställningar
