import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/*
 * Deze sokobanSolver2 zoekt een oplossing door de zoekbomen
 * vanaf de startpositie en de eindpositie per diepte tegelijk te berekenen.
 * Op elke diepte wordt nagegaan of er een configuratie bestaat
 * die voorkomt als een bladconfiguratie in elk van beide zoekbomen.
 */
public class Solver {

	public static void main(String[] args) {

		String filename = args[0];
		System.out.println(filename);
		
		int maxRuntime = 300; // in seconden
		if (args.length > 1){
			String strMaxRuntime = args[1];
			try {
				maxRuntime = Integer.parseInt(strMaxRuntime);
			} catch (NumberFormatException e){
				maxRuntime = 300;
			}
		}
		System.out.println(maxRuntime);

		Scanner sc = null;
		try {
			sc = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println(filename + " niet gevonden");
			return;
		}

		Spel spel = new Spel(sc); // file lezen en vakken initialiseren
		sc.close();

//		spel.printVakken();
//		spel.printBinnenEiland();
//		spel.printBurenRelaties();
//		spel.printVakkenOpEindvakPad_PerEindvak();
//		spel.printVakkenOpEindvakPad();
		spel.printLigtNietOpEindvakPad();

		// blokken
		ArrayList<Vak> blokkenOld = spel.getBlokken();
		ArrayList<Vak> blokken = new ArrayList<Vak>(blokkenOld.size()); // capacity gelijk stellen aan size
		blokken.addAll(blokkenOld);
//		System.out.print("blokken: ");
//		for (int i = 0; i < blokken.size(); i++) {
//			System.out.print("(" + blokken.get(i).getRow() + "," + blokken.get(i).getCol() + ") ");
//		}
//		System.out.println("");
		
		// eindvakken
		ArrayList<Vak> eindVakkenOld = spel.getEindvakken();
		ArrayList<Vak> eindVakken = new ArrayList<Vak>(eindVakkenOld.size()); // capacity gelijk stellen aan size
		eindVakken.addAll(eindVakkenOld);

		// speler
		Vak speler = spel.getSpeler();
//		System.out.print("speler: ");
//		System.out.println("(" + speler.getRow() + "," + speler.getCol() + ") ");
		
		// oplossingEilanden
		ArrayList<ArrayList<Vak>> oplossingEilanden = spel.getOplossingEilanden();
		System.out.print(oplossingEilanden.size() + " oplossingEilanden met groottes: ");
		for(int i = 0; i < oplossingEilanden.size(); i++){
			System.out.print(oplossingEilanden.get(i).size() + " ");
		}
		System.out.println("");
		
		// spelerEindposities
		ArrayList<Vak> spelerEindposities = spel.getSpelerEindposities();
		
		// zoekboom
		//
		int wHashC = 0;
		int wHashR = 0;
		for (int i = 0; i < blokken.size(); i++) {
			wHashC = wHashC + blokken.get(i).getCol();
			wHashR = wHashR + blokken.get(i).getRow();
		}
		int wHashValue = wHashC + (wHashR * spel.getHashBase());  // elandHash wordt berekend in DubbeleZoekboom
		Configuratie wortelConfiguratie = new Configuratie(speler, blokken, null, wHashValue);
		
		int tHashC = 0;
		int tHashR = 0;
		for (int i = 0; i < eindVakken.size(); i++) {
			tHashC = tHashC + eindVakken.get(i).getCol();
			tHashR = tHashR + eindVakken.get(i).getRow();
		}
		int tHashValue = tHashC + (tHashR * spel.getHashBase());  // elandHash wordt berekend in DubbeleZoekboom
		ArrayList<Configuratie> topConfiguraties = new ArrayList<Configuratie>();
		for(int i = 0; i < oplossingEilanden.size(); i++){
			ArrayList<Vak> oplossingEiland = oplossingEilanden.get(i);
			for (int j = 0; j < spelerEindposities.size(); j++) {
				Vak kandidaatTopSpeler = spelerEindposities.get(j);
				if(oplossingEiland.contains(kandidaatTopSpeler)){
					Configuratie topConfiguratie = new Configuratie(kandidaatTopSpeler, eindVakken, null, tHashValue);					
					topConfiguraties.add(topConfiguratie);
				}
			}			
		}
		//
		System.out.println(topConfiguraties.size() + " topConfiguraties:");
		for (int i = 0; i < topConfiguraties.size(); i++) {
			spel.printConfiguratie(topConfiguraties.get(i));
		}		
		
		DubbeleZoekboom dubbeleZoekboom = new DubbeleZoekboom(wortelConfiguratie, topConfiguraties, spel, maxRuntime);
		
		// zoek oplossing
		final long startTime;
		final long endTime;
		startTime = System.nanoTime();
		dubbeleZoekboom.zoekOplossing();
		endTime = System.nanoTime();
		System.out.println("Tijd: " + ((endTime - startTime) / 1000000) + " ms");
		//System.err.println("Max JVM memory: " + Runtime.getRuntime().maxMemory());

		if (dubbeleZoekboom.isOplossingGevonden()) {
			Oplossing oplossing = dubbeleZoekboom.getOplossing();
//			spel.printVakken();
			oplossing.printOplossingsStappen();
//			oplossing.printBlokkenPad();
		}

		System.out.println("zoekboom diepte: " + dubbeleZoekboom.getWortelDiepte());
		// --wortel
		System.out.println("zoekboom wortel historiek aantal_keys: " + dubbeleZoekboom.getWortelHistoriek().keySet().size());
		int wZoekboomSize = 0;
		for (int key : dubbeleZoekboom.getWortelHistoriek().keySet()) {
			wZoekboomSize = wZoekboomSize + dubbeleZoekboom.getWortelHistoriek().get(key).size();	// aantal configuraties met hashwaarde==key
		}
		System.out.println("zoekboom wortel historiek grootte: " + wZoekboomSize);
		System.out.println("zoekboom wortel historiek loops: " + dubbeleZoekboom.wLoops);
		System.out.println("zoekboom wortel aantalVierkanten: " + dubbeleZoekboom.aantalVierkanten);
		System.out.println("zoekboom wortel aantal bladconfiguraties: " + dubbeleZoekboom.getWortelBladConfiguratiesFIFO().size());
		// --top
		System.out.println("zoekboom top historiek aantal_keys: " + dubbeleZoekboom.getTopHistoriek().keySet().size());
		int tZoekboomSize = 0;
		for (int key : dubbeleZoekboom.getTopHistoriek().keySet()) {
			tZoekboomSize = tZoekboomSize + dubbeleZoekboom.getTopHistoriek().get(key).size();	// aantal configuraties met hashwaarde==key
		}
		System.out.println("zoekboom top historiek grootte: " + tZoekboomSize);
		System.out.println("zoekboom top historiek loops: " + dubbeleZoekboom.tLoops);
		System.out.println("zoekboom top aantal bladconfiguraties: " + dubbeleZoekboom.getTopBladConfiguratiesFIFO().size());
		
		/*
		System.out.println("wortel verdeling:");
		String wList = "";
		String wRowList = "";
		String wColList = "";
		String wHashAantalList = "";
		for (String hash : dubbeleZoekboom.getWortelHistoriek().keySet()) {
			int hashAantal = dubbeleZoekboom.getWortelHistoriek().get(hash).size();
			//System.out.println(hash);
			String[] hashes = hash.split("\\.");
			String wRow = hashes[0];
			String wCol = hashes[1];
			System.out.println("(x,y)= " + wRow + "," + wCol + " : " + hashAantal);
			wList = wList + "[" + wRow + "," + wCol + "," + hashAantal + "],";
			wRowList = wRowList + wRow + ",";
			wColList = wColList + wCol + ",";
			wHashAantalList = wHashAantalList + hashAantal + ",";
		}
		System.out.println("wList:= " + wList);
		System.out.println("wRowList:= " + wRowList);
		System.out.println("wColList:= " + wColList);
		System.out.println("wHashAantalList:= " + wHashAantalList);
		System.out.println("");
		
		System.out.println("top verdeling:");
		String tList = "";
		String tRowList = "";
		String tColList = "";
		String tHashAantalList = "";
		for (String hash : dubbeleZoekboom.getTopHistoriek().keySet()) {
			int hashAantal = dubbeleZoekboom.getTopHistoriek().get(hash).size();
			String[] hashes = hash.split("\\.");
			String tRow = hashes[0];
			String tCol = hashes[1];
			System.out.println("(x,y)= " + tRow + "," + tCol + " : " + hashAantal);
			tList = tList + "[" + tRow + "," + tCol + "," + hashAantal + "],";
			tRowList = tRowList + tRow + ",";
			tColList = tColList + tCol + ",";
			tHashAantalList = tHashAantalList + hashAantal + ",";
		}
		System.out.println("tList:= " + tList);
		System.out.println("tRowList:= " + tRowList);
		System.out.println("tColList:= " + tColList);
		System.out.println("tHashAantalList:= " + tHashAantalList);
		System.out.println("");
		*/

		//		LinkedList<Configuratie> configuraties = zoekboom.getBladConfiguratiesFIFO();
		//		for (int i = 0; i < configuraties.size(); i++) {
		//			Configuratie tempEindConfiguratie = configuraties.get(i);
		//			System.out.println("bladConfiguratiesFIFO: " + i);
		//			while(tempEindConfiguratie != null){				
		//				spel.printConfiguratie(tempEindConfiguratie);
		//				tempEindConfiguratie = tempEindConfiguratie.getVorig();
		//			}
		//		}
		//		System.out.println("startpositie was:");
		//		spel.printVakken();

		//		// ter info: hoe een FIFO maken en gebruiken
		//		char arr[] = {3,1,4,1,5,9,2,6,5,3,5,8,9};
		//        LinkedList<Integer> fifo = new LinkedList<Integer>();
		//        for (int i = 0; i < arr.length; i++)
		//            fifo.add (new Integer (arr[i]));
		//        System.out.print (fifo.removeFirst() + ".");
		//        while (! fifo.isEmpty())
		//            System.out.print (fifo.removeFirst());
		//        System.out .println();

	}

}
