# INTRO

Oop1.0 + Functional Programming = Oop2.0;

Oggi lamda (programmazione funzionale).
Venerdi' faremo gli stream (no input\outputStream. 

## lamda expressions

Release del 2014.
Cambia molto come programmi.
Dervia dal termine dal lamda calcolo (1930) di Alonzo Charch (matematico).
Assieme ad Alan Turing.

Le classi anonime ci permettono di fare chiamate a metodi 
Le lamda sono ritradotte in classi anonime!
Modo succinto per esprimere un oggetto che ha un solo metodo.
In realta' sono piu' efficienti della loro controparte con le classi anonime!!!
Hanno introdotto delle istruzioni speciali della JVM per gestirle.
Infatti non viene creata una nuova classe come nel caso sopra ma diventano tipo dei metodi della classe in cui le hai fatte tipo (piu' efficiente, meno memoria sprecata...).

Lamda:
`(input1, input2) -> (input1 * 2 + input1 / 3)`

```java
public class FirstComparable {

public static void main ( String [] args ) {

        final List<Person> list = new ArrayList<Person>();
        list.add(new Person("Mario", 1960, true));
        list.add(new Person("Gino", 1970, false));
        list.add(new Person("Rino", 1951, true));
        System.out.println(list);

        // With anonymous class
        Collections.sort(list, new Comparator<Person>(){
            public int compare(Person o1, Person o2){
                return o1.getBirth() - o2.getBirth();
            }
        }) ;
        System.out.println(list);

        // With full - syntax lambda
        Collections.sort(list, (Person o1, Person o2) -> { // la sort nella sua intestazione vuole un 
            return o1.getBirth() - o2.getBirth();          // comparator, quindi questa lamda si reduce
        });                                                // in una classe anonima col metodo comparator!
        System.out.println(list);

        // With shortcut - syntax lambda
        Collections.sort(list, (o1, o2) -> o2.getBirth() - o1.getBirth()) ;
        System.out.println(list);
 }
```

altro esempio

```java
public class UseButtonEvents {

    public static void main (String[] args) {
        final JButton b1 = new JButton("Say Hello");

        // Uso una inner class anonima ..
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello!!" + e);
            }
        }) ;

        // Uso una lambda
        final JButton b2 = new JButton("Say Hello 2");
        b2.addActionListener(e -> System.out.println("Hello 2!! " + e));

        final JFrame frame = new JFrame("Events Example");
        frame.setSize(320, 200);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(b1);
        frame.getContentPane().add(b2);
        frame.setVisible(true);
    }
 }
```


Altro esempio

```java
public class FirstStream {
    public static void main ( String [] args ) {
        final List<Integer> list = List.of(10, 20, 30, 40, 50, 60, 70, 80, 90);

        // for each element , pass it to method println of System . out
        list.stream().forEach(System.out::println); // "10" \n "20" \n "30"

        // Filter and print
        list.stream().filter(i -> i>60).forEach(i -> System.out.print(i + " "));
        System.out.println(" "); // "70 80 90"

        // Map and print
        list.stream().map(i -> i + " ").forEach(System.out::print);
        System.out.println(" "); // "10 20 .. 70 80 90 "

        // Map - reduce and print the resulting string
        final String s = list.stream().map(i -> i + "|").reduce((x, y) -> x + y).get();
        System.out.println(s); // "10|20|.. |70|80|90|"
    }
 }
```


### che cos'e'
La lamda e' una funzione anonima (e' anonima).
Lo scope che hai dentro il corpo delle lamda e' solo delle var final che sono nel metodo dove la hai dichiarata.

#### sintassi piu' usata

Grazie al type inference.
* `(x1,..,xn) -> <exp>` // con n argomenti
* `() -> <exp>` // caso speciale a zero argomenti
* `x -> <exp>` // caso special a 1 argomento

#### sintassi completa

* `(T1 x1,..,Tn xn) -> {<body>}` // sintassi completa
* `(x1,..,xn) -> {<body>}` // completa non tipata
* `(T1 x1,..,Tn xn) -> <exp>`// tipata con body


### Method reference

Sintassi possibili
1. `<class>::<static-method>`
    ▶ sta per `(x1,..,xn)-> <class>.<static-method>(x1,..,xn)`
2. `<obj>::<method>`
    ▶ sta per `(x1,..,xn)-> <obj>.<method>(x1,..,xn)`
