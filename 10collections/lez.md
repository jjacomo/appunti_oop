# INTRO

Java Collections Framework.
Iterators, for each, collezioni, liste, set, hashset, treeset.

`Iterable` <-(estesa da) `Collection`     <-    (estesa da)    `List`, `Queue`, `Set` 
   ^                     ^                                         ^
   |                     |                                         |
interfaccia          interfaccia                ancora interfacce (sempre piu' definite)
                         ^                                         ^
                         |                                         |
                aggiunge operazioni come la                        |
                rimozione e aggiunta di cose                       |
                                                                   |
                                            Ad esempio list consente di accedere per indice e ha un ordine;
                                     Set invece non ha ordine, queue invece ha solo add (dal fondo) e remove (alla fine)
                                                                   ^
                                                                   |
                                                                   |
                                Sono implementate dalle classi `ArrayList`/`LinkedList` per List, `HashSet` e `TreeSet` per i Set,
                                        E poi ci sono le `Map`s (interface) che saranno implementate da `HashMap`...






