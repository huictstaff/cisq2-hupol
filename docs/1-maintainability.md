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

### Stap 1. Bestudeer de opdracht en maak een klassendiagram
Bestudeer de opdracht in `0-background`. Teken voor jezelf uit in een klassendiagram hoe het softwaresysteem zou moeten werken, zónder naar de code gekeken te hebben.
Hoe zit de structuur in elkaar volgens de *intended architecture*?

Bekijk de code van
de `votes`, `results` en `candidates` components. Draai eerst mvn verify en bekijk de docker-compose.yml en de verschillende .properties-bestanden
voor de instellingen)

### Stap 2. Noteer wat er beter kan
Maak een nette indeling met kopjes in een markdowndocument en schrijf puntsgewijs op wat er volgens jou beter kan
wat betreft de code van de `votes`, `results` en `candidates` components.

Denk hierbij bijvoorbeeld aan *code smells* en hoe de *implemented architecture*
verschilt van de *intended architecture*. Je kan ook kijken naar wat je
bij andere cursussen hebt geleerd over een onderhoudbare structuur
([separation of concerns](https://www.arothuis.nl/posts/separation-of-concerns/), 
[loose coupling](https://en.wikipedia.org/wiki/Loose_coupling), 
[high cohesion](https://en.wikipedia.org/wiki/Cohesion_(computer_science)), 
[data normaliseren en normaalvormen](https://www.splunk.com/en_us/blog/learn/data-normalization.html)).

Dat kan je doen in een nieuw markdown-bestand
(bijvoorbeeld onder `docs/1-notes.md`). Vermeld gebruikte bronnen! Plagieer niet van generatieve AI, internetbronnen of je medestudenten.
Wees grondig in je analyse en denk er niet te makkelijk over! Bekijk de code kritisch en analyseer grondig wat er beter kan. 
Teken eventueel een domeinmodel uit voor jezelf hoe je deze applicatie zelf zou ontwerpen.

Commit en push je werk.
Denk aan een zinvolle, beschrijvende commit message.
> ❗ Pas op met een generatieve AI (ChatGPT, Bing, etc.), codekwaliteit wordt daar vaker slechter van

### Stap 3. Voeg een build pipeline toe

Gebruik hiervoor
[GitHub Actions](https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-maven). Haak PMD (zie hieronder) in op de maven lifecycle in de build. Gebruik maven verify en skip geen tests.

### Stap 4. Schoon de code op en refactor de architectuur
1. Refactor de architectuur. Bedenk welke verantwoordelijkheden in welke klassen of laag thuishoren.
   Je mag nieuwe klassen introduceren en de code vriendelijker voor
   gebruikers en developers maken. Reduceer waar nodig (en logisch).
   
2. Waar liggen de verantwoordelijkheden van packages, hoe praten deze met elkaar?
   Wie beheert zijn informatie en hoe komt een pacakage aan informatie die diegene nodig heeft?
   Bedenk welke (nieuwe) klassen er nodig zijn. Je mag de gehele applicatie aanpassen.
2. Refactor de tests zodat deze weer slagen. Let erop dat alle tests uiteindelijk weer slagen! (dus comment geen tests uit)
3. Neem de [PMD](https://docs.pmd-code.org/latest/) plugin op in je project en de `pom-complexity-analysis.xml` in je root.
Draai de plugin en bekijk het rapport. 
4. Schoon vervolgens de code op van bestaande klassen binnen `votes`, `results` en `candidates`.
Kijk ook naar de testcode. Richt je hierbij op clean en declarative code, 
bijvoorbeeld door gebruik te maken van Java-features 
als [switch expressions](https://docs.oracle.com/en/java/javase/13/language/switch-expressions.html),
[functional interfaces](https://www.baeldung.com/java-8-functional-interfaces) 
en de [Stream API](https://stackify.com/streams-guide-java-8/). 

6. Hoe sla je je gegevens op? Doe je dat volgens een bepaalde normaalvorm?
7. Beschrijf wat je hebt gedaan. Hoe is de accidental complexity verminderd? Laat je code zien waarin je dit hebt verbeterd.
   Je mag aanvullend  hulp inschakelen die je kunt gebruiken:
   je IDE, of stackoverflow. Schrijf wel op wat je hebt gedaan (in `notes-1.md`)  
   en wees kritisch op de uitkomst. Vermeld gebruikte bronnen!

> ❗ Je hoeft alleen de `votes`, `results` en `candidates` components
te verbeteren (je zou voor de andere components 
> *excluded* of *ignore* regels kunnen opnemen).
> 
> Je *mag* natuurlijk ook de andere components opschonen,
> maar doe dit in aparte branches/commits.
 
Commit en push je werk. 
Denk aan een zinvolle, beschrijvende commit message.

### Stap 5. Archunit
Onderzoek de architecturele regels van het hele project
(welke modules zijn er en hoe mogen ze met elkaar praten). Zie voor de minimale eisen de `background.md`.
Test en verbeter het project met behulp van 
[ArchUnit](https://www.archunit.org/getting-started). Zie voor documentatie
[use cases](https://www.archunit.org/use-cases),
[docs](https://www.archunit.org/userguide/html/000_Index.html),
en een [uitgebreide video-tutorial](https://www.youtube.com/watch?v=_ZUtb_hsm4Q).

Zorg hier dat de domeinregels worden toegepast, bijvoorbeeld het dependency inversion principle. Zie slides voor voorbeelden.

Commit en push je werk. Zorg dat je pipeline nog draait

### Stap 6. Refactoring voor een XML-parser toevoegen
Refactor de structuur van de `votes`, `results` en `candidates` components 
zodat het mogelijk wordt om naast de CSV-parser een XML-parser te gebruiken.
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