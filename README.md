# ReverseGeocoding

Diese Library erzeugt aus Längen- und Breitengrad Städtenamen. Falls man Längen- und Breitengrad von **keiner** Stadt angegeben hat bekommt man eine generische Antwort: "Kein Ort gefunden."

Die Benutzung ist super einfach: 

```java
Converter converter = new Converter();
converter.getLocationByLonLat(44.0229224, 22.8277253, SearchType.COUNTRY)
```

Als SearchTypes existieren ```TOWN, COUNTRY, POSTCODE, STREET```. Diese geben an was das Ergebnis der Umwandlung sein soll.
