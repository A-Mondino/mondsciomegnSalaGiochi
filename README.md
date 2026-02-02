# SalaGiochi

## Descrizione
Questo progetto è un'applicazione Java basata su Maven che implementa una piattaforma di intrattenimento virtuale, per simulare l'ingresso in una salagiochi


## Tecnologie utilizzate
- Java SE (JDK 21+)
- Maven
   ├ JUnit
   ├ H2
   └ JavaFX


## Struttura del progetto

salagiochi/
├── pom.xml							# file per gestire le dipendenze Maven
├── database/							# file del database
├── src/
    ├── main/
    │    └── java/
    │        └── /com/mondsciomegn/salagiochi
    │            	├── db/					# contiene tutte le classi del DB e le varie classi x la connessione
    │            	├── gui/
    │            	│    └── img/				# contiene le varie immagini utilizzate nel progetto
    │            	└── videogame/
    │            		 ├── battleship/		# contiene tutto l'insieme delle classi x battaglia navale
    │             		 └── roulette/			# contiene tutto l'insieme delle classi x roulette
    │
    └── test/							# contiene i test JUnit effettuati durante lo sviluppo
         └── java/
             └── /com/mondsciomegn/salagiochi
                    ├── db/
                    └── videogame/


## Documentazione
È possibile visualizzare i diagrammi seguendo dalle impostazioni di Eclipse:
 	Import projects.. -> General -> Project from Folder or Archive -> Import Source
 	-> (selezionare la cartella documenti della repository) -> Finish.

NOTA: 
Se non si dispone del plugin Papyrus integrato in Eclipse, è possibile usare il software stand-alone o consultare le versioni JPEG.
-> documenti/DiagrammiUML/DiagramJPEG/ 
	
Gli unici diagrammi che non compaiono importando il progetto documenti sono:
	-> Package Diagram, perchè generato tramite STAN-ide (è presente quindi solo in formato JPEG, come specificato)
 	-> Component Diagram, perchè realizzato con Plant UML (è presente quindi solo in formato JPEG, come specificato)
 

## Import
È possibile importare il progetto all'interno di Eclipse secondo le seguenti procedure:

1. Import projects.. -> Git -> Projects from Git (with Smart Import) -> Clone URL
 	-> (*inserire l'URL della repo: https://github.com/A-Mondino/mondsciomegnSalaGiochi)
 	-> Next -> Next -> (*scegliere la directory di destinazione: quella del WorkSpace) -> Next -> Finish

2. Import projects.. -> General -> Existing Projects into Workspace -> Browse root directory..
 	(selezionare la cartella codice/salagiochi scaricata da github)	-> Finish.
 	NOTA: se si seleziona l'intera cartella scaricata da git (che quindi contiene sia il progetto codice/salagiochi
 	      sia il progetto documenti) l'ide di Eclipse da problemi.


Una volta importato il progetto su Eclipse si può procedere con

## Build ed Esecuzione
Per compilare ed eseguire il progetto basta eseguire in ordine direttamente da Eclipse:
 	Run -> Run As -> Maven Build... -> (*digitare all'interno del campo Goals:) javafx:run -> Run

poi rimarrà memorizzato nell'history del menù Run o entrando in:
 	Run -> Run Configurations... -> Maven Build -> salagiochi


ATTENZIONE:
In caso non sia configurata su Eclipse una JDK con versione superiore alla 21.0, 
la build di Maven o l'avvio dell'applicazione potrebbero fallire. 
Per verificare e risolvere il problema seguire i seguenti passaggi:

File -> Properties -> Java Build Path 
(La finestra può essere trovata anche con Tasto destro sul progetto -> Build Path -> Configure Build Path...)

Successivamente va aperta la sezione Libraries. Se ci fosse una JRE con una versione inferiore alla 21
va rimossa. Per aggiungere una JRE con la giusta versione di JDK inclusa va premuto su 
↓↑ Classpath -> Add Library... -> JRE System Library -> Workspace Default JRE (jre) -> Finish -> Apply and Close.
NOTA: Se il problema fosse proprio la JRE di default si consiglia di installarne la versione più recente direttamente da Oracle



## Autore
Team SalaGiochi
