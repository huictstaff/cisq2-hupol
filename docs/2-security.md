# Opdracht 2: ISO25010 pijler: security

> ![Elron Husky, a husky dog in a suit](img/elron-husky.png)
>
> __Elron Husky, CEO of *Husky Martian Political Systems*:__
>
> "Voting information can be considered sensitive information.
> That's why we at Hupol made a gigantic effort
> into securing our system --- and, of course, our user's data!
> This means we are *very* careful about who is able to access what.
> We put in the work!
> A small price to pay for democratizing the galaxy, don't you agree?"

## De opdracht
Wat een grote ambities! Het is belangrijk dat zo'n applicatie
goed beveiligd is. Daarom is ons gevraagd om kritisch naar
te kijken naar de `security` component, maar ook de rest van het project.
Bevindingen, risico's en bedenkingen schrijven we op. Verbeteringen voeren we door.
Hierbij maken we, waar mogelijk, gebruik van *static analysis tools*.


### Stap 1. Bestudeer de code en het materiaal

Bekijk de code van de security-component,
lees de opdracht door en doorgrond het lesmateriaal.

Waar moeten we op letten wanneer het over web security gaat?
Wat zijn veelvoorkomende problemen? Hoe kan je deze oplossen?
Wat zijn de security-overwegingen en het beleid bij deze applicatie?

### Stap 2. Noteer welke risico's en problemen je ziet

Maak een nette indeling met kopjes in een markdowndocument en schrijf puntsgewijs op wat er volgens jou beter kan
wat betreft de code van de `security` component. Denk hierbij aan de
[wachtwoorden](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html),
[OWASP top 10](https://owasp.org/www-project-top-ten/),
de [CIA-triad](https://www.fortinet.com/resources/cyberglossary/cia-triad)
en eventuele ethische of juridische overwegingen die je hebt
bij dit project.
Licht beweringen toe en vermeld gebruikte bronnen.

Dat kan je doen in een nieuw markdown-bestand
(bijvoorbeeld `docs/2-notes.md`).

Bekijk bijvoorbeeld ook hoe Spring omgaat met
[security](https://spring.io/guides/topicals/spring-security-architecture/)
[authentication](https://www.baeldung.com/spring-security-basic-authentication)
en [authorization](https://www.baeldung.com/role-and-privilege-for-spring-security-registration).
Hoe is dat in dit project geregeld? Kijk in de code, de tests, de database en de docs.

Commit en push je werk.
Denk aan een zinvolle, beschrijvende commit message.

### Stap 3. Automatische security check

Voer een automatische analyse uit met [Snyk](https://snyk.io/).
Van oorsprong is Snyk een dependency checker die aangeeft welke dependencies er worden gebruikt met bekende kwetsbaarheden.
Later is hier Static Analysis Security Tests (SAST) tegenaan geplakt. Om dit te gebruiken moet je dit wel eerst aanzetten via de webinterface.

Je kan de command line interface (CLI) gebruiken om Snyk te draaien, aan te raden is om deze via npm te installeren.
Voor de dependencies wordt `snyk test` gebruikt, voor SAST wordt `snyk code test` gebruikt.
Om een rapportje in de webinterface te tonen kun je `snyk monitor` gebruiken.

De gevonden verbeteringen verbeter je in stap 4.
Noteer wat je opvalt in `docs/2-notes.md`.
Vermeld gebruikte bronnen! Weet dat er meer security issues inzitten dan wat de automatische security check aangeeft.

Voeg deze tool ook toe aan je build pipeline (GitHub Actions). Zie hiervoor de slides.

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.

### Stap 4. Voer verbeteringen door
Pas de `security` component aan waar nodig om de security te verbeteren.
Voer niet alleen de verbeteringen door die uit de geautomatiseerde
security analysis is gekomen, maar ook wat je zelf denkt dat beter kan.

Je kan hier ook gebruik maken van generatieve AI en andere bronnen.
Maak hier een notitie van in `docs/2-notes.md`, anders is het mogelijk plagiaat.

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.

### Stap 5. Reflecteer
Wat voor gevolgen zijn er voor maintainability wanneer we bovenstaande security fixes toepassen? Beschrijf eventuele trade-offs in eigen woorden. Gebruik van generatieve AI is voor dit gedeelte verboden.
Noteer wat je ideeën zijn hierover in `docs/2-notes.md`. Maak een coherent en goedlopend verhaal.
Vermeld gebruikte bronnen!

Commit en push je werk.
