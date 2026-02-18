**Úloha:** Napíš unit testy (JUnit 5) a integračné testy pre všetok necommitnutý kód v tomto repozitári.

### Postup, ktorý máš dodržať
1. Zisti všetky zmenené súbory a zmenené časti kódu:
    - `git diff`
    - `git diff --cached`
    - z toho zostav zoznam tried/metód/logiky, ktoré sa zmenili alebo pribudli.
2. Pre každú zmenenú/pridanú produkčnú triedu dopíš:
    - **Unit testy** (rýchle, bez reálnych I/O; mockuj závislosti).
    - **Integračné testy** (overujú spoluprácu vrstiev / konfiguráciu / wiring; používaj reálny Spring context iba tam, kde dáva zmysel).
3. Deteguj technológie projektu automaticky (napr. Maven/Gradle, Spring Boot, REST, DB, MapStruct, Lombok…).
    - Ak ide o Spring Boot REST API: preferuj `@SpringBootTest` + `@AutoConfigureMockMvc` (alebo `@WebMvcTest` podľa potreby).
    - Ak je v projekte DB (napr. PostgreSQL) a existujú integračné testy: použi rovnaký prístup ako projekt (napr. Testcontainers, H2, alebo lokálne embedded riešenie). **Nemeň existujúcu stratégiu**, iba ju rozšír.
4. Testy píš tak, aby:
    - boli deterministické, bez flaky správania,
    - mali zmysluplné názvy (Given/When/Then alebo Arrange/Act/Assert),
    - pokryli hraničné prípady, validácie a chybové stavy,
    - nevyžadovali prístup na internet.
5. Nezasahuj do produkčného kódu, **iba ak je to nevyhnutné** na testovateľnosť (napr. malé sprístupnenie package-private, doplnenie konštruktora, odstránenie hard-coded času cez `Clock`).
    - Každú zmenu produkčného kódu minimalizuj a zdôvodni v krátkom komentári v PR-style.
6. Umiestnenie testov:
    - Unit testy: `src/test/java/...` s konvenciou `*Test`.
    - Integračné testy: ak projekt používa Failsafe, tak `*IT`; inak sa riaď existujúcimi konvenciami v repozitári.
7. Po dopísaní testov:
    - spusti lokálne testy (podľa build toolu):
        - Maven: `mvn -q test` a ak existuje Failsafe: `mvn -q verify`
        - Gradle: `./gradlew test`
    - oprav všetky chyby tak, aby build prešiel.
8. Na záver urob krátky report:
    - aké súbory boli zmenené (produkčný kód) a aké nové testy pribudli,
    - čo pokrývajú integračné testy (hlavne endpointy / DB / service flow),
    - aké príkazy si spustil a že prešli.

### Dôležité pravidlá
- Nepíš “testy na testy” ani nerelevantné snapshot testy.
- Nepoužívaj reálne externé služby.
- Ak niečo nevieš o zámere funkcionality z diffu, urob rozumný predpoklad z existujúcich patternov v projekte.

**Začni teraz tým, že zanalyzuješ `git diff` a `git diff --cached` a potom vytvoríš/aktualizuješ testy.**