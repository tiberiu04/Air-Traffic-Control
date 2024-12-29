*** Gutanu Tiberiu-Mihnea 321CA tema2 ***

As propune adaugarea unei functionaliti suplimentare pentru gestionarea
nivelului de combustibil al avioanelor. Aceatsa functionalitate va permite 
urmarirea consumului de combustibil si prioritizarea avioanelor cu niveluri
critice de combustibil pentru manevrele de aterizare. Pe langa acest aspect,
se poate genera un raport al nivelului de combustibil pentru toate avioanele.

Pentru implementarea acestei functionalitati, propun urmatoarele aspecte:
1. O clasa noua *** FuelManager ***
Aceasta clasa se va ocupa de gestionarea nivelului de combustibil al avioanelor
si va prioritiza manevrele pentru avioanele cu cel mai scazut nivel de combustibil

M-as gandi ca aceasta clasa sa aiba ca atribute un MAP care stocheaza perechea id-
nivel combustibil pentru eficienta. Ca metode as propune sa se realizeze 3 principale
care: actualizeaza nivelul de combustibil al unui avion, returneaza nivelul de combustibil
specific avionului respectiv si returneaza o lista de avioane ce au un nivel de combustibil
sub un prag dat, citit din fisier.

2. In CommandProcessor as adauga ***doua metode*** aditionale pentru combustibil si 
anume de: updateFuel(prin care se actualizeaza nivelul de combustibil al 
unui avion pe baza unei comenzi primite) si generateFuelReport(care genereaza
rapoartele de nivel de combustibil pentru toate avioanele).

Prin aceasta noua functionalitate pentru aplicatie ma asigur ca avioanele pot
sa fie mai sigure, permit urmarirea si actualizarea continua a nivelului de
combustibil si cu ajutorul rapoartelor pot identifica probleme operationale.
