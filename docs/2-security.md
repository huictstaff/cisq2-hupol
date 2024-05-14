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
Kijk kritisch naar naar de `security` component, maar ook de rest van het project.
Bevindingen, risico's en bedenkingen schrijven we op. Verbeteringen voeren we door.
Hierbij maken we, waar mogelijk, gebruik van *static analysis tools*.


### Stap 1. Bestudeer de code en het materiaal

Bekijk de code van de security-component, lees de opdracht door en doorgrond het lesmateriaal.

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
Licht beweringen toe en vermeld gebruikte bronnen. Maak een nieuw markdown-bestand
`docs/2-notes.md`. Schrijf op of een securityrisico mogelijk kritisch is.

Bekijk bijvoorbeeld ook hoe Spring omgaat met
[security](https://spring.io/guides/topicals/spring-security-architecture/)
[authentication](https://www.baeldung.com/spring-security-basic-authentication)
en [authorization](https://www.baeldung.com/role-and-privilege-for-spring-security-registration).
Hoe is dat in dit project geregeld? Kijk in de code, de tests, de database en de docs. Er zijn ongeveer 35 zaken te vinden, waarvan 10 kritisch

Commit en push je werk.
Denk aan een zinvolle, beschrijvende commit message.

### Stap 3. Automatische security check

Voer een automatische analyse uit met [Snyk](https://docs.snyk.io/integrate-with-snyk/snyk-ci-cd-integrations/maven-plugin-integration-with-snyk).
Ga naar [Snyk.io](https://app.snyk.io/account) voor een API-key nadat je je account hebt aangemaakt. Zet ook `Synk Code` aan in de instellingen.
Hier kan je ook het rapport inzien, wil je dit lokaal inzien gebruik dan Snyk CLI.
Gebruik `mvn verify` om de Snyk maven plugin te runnen, zowel lokaal als in de pipeline.
Zet de API-key in application.properties en voeg dit bestand toe aan de git.ignore.

De geavanceerdere optie is om via de GitHub CLI de REST-api aan te spreken en je api-key als [secret mee te geven ](https://devopsjournal.io/blog/2022/11/02/GitHub-secrets-without-admin-rights)

De gevonden verbeteringen verbeter je in stap 4, plaats hier het rapport van de door Snyk gevonden issues bij.
Noteer wat je opvalt in `docs/2-notes.md`.
Vermeld gebruikte bronnen! Weet dat er meer security issues inzitten dan wat de automatische security check aangeeft.

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.

### Stap 4. Voer verbeteringen door
Pas de `security` component aan waar nodig om de security te verbeteren.
Voer niet alleen de verbeteringen door die uit de geautomatiseerde
security analysis is gekomen, maar ook wat je zelf denkt dat beter kan.

Maak een notitie van bronnen in `docs/2-notes.md`, anders is het mogelijk plagiaat.

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog? Haak Synk in op de maven lifecycle.
Denk aan een zinvolle, beschrijvende commit message.

### Stap 5. Reflecteer
Wat voor gevolgen zijn er voor maintainability wanneer we bovenstaande security fixes toepassen? Beschrijf eventuele trade-offs in eigen woorden. Gebruik van generatieve AI is voor dit gedeelte verboden.
Noteer wat je ideeën zijn hierover in `docs/2-notes.md`. Maak een coherent en goedlopend verhaal.
Vermeld gebruikte bronnen!

Commit en push je werk.
