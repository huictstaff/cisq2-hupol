# 4. ISO25010 pijler: operations

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
Sana wil daarom dat Husky Martian Political Systems moderne cloud infrastructuur 
gebruikt, zoals Amazon Web Services (AWS), Microsoft Azure of 
Google Cloud Platform (GCP). 

Dit soort cloud platforms maakt vaak gebruik
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

Ons is gevraagd de applicatie om geschikt te maken om te draaien
in zo'n omgeving.

### Stap 1. Bestudeer het materiaal
Neem het lesmateriaal door: slides en
eventuele links. Zorg er in het bijzonder voor
dat je begrijpt wat een Docker container is
en hoe het verschilt van een Docker image.
En: zijn een virtual machine en een Docker container
hetzelfde?

### Stap 2. Een JAR container maken
De manier om van welke applicatie dan ook een
Docker image te maken is door een 
[Dockerfile](https://docs.docker.com/engine/reference/builder/)
te schrijven. Het maakt hiervoor niet uit of je 
applicatie in Java is geschreven of bijvoorbeeld
Node.js of PHP: in een Dockerfile definieer je 
wat er in de container zit en hoe deze benaderd kan
worden.

Maak in onze project root een bestand aan met
de naam `Dockerfile`.

#### A. Een basis image gebruiken

Je schrijft meestal niet de hele image zelf,
maar baseert het op een bestaande image waar
de basisbenodigdheden inzitten. Voor uitleg van Docker zelf kijk [hier](https://docs.docker.com/language/java/build-images/#create-a-dockerfile-for-java).

In je `Dockerfile` kan je het basisimage aangeven
met de [`FROM <image>:<tag>` instructie](https://docs.docker.com/engine/reference/builder/#from). 

Maar wat moet onze basisimage zijn? In ons geval
is dat een minimaal operating system met een
ingebakken Java Runtime Environment (JRE)
of een Java Development Kit (JDK).

Deze kan je vinden op een Docker Image Registry, zoals
[Docker Hub](https://hub.docker.com/). 
Ga naar de site van Docker Hub en 
zoek op "openjdk". Wij willen een distributie gebruiken
die geschikt is voor productie, zoals de images
van `eclipse-temurin`.

Nu we een image-naam hebben, moeten we ook een 
*tag* aangeven. 
Met een tag worden verschillende varianten
aangeduid van dezelfde image, vaak met een versienummer 
en ook het gewenste platform.
Deze komt achter de imagenaam te staan
na een dubbele punt: `FROM image:tag`.

Laten we kiezen voor Java `17-jdk` met als platform
een minimaal Linux operating system (`alpine`).
De tag is dan in dit geval: `17-jdk-alpine`.

Schrijf de `FROM` instructie 
op de eerste regel van onze `Dockerfile`.

#### B. De applicatie in de image opnemen

Wanneer we met Maven onze applicatie compileren
(bijv. `mvn compile`), 
komen de resultaten in een `target`
directory te staan. Voor onze Java-applicatie
is vooral de Java Archive (`.jar`) van belang.
Dit wordt door de Java Virtual Machine (JVM)
uitgelezen en omgezet in voor onze computer 
uitvoerbare code.

Wanneer we Docker onze container image laten
bouwen, willen we dit `.jar` bestand in de 
image overnemen. Dit kunnen we doen met de
[`COPY` command](https://docs.docker.com/engine/reference/builder/#copy). Deze instructie ontvangt twee
argumenten: de bron (op onze host machine) 
en de target (in de docker container).

Voeg een witregel toe en schrijf de `COPY` instructie
op de volgende regel van onze `Dockerfile`, geef de target 
de naam `app.jar`.

#### C. Het startcommando aangeven

We hebben een basisimage toegevoegd met `FROM`
en onze benodigdheden meegegeven met `COPY`.
Nu moeten we aangeven hoe de applicatie opgestart
kan worden zodra Docker de image opstart.
Dit doen we met de 
[`ENTRYPOINT` instructie](https://docs.docker.com/engine/reference/builder/#entrypoint).

Laten we eerst eens kijken hoe we *zonder Docker*
onze applicatie kunnen starten met de commandline:

1. Zorg dat we de database kunnen (`docker-compose up`) 
2. Compileer de code (`mvn compile` of `./mvnw compile`)
3. Run de jar (`java -jar .\target\hupol-0.0.1-SNAPSHOT.jar`) 

Als het goed is, start de applicatie nu op. 
Je kan de applicatie weer stoppen met `CTRL + C`.

Laten we nu hetzelfde doen in onze `ENTRYPOINT`.
Een entrypoint schrijf je meestal in array-notatie
(de *exec form*) in plaats van een regel text.
`ENTRYPOINT [command, parameter1, parameter2, ...]`

Schrijf eerst weer een witregel 
en voeg dan een `ENTRYPOINT` aan de Dockerfile toe.

#### D. Bouw de image

We kunnen nu een image bouwen door `docker build`
in de commandline te gebruiken. We moeten wel specificeren
waar de Dockerfile gevonden kan worden. Ook kunnen we aangeven
hoe de image moet heten. Laten we `hupol/basic` aanhouden.
```bash
docker build -t hupol/basic .
```

Het puntje (`.`) geeft aan dat de Dockerfile in de huidige directory
is te vinden, terwijl je met `-t` de tag aangeven. 

Dit duurt even, omdat Docker de onderdelen van de benodigde base
image moet binnenhalen en vervolgens de instructies in de Dockerfile
uitvoert.

Met `docker image ls` kan je kijken welke docker 
images je allemaal op je computer hebt staan.
Dit zijn er misschien meer dan je denkt!

#### E. Een container configureren en draaien

Laten we een container opstarten van de image die we 
net hebben aangemaakt:
```bash
docker run -it hupol/basic
```

Wanneer je dit uitvoert, zul je zien dat er een error te zien is.
Na een boel scrollen, zie je:
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
[12 factor app](https://12factor.net/config)! 

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

### Stap 3. Deployment

Uiteindelijk willen we een bekende cloud-aanbieder gebruiken.
AWS, GCP en Azure vragen echter om een credit card. 
Daarom maken we gebruik van een andere (gratis) service.

> Mocht je toch
> willen deployen op een andere cloud provider dan mag dat,
> de instructies zullen daar niet helemaal op aansluiten.

Voor deze opdracht maken we gebruik van Render.com,
omdat zij gratis Docker-gebaseerde hosting hebben met
GitHub-integratie en daarnaast PostgreSQL-instances aanbieden.
Voorheen konden we gebruik maken van Heroku, maar
die bieden geen gratis service meer.

De gratis service is wel erg traag, zowel qua
hosting als qua build, maar het is fijn om iets
te hebben om mee te oefenen.

#### A. Beperkingen Render.com

In een ideale wereld zouden we een CI-pipeline hebben 
die onze code test en static analysis uitvoert
en, zodra alles slaagt, een docker image produceert
en opslaat in een registry. Ons hosting platform zou
dan slechts integreren met die registry 
en een image pullen wanneer een deployment gewenst is.

Helaas is dit [nog niet mogelijk op *render.com*](https://feedback.render.com/features/p/deploy-docker-images-from-public-private-registries).
Ze bouwen steeds zelf een image op basis 
van de Dockerfile die we meeleveren.

Om die reden kunnen we werken met een deploy-branch waar
we alleen onze JAR in zetten. We laten render.com luisteren
naar deze branch zodat ze, bij veranderingen, een docker
image en container bouwen en deployen op hun platform.

#### B. Deployment pipeline met GitHub Actions

Na een succesvolle build op main (met static analysis en tests)
kunnen we ervoor zorgen dat
de resulterende JAR en de Dockerfile worden 
gecommit op de *deploy* branch. 
Render.com ziet dan dat er iets is geupdatet 
en bouwt en deployt de container.

Zorg dat onze GitHub Actions na static analysis en
tests de JAR en de Dockerfile commit op een branch genaamd
`deploy`. Je mag hier een community action voor gebruiken,
maar je kan ook het volgende doen.

We hebben een Personal Access Token nodig om te pushen naar
een branch binnen onze GitHub Actions pipeline. Dit is vanwege
security-redenen. Dat kunnen we als volgt doen:
1. Ga naar de instellingen van je GitHub-account 
en klik op "Developer-settings" -> "Personal Access Tokens" -> "Generate New Token".
2. Zorg voor een lange geldigheid (liever niet oneindig i.v.m. security)
3. Geef je token een naam en selecteer de benodigde toestemmingen (read + write voor Content).
4. Klik op "Generate Token" en kopieer het token 
naar je klembord.
5. Ga naar de instellingen van je CISQ2-repository en 
klik op "Secrets and Variables" -> "Actions"
6. Maak een nieuwe secret aan: "New repository secret"
7. Geef je secret een naam (bijvoorbeeld ACCESS_TOKEN) en 
plak het gekopieerde token in het "Waarde"-veld.
9. Klik op "Add secret"

Nu kunnen we de secret gebruiken binnen onze pipeline.
Wijzig je GitHub Actions pipeline. Als je er nog 
geen een hebt, maak er dan één voor een Java/Maven 
project.

In de pipeline willen we een stap toevoegen na alle
tests en checks om alle JARs uit target mee te committen
naar een nieuwe branch 
(aangepast van [StackOverflow]()):
```yml
    - name: Push to deploy branch
      run: |
          git config --global user.name 'GitHub Action'
          git config --global user.email 'action@github.com'
          git fetch
          git checkout -b deploy || true
          git add --force target/*.jar
          git commit -am "Automated: prepare deployment" 
          git push --force --set-upstream origin deploy
```

We moeten er wel voor zorgen dat onze eerdere `actions/checkout`
stap gebruik maakt van de Personal Access Token die we eerder hebben
gegenereerd en via secrets meegeven. 
Vervang deze eerste stap met de volgende:
```yml
    - uses: actions/checkout@v3
      with:
        token: ${{ secrets.ACCESS_TOKEN }}
```

Als dit is gelukt moeten we een groen vinkje bij onze
GitHub Action zien en een nieuwe branch "deploy" op
onze GitHub Repository. We hebben nu een continuous
delivery-achtige setup waarbij alles wat op deploy
staat *klaar* is voor deployment.

#### C. Deployment met Render.com

We hebben een deployment branch gemaakt. Nu kunnen
we met ons hosting platform aan de slag.
Volg de volgende stappen:

1. Maak een account op Render.com
2. Maak een database instantie (`hupol-db`) aan
(genereer database en wachtwoord automatisch, region EU)
3. Maak een nieuwe service aan (`hupol`)
4. Koppel je GitHub repository
5. Kies de `deploy` branch

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

### Stap 4. Een native container maken (extra/optioneel)

In stap 2 hebben we met de hand een container gemaakt
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
van stap 2, maar nu met deze container.
Valt je iets op aan de opstarttijd?

Gelukt? Neem een screenshot van je `docker image ls` output
of je Docker images overview van Docker desktop op in `docs/4-notes.md`.

> Het zou mooi zijn als we deze native image ook konden
> deployen. Helaas is het [nog niet mogelijk op *render.com*](https://feedback.render.com/features/p/deploy-docker-images-from-public-private-registries)
> gebruik te maken van een image registry.
> 
> Dan zouden we een image builden in onze CI-omgeving
> en, deze opslaan in bijvoorbeeld GitHub's private
> image registry en *render.com* deze laten pullen.

Commit en push je werk.
Denk aan een zinvolle, beschrijvende commit message.
