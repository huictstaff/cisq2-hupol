# 3. ISO25010 pijler: operations

> ![Elron Husky, a husky dog in a suit](img/elron-husky.png)
>
> __Elron Husky, CEO of *Husky Martian Political Systems*:__
>
> "Working on Hupol and keeping it in production is a breeze.
> Whether your working on your development environment
> or in production: just install a few programs and 
> dependencies for your machine and you're good
> to go. Now that's *dev-prod parity*! 
> Configuration is as easy, just edit the 
> every file when needed!

## De opdracht
Het plan is om Hupol op termijn op grote schaal uit te rollen. 
Cloud platforms als Amazon Web Services (AWS), Microsoft Azure of
Google Cloud Platform (GCP) maken vaak gebruik
van [containers](https://www.docker.com/resources/what-container/). 
Een container is een abstractie om 
software te kunnen benaderen op een gelijkvormige manier,
onafhankelijk van programmeertaal, operating system of andere
omgevingsfactoren. Containers zijn belangrijkse bouwstenen van 
moderne cloud infrastructuur en worden steeds 
populairder onder ontwikkelaars. 
Containers stellen ontwikkelaars in staat om 
software te ontwikkelen zonder zich zorgen te hoeven maken 
over de complexiteit van de onderliggende infrastructuur. 
Bovendien maakt het gebruik van containers het mogelijk 
om applicaties gemakkelijker te verplaatsen
tussen verschillende omgevingen, 
zoals lokale machines en cloudomgevingen, waardoor compatibiliteit
tussen development en productie kan worden vergroot.

Maak de applicatie geschikt om te draaien
in zo'n omgeving.

### Stap 1. Bestudeer het materiaal
Neem het lesmateriaal door: slides en
eventuele links. Zorg er in het bijzonder voor
dat je begrijpt wat een Docker container is
en hoe het verschilt van een Docker image.
En: zijn een virtual machine en een Docker container
hetzelfde?

### Stap 2. Jar builden
Laten we eerst eens kijken hoe we *zonder Docker*
onze applicatie kunnen starten met de commandline:

1. Zorg dat we de database kunnen (`docker-compose up`)
2. Compileer de code (`mvn compile` of `./mvnw compile`)
3. Run de jar (`java -jar .\target\hupol-0.0.1-SNAPSHOT.jar`)

Als het goed is, start de applicatie nu op.
Je kan de applicatie weer stoppen met `CTRL + C`.

### Stap 3. Een JAR container maken
De manier om van welke applicatie dan ook een
Docker image te maken is door een 
[Dockerfile](https://docs.docker.com/engine/reference/builder/)
te schrijven. In een Dockerfile definieer je 
wat er in de container zit en hoe deze benaderd kan
worden.

Maak in onze project root een bestand aan met
de naam `Dockerfile` zonder bestandsextensie.

#### A. Een basis image gebruiken
Je schrijft meestal niet de hele image zelf,
maar baseert het op een bestaande image waar
de basisbenodigdheden inzitten. Voor uitleg van Docker zelf kijk [hier](https://docs.docker.com/language/java/build-images/#create-a-dockerfile-for-java).

Uiteindelijk heb je een dockerfile met daarin:
1. `FROM` met Java 17 JDK + tag
2. `COPY` de gecompileerde JAR kopiëren
3. `RUN` commandos
4. `ENTRYPOINT` opties

#### Extra uitleg per stap
1. Basisimage: een minimaal operating system met een
ingebakken Java Runtime Environment (JRE)
of een Java Development Kit (JDK). Deze kan je vinden op een Docker Image Registry, zoals
[Docker Hub](https://hub.docker.com/).  Laten we kiezen voor Java `17-jdk` met als platform een minimaal Linux operating system (`alpine`). 
Deze *tag* je met een versienummer en het gewenste platform. De tag is dan in dit geval: `17-jdk-alpine`.

2. Wanneer we met Maven onze applicatie compileren
(bijv. `mvn compile`), 
komen de resultaten (`.jar` voor Java) in een `target`
directory te staan. Dit wordt door de Java Virtual Machine (JVM)
uitgelezen en omgezet in voor onze computer 
uitvoerbare code. Wanneer we Docker onze container image laten
bouwen, willen we dit `.jar` bestand in de 
image overnemen. Dit kunnen we doen met de
[`COPY` command](https://docs.docker.com/engine/reference/builder/#copy). Deze instructie ontvangt twee
argumenten: de bron (op onze host machine) 
en de target (in de docker container). Voeg een witregel toe en schrijf de `COPY` instructie
op de volgende regel van onze `Dockerfile`, geef de target 
de naam `app.jar`.
3. `RUN` is bijvoorbeeld handig wanneer je een directory wilt aanmaken

4. Het startcommando aangeven zodra Docker de image opstart.
Dit doen we door de [`ENTRYPOINT` instructie](https://docs.docker.com/engine/reference/builder/#entrypoint) toe te voegen aan de `Dockerfile`.
Een entrypoint schrijf je meestal in array-notatie
(de *exec form*) in plaats van een regel text.
`ENTRYPOINT [command, parameter1, parameter2, ...]`
Schrijf eerst weer een witregel en voeg dan een `ENTRYPOINT` aan de Dockerfile toe. 
Parameters en commando's staan tussen "".

#### B. Bouw de image

We kunnen nu een image bouwen door `docker build`
in de commandline te gebruiken. We moeten wel specificeren
waar de Dockerfile gevonden kan worden. Ook kunnen we aangeven
hoe de image moet heten. Laten we `hupol/basic` aanhouden.
```bash
docker build -t hupol/basic .
```

Het puntje (`.`) geeft aan dat de Dockerfile in de huidige directory
is te vinden, terwijl je met `-t` de tag aangeven.

Met `docker image ls` kan je kijken welke docker 
images je allemaal op je computer hebt staan.

#### C. Een container configureren en draaien
```bash
docker run -it hupol/basic
```
De parameter `--name` gebruiken kan ook wanneer je je container een vaste naam wilt geven.

Wanneer je deze bash uitvoert zie je:
```
org.postgresql.util.PSQLException: Connection to localhost:15432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
```

Maar we draaien onze database toch op localhost:15432?

Waarom werkt dit dan niet?
Docker containers hebben een eigen geïsoleerde netwerkstack, 
waardoor de container niet rechtstreeks toegang heeft 
tot de localhost van de host machine. Ze bieden als het ware
encapsulation over hun eigen benodigde infrastructuur.
Dus: `localhost` in de container is niet hetzelfde als
de `localhost` van de host machine waar de container op draait.

We moeten tegen de Docker container zeggen dat hij niet
op zoek moet gaan naar de database op localhost, maar op
de host machine.

Dit kunnen we doen door localhost in onze configuratie
te vervangen met `host.docker.internal`. Maar we gaan natuurlijk
niet onze `application.properties` file wijzigen!

Spring Boot staat toe om instellingen te overschrijven via
environment variabelen. Dit is in lijn met de ideeën van de
[12 factor app](https://12factor.net/config)

We kunnen bij het runnen van een Docker
container deze omgevingsvariabelen meegeven met `--env` of `-e`.
De properties schrijven we in `ALL_CAPS`, waarbij we de puntjes
vervangen met underscores. Zo kunnen we `spring.datasource.url`
als volgt veranderen:

```bash
docker run -it -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:15432/cisq2-hupol hupol/basic

```

Nu start de applicatie als het goed is wel op.
Maar is hij ook bereikbaar?

Doe een call naar `GET localhost:8080/elections/0/results`.
Wat krijg je terug? Is dit een `ECONNREFUSED` (verbinding geweigerd)?

Dat komt omdat onze web applicatie draait op poort 8080
van de container. *Niet* van onze host!
Om deze poort door te geven aan de host, moeten we
onze container starten met `-p` of `--publish`. 
Hiermee kunnen we de container port publiceren
als een poort op de host: `-p <host_port>:<container_port>`.

In ons geval willen we dat 8080 van de host
verwijst naar 8080 van de container, 
het hele commando wordt dan:
```bash
docker run -it -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:15432/cisq2-hupol -p 8080:8080 hupol/basic
```

Doe een call naar `GET localhost:8080/elections/0/results`.
Als het goed is krijgen we nu een andere response code
met of zonder een body (afhankelijk van wat je in de database hebt staan).

Gelukt?
Commit en push je werk.
Denk aan een zinvolle, beschrijvende commit message.

### Stap 4. Deployment

Uiteindelijk willen we een bekende cloud-aanbieder gebruiken.
AWS, GCP en Azure vragen echter om een credit card. 
Daarom maken we gebruik van een andere (gratis) service.

> Mocht je toch
> willen deployen op een andere cloud provider dan mag dat,
> de instructies zullen daar niet helemaal op aansluiten. 
> Via de HU worden er Visual Studio Azure Cloud subscriptions aangeboden, echter slechts in beperkte mate.

Voor deze opdracht maken we gebruik van Render.com,
omdat zij gratis Docker-gebaseerde hosting hebben met
GitHub-integratie en daarnaast PostgreSQL-instances aanbieden.

De gratis service is wel erg traag, zowel qua
hosting als qua build, maar het is fijn om iets
te hebben om mee te oefenen.

#### A. Deployment met Render.com
Voor Render volg de volgende stappen:

1. Maak een account op Render.com
2. Maak een database instantie (`hupol-db`) aan
(genereer database en wachtwoord automatisch, region EU)
3. Maak een nieuwe web service aan (`hupol`)
4. Koppel je GitHub repository
5. Kies de `main` branch. Het kan netter zijn om een `deploy`-branch hiervoor aan te maken, maar dit hoeft niet (hangt van je Git-strategie af).

Als het goed is zie je meteen de deployment
lopen. Onze code wordt gecloned en een Docker
container wordt voorbereid.

Helaas werkt dit nog niet meteen! Na wat scrollen
zien we: 
```
org.postgresql.util.PSQLException: Connection to localhost:15432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
```

Dit komt je vast bekend voor! Hoe moeten we dit oplossen?

We moeten Spring wijzen op de juiste
vindplaats van onze database, met de juiste username
en het juiste wachtwoord. Hiervoor moeten we
de volgende omgevingsvariabelen meegeven via Render.com 
(environment onder onze hupol webservice):

* `SPRING_DATASOURCE_URL`
* `SPRING_DATASOURCE_USERNAME`
* `SPRING_DATASOURCE_PASSWORD`

Hoe komen we erachter hoe deze moeten worden ingevuld?
Deze gegevens kunnen we halen uit onze database instance,
door naartoe te gaan op de Render.com dashboard en op
connect te klikken. We willen een interne connectie maken
(tussen twee containers op Render.com). 
Kopieer die connectie-string,
welke ongeveer de volgende vorm heeft:
```
postgres://<gebruiker>:<wachtwoord>@<host>/<database>
```

We kunnen de environment variables voor onze web
service aanpassen door daarnaartoe te gaan vanuit de Render.com dashboard.
Vul deze als volgt in aan de hand van je connectie-string:
(let op, JDBC verwacht `postgresql` en niet `postgres`)

* `SPRING_DATASOURCE_URL`: `jdbc:postgresql://<host>/<database>`
* `SPRING_DATASOURCE_USERNAME`: `<gebruiker>`
* `SPRING_DATASOURCE_PASSWORD`: `<wachtwoord>`

Test je service uit door calls te doen naar de
register en login end-points.

Nu hebben we de basis van een 
continuous deployment-setup.
Alles dat op deploy staat komt automatisch live
te staan.

> Een alternatieve manier is gebruik te maken van een Renders image registry.
>
> Dan zouden we een image builden in onze CI-omgeving
> en, deze opslaan in bijvoorbeeld GitHub's private
> image registry en *render.com* deze laten pullen.


### Stap 5. Een native container maken (extra/optioneel)

In stap 3 hebben we met de hand een container gemaakt
aan de hand van een Dockerfile en een JAR in de target
directory.

Spring Boot ondersteunt tegenwoordig ook het bouwen van
een *native image* aan de hand van GraalVM. Dit betekent
dat we niet de JVM gebruiken, maar een binary compileren
op basis van onze source code. Dit heeft als gevolg
dat onze container kleiner wordt en sneller kan worden opgestart!

De opstartsnelheid is van belang in cloud-omgevingen,
want daarin wordt opgeschaald door meer containers
(in verschillende virtual machines in een data center)
tegelijkertijd te draaien.

Laten we het eens uitproberen met Maven!

```
./mvnw -Pnative spring-boot:build-image
```

Maven compileert onze code en draait onze tests
en bouwt vervolgens een Docker container van ons
project. Met `-Pnative` zetten we de profile op native.
Laten we dit weg, dan maakt Maven standaard een
non-native variant (dus: JVM + JAR).
De `Dockerfile` die we voor stap 2 hebben gemaakt
wordt niet meer gebruikt.

De image wordt standaard genoemd aan de hand
van wat in onze pom.xml staat: `<artifactId>:<version>`,
i.e. `hupol:0.0.1-SNAPSHOT`.

We kunnen zien dat deze image wat
kleiner is dan onze eerder gemaakte `hupol/basic` image.
Bijvoorbeeld via:
```bash
docker image ls
```

Als alternatief kan je ook de Docker desktop GUI gebruiken.

Maar werkt deze nieuwe image ook?
Probeer weer het laatste commando uit
van stap 3, maar nu met deze container.
Valt je iets op aan de opstarttijd?

Gelukt? Neem een screenshot van je `docker image ls` output
of je Docker images overview van Docker desktop op in `docs/4-notes.md`.

Commit en push je werk.
Denk aan een zinvolle, beschrijvende commit message.
