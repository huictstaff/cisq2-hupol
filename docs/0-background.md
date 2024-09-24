# HU CISQ2 Opdracht

In deze opdracht gaan we werken aan een project genaamd *Hupol*, 
met als doel het analyseren en verbeteren van de 
*onderhoudbaarheid*, *veiligheid*, *prestaties* en *productieklaar maken* 
van een softwaresysteem.

Dit is een educatief werk van fictie, 
elke overeenkomst met echte personen, gebeurtenissen of systemen 
is puur toevallig. Dit startproject 
dient niet als voorbeeld te worden genomen 
voor daadwerkelijke ontwikkeling.

## Achtergrond
Ondanks problemen dicht bij huis, 
streeft Elron Husky, CEO van Husky Martian Political Systems, 
naar de sterren --- letterlijk. 

Omdat hij de bewoning van andere planeten onvermijdelijk acht, 
wil hij voorbereid zijn op het moment dat het zover is: 

> ![Elron Husky, a husky dog in a suit](img/elron-husky.png)
>
> __Elron Husky, CEO of *Husky Martian Political Systems*:__
>"Let's do democracy right from the start. 
> We need to develop a toolkit for digital 
> democracy and politics. And we need to be the ones
> to control it!"

Ondanks de [risico's](https://www.cylumena.com/insights/8-cybersecurity-reasons-online-voting-never-happen/) en [uitdagingen](https://www.aaas.org/epi-center/internet-online-voting)
van [e-voting](https://www.youtube.com/watch?v=LkH2r-sNjQs) 
besloot *Husky* het *Hupol* systeem te creëren, 
een project gericht op buitenaardse digitale verkiezingen. 
Met andere woorden: peilingen en stemmen voor Marsbewoners!

De eerste iteratie van Hupol werd ontworpen als een proof of concept 
om te zien of een stem- of peiling-API haalbaar is. 
Los van de sceptische houding in de maatschappij 
en andere praktische bezwaren, was Elron Husky tevreden. 
Het is tijd voor de volgende stap! 
Voordat het project echt kan knallen
is *ons*, software development experts, 
gevraagd om de technische schuld af te lossen 
die is opgebouwd tijdens het experiment.

We benaderen het probleem van verschillende hoeken:
*maintainability*, *security* en *operations*. Deze komen overeen met de drie pijlers uit ISO25010-model, dat we nog kennen uit CISQ1.
Per opdracht kijk je naar een andere pijler, waarbij er opgemerkt moet worden dat we dit onderwerk niet uitputtend kunnen behandelen.
Per opdracht bekijken we elke keer vanuit een andere invalshoek de staat van het systeem,
bedenken we wat er beter kan en voeren we deze door
of geven we advies.

## Technical stack

* Java 17+
* Spring Boot
* Hibernate
* PostgreSQL
* Docker
* JUnit
* Mockito
* H2
* Static analysis tools (e.g. Sonar, ErrorProne, Infer, CheckStyle)
* ArchUnit
* IntelliJ / VisualVM
* JMH
* Apache Benchmark

## Development

Voor deze cursus moeten we Docker kunnen gebruiken.

Gebruik voor development `docker-compose up` om 
een database te starten. Deze maakt twee databases
aan: `cisq2-hupol` en `cisq2-hupol-test`.

### Postman

Onder `hupol.postman_collection.json` is een Postman
collection opgenomen. Deze kan je gebruiken om
de API te gebruiken, maar je moet natuurlijk wel
zelf de juiste parameters invoeren onder de juiste
omstandigheden. 

Zorg dat je een nieuwe gebruiker registreert en inlogt. 
Na het inloggen krijg je 
in de authentication header van je
response een JWT (Bearer) token terug.
Voor de handigheid hebben we een script in 
de collection opgenomen die de token onthoudt 
en meestuurt bij elke request.

Voor het importeren van CSV-bestanden
kan je gebruik maken van de files die
opgenomen zijn onder `test/resources/fixtures`,
maar je hoeft deze misschien niet te gebruiken.

### Spring Profiles

We maken gebruik van 3 [Spring profiles](https://docs.spring.io/spring-boot/docs/3.0.5/reference/html/features.html#features.external-config.files.profile-specific):
* de default profile (deze laadt `src/main/resources/application.properties`)
* de test profile (deze laadt `src/test/resources/application.properties`)
* de ci profile (deze laadt `src/test/resources/application-ci.properties`)

Gebruik deze laatste in een build pipeline of lokaal
om zonder PostgreSQL te hoeven werken (`"-Dspring.profiles.active=ci"`).

## Ontwerp

### Functionaliteit

Het systeem bestaat uit vier componenten:
* _security_: registreren, inloggen en mensen admin maken
* _candidates_: het invoeren van (kleine) CSV-bestanden om kandidaten voor verkiezingen in te voeren
* _votes_: het invoeren van (kleine) CSV-bestanden om stemmen voor kandidaten tijdens verkiezingen in te voeren 
* _results_: het berekenen van de resultaten van een verkiezing per kandidaat

### Structuur

Er wordt gewerkt met *componenten* welke zijn onderverdeeld
in *lagen*:
* _presentation_: toegangspunt externe infrastructuur naar binnen toe
* _application_: services die taakgerichte use cases aanbieden
* _domain_: entiteiten die acties aanbieden
* _data_: uitgang naar externe data-infrastructuur

Hiervoor gelden de volgende communicatieregels:
* _presentation_: mag alleen naar naar servicelaag communiceren
* _application_: mag alleen naar andere services, domein en datalaag communiceren
* _domein_: mag alleen naar de data-laag communiceren (dmv annotations)
* Deze relaties mogen niet andersom worden gelegd.

### Security

#### Authentication
Behalve voor inloggen en registreren, mogen acties alleen
worden uitgevoerd door ingelogde gebruikers.

#### Authorization
Alleen admins mogen votes en candidates importeren 
en admins maken / verwijderen.