# Tema POO - GwentStone
### Readme - Oprisan Alexia Ioana

### Descrierea temei
Codul implementeaza jocul de carti **GwentStone Lite**, care poate 
fi jucat de 2 jucatori. Jocul se desfasoara pe o tabla de joc, 
fiecare jucator avand un deck de carti, din care poate lua cate o 
carte la fiecare runda pentru a o tine in mana. Jucatorii pot plasa
cartile pe tabla de joc, pot ataca cu ele, pot folosi abilitatile 
cartilor si ale eroilor.

### Implementare
Implementarea jocului incepe din **main**, unde este creata 
clasa **GameGwentStone**, iar metoda **startPlayingTheGame** 
declanseaza desfasurarea jocului. Fiecare sesiune de joc (game) 
este generata din **Input** si include doi jucatori, **playerOne** 
si **playerTwo**, care concureaza pana la final.

#### Clasa Player
**playerOne** si **playerTwo** sunt instante ale clasei **Player**, 
care contine:

- `private int mana;`  
  Reprezinta cantitatea de mana disponibila jucatorului intr-o 
- runda. Mana este utilizata
  pentru a plasa carti pe tabla de joc.

- `private final ArrayList<Card> cardsInHand = new ArrayList<>();`  
  Lista care contine cartile pe care jucatorul le are in mana.

- `private final Deck cardsInDeck = new Deck();`  
  Obiect de tip **Deck**, care reprezinta pachetul de carti 
al jucatorului.

- `private Hero hero;`  
  Obiectul de tip **Hero**, eroul jucatorului.

- `private boolean isActive;`  
  O variabila booleana care indica daca jucatorul este activ 
in momentul respectiv al jocului.

Pentru a incepe un joc, datele sunt preluate din clasele de 
**Input** si atribuite variabilelor corespunzatoare fiecarui 
jucator, acest proces avand loc in metoda **processDataForEachPlayer**. 
Cartile din deck-ul jucatorului sunt stocate utilizand clasa **Card**, 
care descrie fiecare carte detinuta de jucator.

#### Clasa Card
Clasa **Card** reprezinta o carte din joc si contine atribute 
precum mana, daunele de atac, viata, descrierea, culorile asociate,
numele si statutul de "frozen". Aceasta clasa va fi extinsa pentru 
a include tipuri de carti precum **Hero** si **Minion**.

Pentru a incepe prima runda, fiecare jucator extrage o carte din
deck si efectueaza diverse actiuni. Prin utilizarea unei 
instructiuni **switch**, se verifica numele comenzii si se 
apeleaza metodele corespunzatoare, in functie de aceasta.  
La sfarsitul unei runde, se actualizeaza mana jucatorilor, se 
schimba turele acestora si se pregateste tabla de joc pentru 
urmatoarea runda. Daca ambii jucatori au terminat tura, mana fiecarui
jucator creste, iar jucatorii extrag cate o carte din deck-ul lor. 
Apoi, se initiaza o noua runda pe tabla de joc. Minionii de pe 
tabla sunt decongelati, iar turele jucatorilor se actualizeaza,
pregatindu-se pentru urmatorul set de actiuni.

#### Clasa GameTable
Clasa **GameTable** reprezinta tabla de joc, unde sunt plasati 
minionii in timpul jocului. Aceasta include metode pentru 
adaugarea, eliminarea si recuperarea minionilor de pe tabla.
Tabla este reprezentata printr-o lista bidimensionala, cu 
un numar maxim de 4 randuri si 5 coloane.

Pentru a plasa un minion pe tabla, jucatorul trebuie sa extraga 
o carte din mana si sa o aseze pe tabla de joc. In cazul 
in care jucatorul nu dispune de suficienta mana pentru a 
plasa cartea, va aparea un mesaj de eroare.

#### Atacul unei carti
Atunci cand o carte de tip minion se afla pe tabla, 
jucatorul are posibilitatea de a ataca o alta carte de tip 
minion sau eroul adversar. Atacul se realizeaza prin apelarea
metodei **cardUsesAttack**, care verifica initial toate 
conditiile necesare pentru a efectua atacul. Pentru a putea 
mai usor erorile s-a folosit clasa **actionsErrors**. In cazul 
in care aceste conditii sunt indeplinite, se scad punctele de
viata ale cartii atacate, iar sistemul verifica daca aceasta 
a fost eliminata de pe tabla. In situatia in care tinta atacului
este un erou, se scad punctele de viata ale acestuia, iar
sistemul verifica daca eroul a fost eliminat din joc.

#### Abilitatile cartilor
Cartile pot avea diverse abilitati, care pot fi folosite in 
timpul unei runde. Acestea sunt implementate in metoda 
**useCardAbility**, care face de asemenea, verificari pentru
a asigura ca abilitatea este folosita corect. Minionii 
cu abilitati speciale sunt deja stabiliti din regulile jocului, 
astfel verificand dupa numele cartii ce abilitate va fi folosita.
Atat abilitatile cartilor minioni, cat si cele ale eroilor 
sunt implementate in metodele din clasa **Abilities**. De 
asemenea, eroul poate folosi abilitatea sa speciala, o 
singura data pe joc.

#### Sfarsitul unui joc
In cazul in care un eroul al unui jucator ramane fara viata, 
jocul se incheie. Se poate incepe un alt joc, fiind nevoie sa
se reseteze atributurile jucatorilor si tabla de joc.

#### Comenzi de debug
In timpul jocului, se pot folosi comenzi de debug pentru a
verifica implementarea jocului.

- **aflarea cartilor din mana unui jucator** - **getCardsInHand**: 
se obtin cartile din mana unui jucator specificat, se 
adauga intr-un obiect JSON si se returneaza.

- **aflarea cartilor de pe tabla de joc** - **getCardsOnTable**: 
se obtin cartile de pe masa de joc si se adauga intr-un obiect JSON.

- **aflarea cartilor din deck-ul unui jucator** - 
**getCardsInDeck**: se obtine pachetul de carti al unui 
jucator (in functie de **playerIdx**), se creeaza un obiect
JSON pentru comanda **"getPlayerDeck"** si se adauga 
informatiile despre fiecare carte din pachet.

- **afisarea jucatorului activ** - **methodGetPlayerTurn**: se 
returneaza jucatorul activ.

- **afisarea eroului unui jucator** - **methodGetPlayerHero**:
se returneaza eroul unui jucator, folosind metoda 
**printHeroInJson** din clasa **Hero**.

- **afisarea unei carti de la o pozitie specifica** - 
**getCardAtPosition**: se recupereaza cartea de la o 
anumita pozitie de pe tabla de joc si se afiseaza in format JSON.

- **afisarea manei unui jucator** - **methodGetPlayerMana**: 
se returneaza mana unui jucator.

- **afisarea cartilor de pe tabla de joc care sunt frozen** - 
**getFrozenCardsOnTable**: se verifica care carti de pe tabla
de joc sunt congelate si se returneaza informatiile despre acestea.

### Comenzi pentru statistici

- **afisarea numarului de jocuri jucate** - 
**getTotalGamesPlayed**: se returneaza numarul 
total de jocuri jucate, ce a fost incrementat la 
inceputul fiecarui joc.

- **afisarea numarului de jocuri castigate de un 
jucator** - **getPlayerOneWins** sau **getPlayerTwoWins**:
se returneaza numarul de jocuri castigate de un jucator, 
ce a fost incrementat la finalul fiecarui joc castigat.  
