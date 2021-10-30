import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Configuratie {

	private Vak speler;
	private ArrayList<Vak> blokken; // volgorde van blokken wordt altijd behouden
	private Configuratie vorig;

	// temp:
	String zoekboomNode;
	int hashC;	// waarden om sneller te checken of twee configuraties dezelfde zijn;
	int hashR;	// dus public boolean equals(Configuratie configuratie, ArrayList<Vak> spelerEiland) sneller maken

	public Configuratie(Vak speler, ArrayList<Vak> blokken, Configuratie vorig, String zoekboomNode, int hashC, int hashR) {
		this.speler = speler;
		this.blokken = blokken;
		this.vorig = vorig;

		// temp:
		this.zoekboomNode = zoekboomNode;
		this.hashC = hashC;
		this.hashR = hashR;
	}
	
	// hash als een String
	public String getHash(){
		return hashC + "." + hashR;
	}

	// equal als en slechts als
	// de blokkenconfiguraties dezelfde zijn (ongeacht hun volgorde) en de spelers in hetzelfde eiland zitten
	// (de eilandenstructuur is gelijk voor beide configuraties zodra de blokkenconfiguraties dezelfde zijn)
	public boolean equals(Configuratie configuratie, ArrayList<Vak> spelerEiland) {
		//
		if(!(hashC == configuratie.hashC && hashR == configuratie.hashR)){
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
