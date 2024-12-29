Gutanu Tiberiu-Mihnea 321CA tema2

In clasa CommandProcessor, am utilizat mai multe tipuri de colectii pentru
a putea organiza si gestiona datele mai bine. Mai jos o sa prezint detaliat
motivul pentru care am utilizat fiecare colectie.

1. Map-ul.
Aceasta colectie este una ideala pentru a putea stoca datele sub forma de 
perechi cheie valoare, cheia fiind unica. Utilizez aceasta colectie de 3 ori.
Voi prezenta care sunt si de ce:
    - Map<String, Runway<Airplane>> runways: am acces rapid la o pista de aterizare
      sau decolare pe baza Id-ului in O(1), realizandu-se operatiile de cautare.
      Imi permite sa localize rapid o pista specifica fara a parcurge intreaga
      colectie
    - Map<String, Airplane> airplanes: similar cu runways aceasta colectie asociaza
      fiecarui avion (cheia flightId) obiectul corespunzator Airplane. Prin intermediul
      acestei colectii am acces rapid la informatiile unui avion pe baza id ului zborului.
      Cum fiecare avion are id unic, asta determina ca utilizarea unui Map sa fie una ideala.
      Il utilizez pentru a obtine detalii despre un avion specific sau pentru a-l actualiza
      in timpul unei alocari sau a unei manevre sau chiar a unui raport
    - Map<String, LocalTime> runwayAvailability: asociaza pentru fiecare pistatimpul in care
      devine disponibila. Cu ajutorul acestui map pot urmari rapid disponibilitatea unei piste
      in O(1) fara a parcurge. In contextul aplicatiei, este crucial acest map caci imi permite sa aflu momentul in care o pista va fi libera pentru a hotari daca poate fi
      acordata permisiunea spre a se realiza o manevra.
2. PriorityQueue
Aceasta colectie am folosit o spre a mentine elementele ordonate conform unui 
comparator specific, dupa prioritate(fie este urgent fie in ordinea timpului).
Asigura acces eficient la elementele cu cea mai mare prioritate si pastreaza 
ordinea dorita a avioanelor pe pista.

Colectiile utilizate ofera performanta ridicata pentru operatiile din cadrul 
aplicatiei. Totodata confera si claritate olus organizare, map-ul face codul 
mai intuitiv, oferinf o relatie clara cheie-valoare. De asemenea, avand intr-un
singur loc centralizat timpii de disponibilitate ajuta la claritate. Colectiile
ofera si flexibilitate codului.