3. `<class>::<instance-method>`
    ▶ sta per `(x1,x2,..,xn)-> x1.<instance-method>(x2,..,xn)`
            un metodo istanza e' tipo "stringa1"`.compareTo`("bob");
            (invece non lo e': compare("str1", "str2") )
4. `<class>::new`
    ▶ sta per `(x1,..,xn)-> new <class>(x1,..,xn)`

```java
public class AllLambdas {
    private static int mycompare(final String a, final String b){
        return a.compareTo(b);
    }

    public static void main(String[] args){
        final List<String> list = List.of("a", "bb", "c", "ddd");

        // sintassi completa
        Collections.sort(list, (String a, String b) -> {
            return a.length() - b.length();
        });

        // sintassi con type inference
        Collections.sort(list, (a, b) -> {
            return a.length() - b.length();
        });

        // sintassi con type inference e single - expression body
        Collections.sort(list, (a, b) -> a.length() - b.length());

        // il body `e una chiamata di metodo diretta : user `o un method reference
        Collections.sort(list, (a, b) -> mycompare(a, b));
        Collections.sort(list, AllLambdas::mycompare); // method reference
        // che e' una sintassi che ci sta, peccato che non ti fanno scrivere direttamente:
        // Collections.sort(list, mycompare);
    }
 }
```

### Filtri (predicati)

Predicati: input qualsiasi -> output booleano

Molto spesso le lamda sono fatte per fare del filtri per dire ad esempio tu puoi passare, tu non puoi passare.
Ottime per realizzare tutti quei metodi filter che prendono una lista e un predicato e returnano una lista ottenuta facendo passare solo gli elementi dettati dal predicato.
Infatti le lamda si usano solo al posto di oggetti che implementano una interfaccia che ha 1 SOLO metodo.
Visto che a volte voglio usare le lamda anche al posto di interfacce con piu' metodi volendo nelle interfacce si posso implementare dei metodi di default (che servono solo per le lamda) (nel caso di un interfaccia fatta cosi' si puo' mettere sopra @functionalInterface). 
Rettifico: le lamda si usano solo al posto di oggetti che implementano una interfaccia che ha 1 SOLO metodo che deve essere implementato.

CI sono diverse interfacce come predicate che implementano un solo metodo:
circa 7, e queste interfacce sono gia' di libreria!
`java.util.function`!

Un `consumer` prende un qualcosa e da in input void.
le classi di Iterable hanno anche il metodo di default forEach(Predicate) che accetta un predicate.
Cosi' puoi fare mylist.forEach(x -> System.out.println(x + " "));


### java.util.Map

Ci ritorniamo con le lambda.

Nell'interfaccia per le map e' pieno di metodi fatti apposta per essere usati con le lamda.
replaceAll, merge, ...

```java
public class UseMap {
    public static void main(String[] args){
        final Map<Integer, String> map = new HashMap<>();
        map.put(10, "a");
        map.put(20, "bb");
        map.put(30, "ccc");

        map.forEach((k, v) -> System.out.println(k + " " + v));

        map.replaceAll((k, v) -> v + k); // nuovi valori
        System.out.println(map);
        // {20= bb20 , 10= a10 , 30= ccc30 }

        map.merge(5, ".", String::concat);
        map.merge(10, ".", String::concat);
        System.out.println(map);
        // {20=bb20, 5=., 10=a10., 30=ccc30}

        System.out.println(map.getOrDefault(5, "no")); // "."
        System.out.println(map.getOrDefault(6, "no")); // "no"
    }
 }
```


### la classe Optional

E' come se fosse una lista con una sola lista.
E' come una scatola o piena o vuota.

Problema del null. Molto spesso quando chiami un metodo get di una qualsiasi classe c'e' la possibilita' che fallisca e ti returni un null.
Quindi sarebbe meglio che quella classe invece che returnare T (che non sei sicuro che non sia null) returni un `Optional<T>`.

Poi optional ha i metodi `boolean isPresent()`, `T get()`, `T orElse(T other)`.

Anche in questo caso la hanno progettata dopo le lambda creando metodi che si sposano benissimo con esse.

```java
public class Person {
    private final String name;
    private Optional<Person> partner = Optional.empty();

    public Person(final String name){
        this.name = Objects.requireNonNull(name);
    }
    public String getName(){
        return this.name;
    }
    public Optional<Person>getPartner(){
        return this.partner;
    }
    public void setPartner(final Person p) {
        // se non mi passano null (in tal caso eccezione) creo un optional pieno (con una persona)
        this.partner = Optional.of(Objects.requireNonNull(p));
    }
    public void removePartner() {
        this.partner = Optional.empty();
    }
    public Optional<String> getPartnerName(){
        return this.partner.map(Person::getName);
        // return this.partner.map(p -> p.getName());

        // invece di scrivere tutta sta roba
        // if (this.partner.isPresent()){
        //     return Optional.of(this.partner.get.().getName());
        // }
        // return Optional.empty();

        // la map prende in input un Optional<T> e returna un Optional<R>
        // in questo caso prendo un opzionale di person e returno un opzionale di string
        // ecco perche' si chiama map (da un optional di person a un o di string)
    }
 }
```

Molto carino optional, sembra utile, ti risparmia molta fatica
