# 3. Security Assignment

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

Schrijf in op wat er volgens jou beter kan
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

Voer een automatische analyse uit met de
[SonarQube](https://www.baeldung.com/sonar-qube).
Deze tool checkt ook je code style.
Voel je vrij om deze zaken ook te verbeteren,
maar wij zijn voor deze opdracht geïnteresseerd in wat wordt
genoemd onder *vulnerabilities*. 

Noteer wat je opvalt in `docs/2-notes.md`. 
Vermeld gebruikte bronnen!

Voeg deze tool ook toe aan je
build pipeline (Maven en GitHub Actions).

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.


### Stap 4. Automatische dependency check

Voer een automatische analyse uit met de 
[OWASP dependency check](https://owasp.org/www-project-dependency-check/). 
Wat valt je op?

Noteer wat je opvalt in `docs/2-notes.md`. Vermeld gebruikte bronnen!

Voeg deze tool ook toe aan je 
build pipeline (Maven en GitHub Actions).

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.

### Stap 5. Voer verbeteringen door
Pas de `security` component aan waar nodig om de security te verbeteren.
Voer niet alleen de verbeteringen door die uit de geautomatiseerde 
security analysis is gekomen, maar ook wat je zelf denkt dat beter kan.

Je kan hier ook gebruik maken van AI en andere bronnen.
Maak hier een notitie van in een comment of in `docs/2-notes.md`.

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.

### Stap 6. Reflecteer

Rufus, product owner, geeft tijdens een Scrum backlog-sessie aan
dat we binnenkort ook XML-bestanden moeten ondersteunen
om kandidaten en stemmen in te voeren. Mogelijk dat er in de toekomst
zelfs nog meer andere file formats nodig zijn voor de importeer-functionaliteit.

Waar moeten qua security we opletten wanneer we XML en andere file formats
toevoegen aan het project? Bekijk de OWASP top 10.
Noteer wat je ideeën zijn hierover in `docs/2-notes.md`.
Vermeld gebruikte bronnen!

Commit en push je werk.