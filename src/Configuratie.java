import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Configuratie {

	private Vak speler;
	private ArrayList<Vak> blokken; // volgorde van blokken wordt altijd behouden
	private Configuratie vorig;

	// temp:
	//int hashC;	// waarden om sneller te checken of twee configuraties dezelfde zijn;
	//int hashR;	// dus public boolean equals(Configuratie configuratie, ArrayList<Vak> spelerEiland) sneller maken
	//int hashEiland; // grootte van spelereiland
	private int hashValue;  // hashValue = hashC + hashR*hashBase + hashEiland*hashBase^2
	                        // with hashBase = max(rows,columns,blokken)^2 > rows*blokken, columns*blokken, rows*columns > hashC, hashR, hashEiland

	public Configuratie(Vak speler, ArrayList<Vak> blokken, Configuratie vorig, int hashValue) {
		this.speler = speler;
		this.blokken = blokken;
		this.vorig = vorig;

		// temp:
		//this.hashC = hashC;
		//this.hashR = hashR;
		this.hashValue = hashValue;
	}
	
	//public int getHashC(int hashBase){
	//	return hashValue % hashBase;
	//}
	//public int getHashR(int hashBase){
	//	return (hashValue / hashBase) % hashBase;
	//}
	//public int getHashEiland(int hashBase){
	//	return hashValue % (hashBase * hashBase);
	//}
	
	// set grootte van spelereiland
	public void setEilandHash(int hashBase, int eilandHash){
		hashValue = hashValue + (eilandHash*(hashBase*hashBase));
	}
	
	// hash als een String
	public int getHash(){
		return hashValue;
	}

	// equal als en slechts als
	// de blokkenconfiguraties dezelfde zijn (ongeacht hun volgorde) en de spelers in hetzelfde eiland zitten
	// (de eilandenstructuur is gelijk voor beide configuraties zodra de blokkenconfiguraties dezelfde zijn)
	public boolean equals(Configuratie configuratie, ArrayList<Vak> spelerEiland) {
		//
		//if(this.getHashC() != configuratie.getHashC() || this.getHashR() != configuratie.getHashR()){
		//	return false;
		//}
		//if(spelerEiland.size() != this.getHashEiland()){
		//	return false;
		//}
		if(this.getHash() != configuratie.getHash()){
			return false;
		}
		if (!blokken.containsAll(configuratie.getBlokken())) { // als subset, dan gelijk, want zelfde kardinaliteit
			return false;
		}
		if (!spelerEiland.contains(speler)) {
			return false;
		}
		return true;
	}

	public Vak getSpeler() {
		return speler;
	}

	public ArrayList<Vak> getBlokken() {
		return blokken;
	}

	public Configuratie getVorig() {
		return vorig;
	}

	// geeft een pad van minimale lengte vanaf einde naar this.speler (deze beide vakken inclusief)
	public ArrayList<Vak> getKortstePad(Vak einde) {
		ArrayList<Vak> pad = new ArrayList<Vak>();

		ArrayList<Vak> binnenEiland = new ArrayList<Vak>();
		ArrayList<Vak> vorigeVakken = new ArrayList<Vak>(); // vorig vak in het pad naar binnenEiland.get(i) = vorigeVakken.get(i)
		binnenEiland.add(speler);
		vorigeVakken.add(null);
		LinkedList<Vak> vakkenMetTeCheckenBurenFIFO = new LinkedList<Vak>();
		vakkenMetTeCheckenBurenFIFO.add(speler);

		zoeken: // label
		while (!vakkenMetTeCheckenBurenFIFO.isEmpty()) {
			Vak kandidaat = vakkenMetTeCheckenBurenFIFO.removeFirst();
			Vak[] buren = kandidaat.getBuren();
			for (int k = 0; k < buren.length; k++) {
				Vak buur = buren[k];
				if (buur != null && !blokken.contains(buur) && !binnenEiland.contains(buur)) {
					binnenEiland.add(buur);
					vorigeVakken.add(kandidaat);
					if (buur == einde) {
						break zoeken;
					}
					vakkenMetTeCheckenBurenFIFO.add(buur);
				}
			}
		}
		
		Vak volgendePadElement = einde;
		while (volgendePadElement != null) {
			pad.add(volgendePadElement);
			int volgendePadElementIndex = binnenEiland.indexOf(volgendePadElement);
			
			// TODO:
			if(volgendePadElementIndex == -1){
				System.out.println("volgendePadElementIndex == -1");
				Spel.output.printConfiguratie(this);
				for (Vak eilandVak : binnenEiland) {
					System.out.print("(" + eilandVak.getRow() + "," + eilandVak.getCol() + ") ");
				}
				System.out.println("");
				System.out.println("einde: (" + einde.getRow() + "," + einde.getCol() + ")");
				System.out.println("speler: (" + speler.getRow() + "," + speler.getCol() + ")");
			}
			
			volgendePadElement = vorigeVakken.get(volgendePadElementIndex);
		}

		return pad;
	}
	
}
