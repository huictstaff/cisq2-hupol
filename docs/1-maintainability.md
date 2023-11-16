# Opdracht 1: ISO25010 pijler: maintainability

> ![Elron Husky, a husky dog in a suit](img/elron-husky.png)
>
> __Elron Husky, CEO of *Husky Martian Political Systems*:__
> 
> "At Hupol, we only work with the *best* engineers! They
> know how to design a piece of software,
> built to last a lifetime! That's why when
> *we* talk about legacy code, we talk about something
> we're proud of. The cleanest code and the strongest structure. 
> A legacy that anyone would *love* to inherit!
> Wouldn't you?"

## De opdracht
Leuk verhaal van Elron, maar is het allemaal wel zo
mooi als we worden voorgespiegeld? Of is het toch gewoon
lastig te lezen [legacy code](https://understandlegacycode.com/blog/what-is-legacy-code-is-it-code-without-tests/)?

Jimothy, de lead developer van Husky Martian Political Systems,
heeft ons om een *maintainability-onderzoek* gevraagd.

Ons is verzocht kritisch naar 
de `votes`, `results` en `candidates` components te kijken, 
eventuele *code smells* te verwijderen 
en de architectuur waar nodig te herstellen.
Hierbij maken we gebruik van *static analysis tools*,
*refactoring* en *architecture testing*.

### Stap 1. Bestudeer de opdracht en de code, draai de tests
Neem de opdracht door. Wat zijn de ambities van het bedrijf?
Wat zou er allemaal in het project moeten zitten? Wat weet je allemaal al van ISO25010's pijler maintainability?
Welke technieken zijn er gebruikt?
Wat zijn de verantwoordelijkheden van de verschillende components?
Hoe zit de structuur in elkaar volgens de *intended architecture*?

Bekijk daarna de code van
de `votes`, `results` en `candidates` components. Let ook op
de tests. Kan je de tests draaien en het project 
werkend krijgen? Vergeet niet `docker-compose up` te draaien
(bekijk de docker-compose.yml en de verschillende .properties-bestanden
voor de instellingen)

### Stap 2. Noteer wat er beter kan
Maak een nette indeling met kopjes in een markdowndocument en schrijf puntsgewijs op wat er volgens jou beter kan
wat betreft de code van de `votes` en `candidates` components.

Denk hierbij bijvoorbeeld aan *code smells* en hoe de *implemented architecture*
verschilt van de *intended architecture*. Je kan ook kijken naar wat je
bij andere cursussen hebt geleerd over een onderhoudbare structuur
([separation of concerns](https://www.arothuis.nl/posts/separation-of-concerns/), 
[loose coupling](https://en.wikipedia.org/wiki/Loose_coupling), 
[high cohesion](https://en.wikipedia.org/wiki/Cohesion_(computer_science))).

Dat kan je doen in een nieuw markdown-bestand
(bijvoorbeeld onder `docs/1-notes.md`). Vermeld gebruikte bronnen! Plagieer niet van generatieve AI, internetbronnen of je medestudenten.
Wees grondig in je analyse en denk er niet te makkelijk over! Bekijk de code kritisch en analyseer grondig wat er beter kan. 

Commit en push je werk. 
Denk aan een zinvolle, beschrijvende commit message.

### Stap 3. Schoon de code op (deel 1)

De lead developer van het team, Jimothy, tikt ons op de schouder:

> Psst! Die code... daar zit een luchtje aan! Het is in alle haast
> geschreven tijdens crunch time. 
> We hebben [technical debt](https://en.wikipedia.org/wiki/Technical_debt) opgebouwd,
> waardoor we minder makkelijk features kunnen opleveren.
> Het was beter als dat niet was gebeurd... Maar we hebben nu tijd (en hulp)
> om het te verbeteren.
> 
> Weg met de [code smells](https://blog.codinghorror.com/code-smells/)! 
> We willen [clean code](https://www.pluralsight.com/blog/software-development/10-steps-to-clean-code)!
> 
> Kan jij ons helpen?

Maak voor deze opdracht gebruik van tenminste *1 static analysis tool*,
bijvoorbeeld:

* [PMD](https://docs.pmd-code.org/latest/)
* [Checkstyle](https://checkstyle.org/)
* [SonarQube](https://docs.sonarqube.org/latest/analyzing-source-code/languages/java/)
* [Error Prone](https://errorprone.info/) 
* [Infer](https://fbinfer.com/) 

Kies een of meer tools, kies een passende codestyle 
(bijvoorbeeld die van Google), lees de documentatie(!) en
neem de tool op in het project via Maven (zoek naar een Maven plugin).

Schoon vervolgens de code op van bestaande klassen binnen `votes`, `results` en `candidates`.
Kijk ook naar de test code.
Richt je hierbij op clean en declarative code, 
bijvoorbeeld door gebruik te maken van Java-features 
als [switch expressions](https://docs.oracle.com/en/java/javase/13/language/switch-expressions.html),
[functional interfaces](https://www.baeldung.com/java-8-functional-interfaces) 
en de [Stream API](https://stackify.com/streams-guide-java-8/). 
Bedenk welke verantwoordelijkheden in welke klassen of laag thuishoren.
Je mag nieuwe klassen introduceren en de code vriendelijker voor
gebruikers en developers maken, maar doe nog geen grotere wijzigingen
(dat doen we in stap 5 en 6). Natuurlijk mag je wel tests toevoegen!

Je mag aanvullend alle hulp inschakelen die je kunt gebruiken:
je IDE, een generatieve AI (ChatGPT, Bing, etc.) of welke andere truc dan ook.
Schrijf wel op wat je hebt gedaan (in `notes-1.md`)  
en wees kritisch op de uitkomst. Vermeld gebruikte bronnen!

> ❗ Je hoeft alleen de `votes`, `results` en `candidates` components
te verbeteren (je zou voor de andere components 
> *excluded* of *ignore* regels kunnen opnemen).
> 
> Je *mag* natuurlijk ook de andere components opschonen,
> maar doe dit in aparte branches/commits.
 
Commit en push je werk. 
Denk aan een zinvolle, beschrijvende commit message.

### Stap 4. Voeg een build pipeline toe

Nadat je een tool hebt gekozen bij stap 3 en deze lokaal hebt
laten werken, willen we de boel onderbrengen in een continuous integration
pipeline. Gebruik hiervoor 
[GitHub Actions](https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-maven).

Commit en push je werk.
Denk aan een zinvolle, beschrijvende commit message.
Waarschijnlijk moet je dit een aantal keer herhalen
om het werkend te krijgen in GitHub Actions.

### Stap 5. Code opschonen deel 2: test en herstel de architectuur (

Sana, de architect voor Hupol, ziet wat je allemaal gedaan hebt.
Ze is erg onder de indruk! Ze vraagt zich af: 
"Kan je die components ook wat meer laten voldoen aan de 
voorgeschreven architectuur? Is zoiets trouwens ook niet te automatiseren?"
Je reageert enthousiast: "Dat moet zeker kunnen!"

Onderzoek de architecturele regels van het hele project
(welke modules zijn er en hoe mogen ze met elkaar praten)
en test en verbeter deze met behulp van 
[ArchUnit](https://www.archunit.org/getting-started).

Maak nieuwe packages en klassen waar nodig (en logisch). Waar liggen de verantwoordelijkheden van packages, hoe praten deze met elkaar? 
Wie beheert zijn informatie en hoe komt een pacakage aan informatie die diegene nodig heeft? 
Bedenk welke (nieuwe) klassen er nodig zijn en teken eventueel een domeinmodel uit voor jezelf.
Je mag het hele project inclusief alle tests aanpassen, maar let erop dat alle tests uiteindelijk weer slagen! (dus comment geen tests uit)

Zie ook
[use cases](https://www.archunit.org/use-cases),
[docs](https://www.archunit.org/userguide/html/000_Index.html),
en een [uitgebreide video-tutorial](https://www.youtube.com/watch?v=_ZUtb_hsm4Q).

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.

### Stap 6. Refactor de structuur
Rufus, product owner, geeft tijdens een Scrum backlog-sessie aan 
dat we binnenkort ook XML-bestanden moeten ondersteunen
om kandidaten en stemmen in te voeren. Mogelijk dat er in de toekomst 
zelfs nog meer andere file formats nodig zijn voor de importeer-functionaliteit.
Dat gaat nu helaas niet zo makkelijk! Eventuele performance-problemen 
mag je voor nu negeren.

Kan jij hier een oplossing voor verzinnen?

Refactor de structuur van de `votes`, `results` en `candidates` components 
zodat dit mogelijk wordt. Kijk ook naar de tests. Misschien zouden we nog wel meer
moeten refactoren, maar we beginnen klein, denk aan de
[Strangler Fig pattern](https://martinfowler.com/bliki/StranglerFigApplication.html).

Je hoeft geen XML-parser te implementeren, maar pas de bestaande code 
aan zodat het CSV-parsing-gedeelte gemakkelijk kan worden uitgewisseld
voor een XML-parsing-gedeelte. Maak het zo, dat uiteindelijk alleen de interface voor de XML-parser geimplementeerd hoeft te worden.

Bekijk welke verantwoordelijkheden hier nodig zijn en hoe je deze
over verschillende objecten kunt verdelen met handige 
[abstracties](https://www.arothuis.nl/posts/the-object-model/#1-abstraction)
die polymorfisme benutten, 
zoals [interfaces](https://dzone.com/articles/learning-java-what-vs-why) en 
[generics](https://www.baeldung.com/java-generics).
Denk natuurlijk ook aan design principles 
([SOLID](https://www.freecodecamp.org/news/solid-principles-explained-in-plain-english/),
[DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself), 
[ICE](https://en.wikibooks.org/wiki/A-level_Computing/AQA/Paper_1/Fundamentals_of_programming/Design_Principles_in_Object-Oriented_Programming))
en probeer een [structural design pattern](https://refactoring.guru/design-patterns/structural-patterns) in te zetten.
Maak gebruik van [refactoring-technieken](https://refactoring.guru/refactoring) 
van je [IDE](https://blog.jetbrains.com/idea/2020/12/3-ways-to-refactor-your-code-in-intellij-idea/).

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?
Denk aan een zinvolle, beschrijvende commit message.

### Stap 7. Reflecteer (extra/optioneel)
De opdracht is vervuld. Het development team vraagt 
om jouw expertise en mening over de onderhoudbaarheid:
heb je suggesties hoe het *hele systeem* mooier kan op de lange termijn?

Wat vind je van de algehele opzet van het project,
wanneer je kijkt naar hoe de use cases over components zijn verdeeld
en hoe het gebruikt wordt?
Zou je bijvoorbeeld components willen verwijderen, samenvoegen of toevoegen?

1. Maak een schets van je voorbeeld waarin structuur 
en verdeling van functionaliteit duidelijk worden, bijvoorbeeld aan de hand van een (versimpeld)
[use case diagram](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-use-case-diagram/),
[package diagram](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-package-diagram/) en
[component diagram](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-component-diagram/)
2. Voeg je tekeningen toe aan je docs
3. (Optioneel) Realiseer je voorgestelde wijzigingen

Commit en push je werk. Draait dit ook in de pipeline? Slaagt alles nog?