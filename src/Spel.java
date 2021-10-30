import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Spel {
	
	public static Spel output;
	
	private ArrayList<String> lines = new ArrayList<String>();

	private int ROWS;
	private int COLS;

	private Vak[][] vakken;
	private ArrayList<Vak> blokken = new ArrayList<Vak>();
	private ArrayList<Vak> eindvakken = new ArrayList<Vak>();
	private Vak speler = null;

	private ArrayList<Vak> binnenEiland = new ArrayList<Vak>();

	private ArrayList<ArrayList<Vak>> vakkenOpEindvakPad_PerEindvak = new ArrayList<ArrayList<Vak>>();
	// voor elk eindvak een lijst van vakken vanwaar het eindvak bereikbaar is met een blok
	// vakkenOpEindvakPad_PerEindvak.get(i).get(0) == i-de eindvak
	private ArrayList<Vak> vakkenOpEindvakPad = new ArrayList<Vak>(); // alle vakken vanwaar blokken een eindvakPad hebben

	public ArrayList<Vak> getBlokken() {
		return blokken;
	}

	public ArrayList<Vak> getEindvakken() {
		return eindvakken;
	}
	
	public Vak getSpeler() {
		return speler;
	}

	public Spel(Scanner sc) {

		leesFile(sc);
		vakken = new Vak[ROWS][COLS];
		maakVakken();
		maakBinnenEiland();
		maakBuurRelatiesEnVerwijderBuitenVakken();
		maakVakkenOpEindvakPad_PerEindvak();
		maakVakkenOpEindvakPad();
		
		output = this;
	}

	// lees file
	public void leesFile(Scanner s) {
		String line;
		int rows = 0;
		int cols = 0;
		while (s.hasNextLine()) {
			line = s.nextLine();
			if (line.matches("^[ ]*#.*")) {
				lines.add(line);
				rows = rows + 1;
				cols = Math.max(cols, line.length());
				System.out.println(line);
			}
		}
		s.close();
		ROWS = rows; // null waarden op de buitenrand
		COLS = cols; // null waarden op de buitenrand
		System.out.println("ROWS: " + rows);
		System.out.println("COLS: " + cols);
	}

	//maak vakken
	public void maakVakken() {
		String line;
		for (int i = 0; i < lines.size(); i++) {
			line = lines.get(i);
			for (int j = 0; j < line.length(); j++) {
				Vak vak;
				switch (line.charAt(j)) {
				case '#':
					vak = null;
					break;
				case ' ':
					vak = new Vak(i, j, false);
					break;
				case '.':
					vak = new Vak(i, j, true);
					eindvakken.add(vak);
					break;
				case '$':
					vak = new Vak(i, j, false);
					blokken.add(vak);
					break;
				case '*':
					vak = new Vak(i, j, true);
					eindvakken.add(vak);
					blokken.add(vak);
					break;
				case '@':
					vak = new Vak(i, j, false);
					speler = vak;
					break;
				case '+':
					vak = new Vak(i, j, true);
					eindvakken.add(vak);
					speler = vak;
					break;
				default:
					vak = null;
					break;
				}
				vakken[i][j] = vak;
			}
		}
	}

	// print vakken
	public void printVakken() {
		System.out.println("vakken:");
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Vak vak = vakken[i][j];
				if (vak == null) {
					System.out.print('#');
				} else {
					if (eindvakken.contains(vak)) {
						if (blokken.contains(vak)) {
							System.out.print('*');
						} else if (speler.equals(vak)) {
							System.out.print('+');
						} else {
							System.out.print('.');
						}
					} else {
						if (blokken.contains(vak)) {
							System.out.print('$');
						} else if (speler.equals(vak)) {
							System.out.print('@');
						} else {
							System.out.print(' ');
						}
					}
				}
			}
			System.out.println("");
		}
	}

	// bepaal eiland van speler om binnen en buiten van elkaar te onderscheiden		
	public void maakBinnenEiland() {
		binnenEiland.add(speler);
		Stack<Vak> vakkenMetTeCheckenBuren = new Stack<Vak>();
		vakkenMetTeCheckenBuren.push(speler);
		while (!vakkenMetTeCheckenBuren.isEmpty()) {
			Vak kandidaat = vakkenMetTeCheckenBuren.pop();
			int[] signs = { 0, 1 };
			for (int i : signs) { // de 4 buren u,r,d,l overlopen
				for (int j : signs) {
					Vak buur = vakken[kandidaat.getRow() + i + j - 1][kandidaat.getCol() + (1 - i) + j - 1];
					if (buur != null && !binnenEiland.contains(buur)) {
						binnenEiland.add(buur);
						vakkenMetTeCheckenBuren.push(buur);
					}
				}
			}
		}
	}

	// print binneneiland
	public void printBinnenEiland() {
		System.out.println("aantal binnen: " + binnenEiland.size());
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Vak v = vakken[i][j];
				if (binnenEiland.contains(v)) {
					System.out.print(' ');
				} else {
					System.out.print('#');
				}
			}
			System.out.println("");
		}
	}

	// maak buur relaties aan en verwijder buitenvakken
	public void maakBuurRelatiesEnVerwijderBuitenVakken() {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				Vak vak = vakken[r][c];
				if (!binnenEiland.contains(vak)) {
					vakken[r][c] = null; // vak verwijderen
				} else {
					int[] signs = { 0, 1 };
					for (int i : signs) { // de 4 buren u,r,d,l overlopen
						for (int j : signs) {
							Vak buur = vakken[vak.getRow() + i + j - 1][vak.getCol() + (1 - i) + j - 1];
							if (buur != null) {
								if (i == 0) {
									if (j == 0) {
										vak.setBoven(buur);
										buur.setOnder(vak);
									} else {
										vak.setRechts(buur);
										buur.setLinks(vak);
									}
								} else {
									if (j == 0) {
										vak.setLinks(buur);
										buur.setRechts(vak);
									} else {
										vak.setOnder(buur);
										buur.setBoven(vak);
									}
								}
							}
						}
					}
				}

			}
		}
	}

	// print buren relaties
	public void printBurenRelaties() {
		System.out.println("buren relaties:");
		for (int i = 0; i < ROWS; i++) {
			// boven
			for (int j = 0; j < COLS; j++) {
				Vak vak = vakken[i][j];
				Vak buur;

				if (vak != null) {
					System.out.print('X');
				} else {
					System.out.print('#');
				}

				if (vak != null) {
					buur = vak.getBoven();
					if (buur != null && buur.getOnder().equals(vak)) {
						System.out.print('+');
					} else {
						System.out.print('?');
					}
				} else {
					System.out.print('#');
				}

				if (vak != null) {
					System.out.print('X');
				} else {
					System.out.print('#');
				}
			}
			System.out.println("");
			// links en rechts
			for (int j = 0; j < COLS; j++) {
				Vak vak = vakken[i][j];
				Vak buur;
				if (vak != null) {
					buur = vak.getLinks();
					if (buur != null && buur.getRechts().equals(vak)) {
						System.out.print('-');
					} else {
						System.out.print('?');
					}
				} else {
					System.out.print('#');
				}

				if (vak != null) {
					System.out.print(' ');
				} else {
					System.out.print('#');
				}

				if (vak != null) {
					buur = vak.getRechts();
					if (buur != null && buur.getLinks().equals(vak)) {
						System.out.print('+');
					} else {
						System.out.print('?');
					}
				} else {
					System.out.print('#');
				}
			}
			System.out.println("");
			// onder
			for (int j = 0; j < COLS; j++) {
				Vak vak = vakken[i][j];
				Vak buur;

				if (vak != null) {
					System.out.print('X');
				} else {
					System.out.print('#');
				}

				if (vak != null) {
					buur = vak.getOnder();
					if (buur != null && buur.getBoven().equals(vak)) {
						System.out.print('-');
					} else {
						System.out.print('?');
					}
				} else {
					System.out.print('#');
				}

				if (vak != null) {
					System.out.print('X');
				} else {
					System.out.print('#');
				}
			}
			System.out.println("");
		}
	}

	public void maakVakkenOpEindvakPad_PerEindvak() {
		for (int i = 0; i < eindvakken.size(); i++) {
			Vak eindvak = eindvakken.get(i);
			vakkenOpEindvakPad_PerEindvak.add(i, new ArrayList<Vak>());
			// lijst met vakkenOpEindvakPad voor het huidig i-de eindvak
			ArrayList<Vak> huidigeVakkenOpEindvakPad = vakkenOpEindvakPad_PerEindvak.get(i);
			Stack<Vak> vakkenMetTeCheckenBuren = new Stack<Vak>();
			huidigeVakkenOpEindvakPad.add(eindvak);
			vakkenMetTeCheckenBuren.push(eindvak);
			while (!vakkenMetTeCheckenBuren.isEmpty()) {
				Vak vakOpEilandRand = vakkenMetTeCheckenBuren.pop();
				Vak[] buren = vakOpEilandRand.getBuren();
				for (int j = 0; j < buren.length; j++) {
					Vak buur = buren[j]; // de 4 buren u,r,d,l overlopen
					if (buur != null) {
						if (buur.getBuren()[j] != null) {
							if (!huidigeVakkenOpEindvakPad.contains(buur)) {
								huidigeVakkenOpEindvakPad.add(buur);
								if (!vakkenMetTeCheckenBuren.contains(buur)) {
									vakkenMetTeCheckenBuren.push(buur);
								}
							}
						}
					}
				}
			}
		}

	}

	public void printVakkenOpEindvakPad_PerEindvak() {
		System.out.println("vakken op eindvakpad per eindvak:");
		for (int k = 0; k < vakkenOpEindvakPad_PerEindvak.size(); k++) {
			ArrayList<Vak> huidigeVakkenOpEindvakPad = vakkenOpEindvakPad_PerEindvak.get(k); // lijst met vakkenBereikbaarMetBlok voor het huidig i-de eindvak
			Vak eindvak = huidigeVakkenOpEindvakPad.get(0); // huidig i-de eindvak
			System.out.println("eindvak " + k + " bereikbaar met blok vanaf:");
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					Vak v = vakken[i][j];
					if (v != null) {
						if (eindvak.equals(v) && huidigeVakkenOpEindvakPad.contains(v)) {
							System.out.print('E');
						} else if (huidigeVakkenOpEindvakPad.contains(v)) {
							System.out.print('+');
						} else {
							System.out.print(' ');
						}
					} else {
						System.out.print('#');
					}
				}
				System.out.println("");
			}
		}

	}

	public void maakVakkenOpEindvakPad() {
		for (int i = 0; i < vakkenOpEindvakPad_PerEindvak.size(); i++) {
			ArrayList<Vak> huidigeVakkenOpEindvakPad = vakkenOpEindvakPad_PerEindvak.get(i);
			for (int j = 0; j < huidigeVakkenOpEindvakPad.size(); j++) {
				Vak huidigVakOpEindVakPad = huidigeVakkenOpEindvakPad.get(j);
				if (!vakkenOpEindvakPad.contains(huidigVakOpEindVakPad)) {
					vakkenOpEindvakPad.add(huidigVakOpEindVakPad);
					huidigVakOpEindVakPad.setLigtOpEindvakPad(true); // ligtOpEindvakPad = true
				}
			}
		}

	}

	public void printVakkenOpEindvakPad() {
		System.out.println("alle vakken op een eindvakpad:");
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Vak v = vakken[i][j];
				if (v != null) {
					if (eindvakken.contains(v) && vakkenOpEindvakPad.contains(v)) {
						System.out.print('E');
					} else if (vakkenOpEindvakPad.contains(v)) {
						System.out.print('+');
					} else {
						System.out.print(' ');
					}
				} else {
					System.out.print('#');
				}
			}
			System.out.println("");
		}
	}

	// print of een vak ligtOpEindvakPad
	public void printLigtNietOpEindvakPad() {
		System.out.println("alle vakken niet op een eindvakpad:");
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Vak v = vakken[i][j];
				if (v != null) {
					if (v.ligtOpEindvakPad() == false) {
						System.out.print('X');
					} else {
						System.out.print(' ');
					}
				} else {
					System.out.print('#');
				}
			}
			System.out.println("");
		}
	}

	public void printConfiguratie(Configuratie configuratie) {
		System.out.println("configuratie: ");
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Vak v = vakken[i][j];
				if (binnenEiland.contains(v)) {
					if (configuratie.getBlokken().contains(v)) {
						System.out.print('B');
					} else if (configuratie.getSpeler() == v) {
						System.out.print('S');
					} else {
						System.out.print(' ');
					}
				} else {
					System.out.print('#');
				}
			}
			System.out.println("");
		}
	}
	
	// bereken en geef een lijst van de eilanden wanneer alle blokken zich bevinden op de eindvakken
	public ArrayList<ArrayList<Vak>> getOplossingEilanden(){
		ArrayList<ArrayList<Vak>> eilanden = new ArrayList<ArrayList<Vak>>();
		
		ArrayList<Vak> restVakken = new ArrayList<Vak>(binnenEiland.size());
		restVakken.addAll(binnenEiland);
		restVakken.removeAll(eindvakken);

		while (! restVakken.isEmpty()) {
			ArrayList<Vak> eiland = new ArrayList<Vak>();
			Vak eilandVak = restVakken.get(0);
			eiland.add(eilandVak);
			Stack<Vak> vakkenMetTeCheckenBuren = new Stack<Vak>();
			vakkenMetTeCheckenBuren.push(eilandVak);
			while (!vakkenMetTeCheckenBuren.isEmpty()) {
				Vak kandidaat = vakkenMetTeCheckenBuren.pop();
				Vak[] buren = kandidaat.getBuren();
				for(int i=0; i< buren.length; i++){
						Vak buur = buren[i];
						if (buur != null && !eiland.contains(buur) && !buur.isEindvak()) {
							eiland.add(buur);
							vakkenMetTeCheckenBuren.push(buur);
						}
				}
			}
			eilanden.add(eiland);
			restVakken.removeAll(eiland);
		}		
		return eilanden;
	}
	
	// geef een lijst van mogelijke eindposities van de speler
	public ArrayList<Vak> getSpelerEindposities(){
		ArrayList<Vak> eindposities = new ArrayList<Vak>();
		ArrayList<ArrayList<Vak>> eilanden = getOplossingEilanden();
		for(int i = 0; i < eilanden.size(); i++){		
			ArrayList<Vak> eiland = eilanden.get(i);
			Vak eilandVak = eiland.get(0);
			ArrayList<Vak> bezochteEilandVakken = new ArrayList<Vak>();
			bezochteEilandVakken.add(eilandVak);
			Stack<Vak> vakkenMetTeCheckenBuren = new Stack<Vak>();
			vakkenMetTeCheckenBuren.push(eilandVak);
			while (!vakkenMetTeCheckenBuren.isEmpty()) {
				Vak kandidaat = vakkenMetTeCheckenBuren.pop();
				Vak[] buren = kandidaat.getBuren();
				for(int j = 0; j < buren.length; j++){
					Vak buur = buren[j];
					if (buur != null) {
						Vak spelerKandidaat = buren[(j+2)%4];
						if(buur.isEindvak()){
							if(spelerKandidaat != null && !spelerKandidaat.isEindvak()){
								if(!eindposities.contains(kandidaat)){
									eindposities.add(kandidaat);
								}
							}
						}
						else if(!bezochteEilandVakken.contains(buur)){
							bezochteEilandVakken.add(buur);
							vakkenMetTeCheckenBuren.push(buur);
						}
						
						
					}
				}
			}
		}		
		return eindposities;
	}

}
