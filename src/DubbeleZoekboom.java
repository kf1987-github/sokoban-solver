import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.lang.Math;

public class DubbeleZoekboom {

	//private ArrayList<Configuratie> historiek = new ArrayList<Configuratie>(); // voor loopdetectie
	private int wDiepte;
	private int tDiepte;

	private HashMap<Integer, ArrayList<Configuratie>> wHistoriek = new HashMap<Integer, ArrayList<Configuratie>>();
	private LinkedList<Configuratie> wBladConfiguratiesFIFO = new LinkedList<Configuratie>(); // te checken configuraties, stijgend volgens diepte
	private ArrayList<Integer> wAantalConfiguraties_PerDiepte = new ArrayList<Integer>(); // wAantalConfiguraties_PerDiepte.get(<diepte>) = <aantal_per_diepte>

	private HashMap<Integer, ArrayList<Configuratie>> tHistoriek = new HashMap<Integer, ArrayList<Configuratie>>();
	private LinkedList<Configuratie> tBladConfiguratiesFIFO = new LinkedList<Configuratie>(); // te checken configuraties, stijgend volgens diepte
	private ArrayList<Integer> tAantalConfiguraties_PerDiepte = new ArrayList<Integer>(); // tAantalConfiguraties_PerDiepte.get(<diepte>) = <aantal_per_diepte>

	private Configuratie wortelOplossingConfiguratie;
	private Configuratie topOplossingConfiguratie;

	private boolean oplossingGevonden = false;
	
	// private int hashValue;  // hashValue = hashC + hashR*hashBase + hashEiland*hashBase^2
	// with hashBase = max(rows,columns,blokken)^2 > rows*blokken, columns*blokken, rows*columns > hashC, hashR, hashEiland
	// private int hashBase;
	
	private int maxRuntime;

	// temp:
	private Spel spel;
	int wLoops = 0;
	int tLoops = 0;
	int aantalVierkanten = 0;
	int aantalWortelConfiguratiesReedsInHistoriek = 0;
	int aantalTopConfiguratiesReedsInHistoriek = 0;
	long startTimeNanos = 0;
	long wStartNanos = 0;
	long wTotalNanos = 0;
	long wStartVierkantNanos = 0;
	long wTotalVierkantNanos = 0;
	long tStartNanos = 0;
	long tTotalNanos = 0;

	public DubbeleZoekboom(Configuratie wortelConfiguratie, ArrayList<Configuratie> topConfiguraties, Spel spel, int maxRuntime) {
		this.wDiepte = 0;
		this.tDiepte = 0;

		this.wBladConfiguratiesFIFO.push(wortelConfiguratie);
		this.wAantalConfiguraties_PerDiepte.add(wDiepte, 1);

		for(int i = 0; i < topConfiguraties.size(); i++){
			this.tBladConfiguratiesFIFO.push(topConfiguraties.get(i));
		}
		this.tAantalConfiguraties_PerDiepte.add(wDiepte, topConfiguraties.size());

		//temp:
		this.spel = spel;
		//this.hashBase = (int) Math.pow(Math.max(Math.max(spel.getRows(), spel.getCols()), spel.getBlokken().size()),2);
		this.maxRuntime = maxRuntime;
	}

	public boolean isOplossingGevonden() {
		return oplossingGevonden;
	}

	// geeft de configuratie uit de wHistoriek, die voorkomt in de oHistoriek,
	// van hieruit kan de helft van de oplossing gereconstrueerd worden tot aan de wortelConfiguratie
	public Configuratie getWortelOplossing() {
		return wortelOplossingConfiguratie;
	}

	// geeft de configuratie uit de oHistoriek, die voorkomt in de wHistoriek,
	// van hieruit kan de helft van de oplossing gereconstrueerd worden tot aan de oplossingConfiguratie
	public Configuratie getTopOplossing() {
		return topOplossingConfiguratie;
	}

	// geeft de oplossing weer voor deze dubbele zoekboom
	public Oplossing getOplossing(){
		return new Oplossing(wortelOplossingConfiguratie, topOplossingConfiguratie);
	}

	public HashMap<Integer, ArrayList<Configuratie>> getWortelHistoriek() {
		return wHistoriek;
	}

	public HashMap<Integer, ArrayList<Configuratie>> getTopHistoriek() {
		return tHistoriek;
	}

	public int getWortelDiepte() {
		return wDiepte;
	}

	public int getTopDiepte() {
		return tDiepte;
	}

	public LinkedList<Configuratie> getWortelBladConfiguratiesFIFO() {
		return wBladConfiguratiesFIFO;
	}

	public int getWortelAantalConfiguratiesOpDiepte(int diepte) {
		return wAantalConfiguraties_PerDiepte.get(diepte);
	}

	public LinkedList<Configuratie> getTopBladConfiguratiesFIFO() {
		return tBladConfiguratiesFIFO;
	}

	public int getTopAantalConfiguratiesOpDiepte(int diepte) {
		return tAantalConfiguraties_PerDiepte.get(diepte);
	}

	public void zoekOplossing() {

		// check of de wortelconfiguratie een oplossing is
		oplossingGevonden = true;
		for (Vak blok : wBladConfiguratiesFIFO.get(0).getBlokken()) {
			if (!blok.isEindvak()) {
				oplossingGevonden = false;
				continue;
			}
		}
		if (oplossingGevonden) {
			System.out.println("De wortelConfiguratie is een OPLOSSING !!!");
			return;
		}
		
		long startTimeNanos = System.nanoTime();

		// begin aan het echte werk
		int wAantalConfiguratiesVoorHuidigeDiepte;
		int wAantalConfiguratiesOpVolgendeDiepte = wAantalConfiguraties_PerDiepte.get(wDiepte); // aantal nieuw gecreêerde configuraties (een counter)
		int wHashHuidigeConfiguratie;

		int tAantalConfiguratiesVoorHuidigeDiepte;
		int tAantalConfiguratiesOpVolgendeDiepte = tAantalConfiguraties_PerDiepte.get(wDiepte); // aantal nieuw gecreêerde configuraties (een counter)
		int tHashHuidigeConfiguratie;

		//while (wDiepte + tDiepte < 200 ) { // todo
		while ((long) (wTotalNanos + tTotalNanos) < (long) ((long)(maxRuntime) * 1000000000)) { // todo
		
		    System.out.println("wTotalNanos: " + wTotalNanos
			                   + " --- wTotalVierkantNanos: " + wTotalVierkantNanos);
			System.out.println("tTotalNanos: " + tTotalNanos);
			
			/**/
			System.out.println("zoekboom diepte: " + wDiepte);
			// --wortel
			System.out.println("zoekboom wortel historiek aantal_keys: " + wHistoriek.keySet().size());
			int wZoekboomSize = 0;
			//String zoekboomSizes = "";
			for (int key : wHistoriek.keySet()) {
				wZoekboomSize = wZoekboomSize + wHistoriek.get(key).size();	// aantal configuraties met hashwaarde==key
				//zoekboomSizes = zoekboomSizes + key + ": " + wZoekboomSize + " ";
			}
			//System.out.println("zoekboom wortel historiek groottes: " + zoekboomSizes);
			System.out.println("zoekboom wortel historiek grootte: " + wZoekboomSize);
			System.out.println("zoekboom wortel historiek loops: " + wLoops);
			System.out.println("zoekboom wortel aantalVierkanten: " + aantalVierkanten);
			System.out.println("zoekboom wortel aantal bladconfiguraties: " + wBladConfiguratiesFIFO.size());
			// --top
			System.out.println("zoekboom top historiek aantal_keys: " + tHistoriek.keySet().size());
			int tZoekboomSize = 0;
			for (int key : tHistoriek.keySet()) {
				tZoekboomSize = tZoekboomSize + tHistoriek.get(key).size();	// aantal configuraties met hashwaarde==key
			}
			System.out.println("zoekboom top historiek grootte: " + tZoekboomSize);
			System.out.println("zoekboom top historiek loops: " + tLoops);
			System.out.println("zoekboom top aantal bladconfiguraties: " + tBladConfiguratiesFIFO.size());
			/**/
			
			// temp:
			System.out.println("WortelDiepte: " + wDiepte);
			System.out.println("TopDiepte: " + tDiepte);

			//
			System.out.println("wortelAantalConfiguratiesVoorHuidigeDiepte: " + wAantalConfiguratiesOpVolgendeDiepte
			                   + " --- vierkanten: " + aantalVierkanten
							   + " --- aantalWortelConfiguratiesReedsInHistoriek: " + aantalWortelConfiguratiesReedsInHistoriek);
			System.out.println("topAantalConfiguratiesVoorHuidigeDiepte: " + tAantalConfiguratiesOpVolgendeDiepte
			                   + " --- aantalTopConfiguratiesReedsInHistoriek: " + aantalTopConfiguratiesReedsInHistoriek);
			System.out.println("System.nanoTime();: " + (System.nanoTime() - startTimeNanos) );

			// TODO:
			if(wAantalConfiguratiesOpVolgendeDiepte < tAantalConfiguratiesOpVolgendeDiepte){
			//if(wTotalNanos <= tTotalNanos){
				wAantalConfiguratiesVoorHuidigeDiepte = wAantalConfiguratiesOpVolgendeDiepte;
				wAantalConfiguratiesOpVolgendeDiepte = 0; // counter resetten
				
				System.out.println("wortelAantalConfiguratiesVoorHuidigeDiepte: " + wAantalConfiguratiesVoorHuidigeDiepte);
				wStartNanos = System.nanoTime();

				//
				// --wortel
				//
				for (int i = 0; i < wAantalConfiguratiesVoorHuidigeDiepte; i++) {

					ArrayList<Vak> wSpelerEiland = new ArrayList<Vak>();
					Stack<Vak> wTeCheckenEilandVakken = new Stack<Vak>(); // vakken op de rand van het huidig onvoltooid eiland

					Configuratie wHuidigeConfiguratie = wBladConfiguratiesFIFO.removeFirst();
					ArrayList<Vak> wBlokken = wHuidigeConfiguratie.getBlokken();
					Vak wSpeler = wHuidigeConfiguratie.getSpeler();
					wSpelerEiland.add(wSpeler);

					wTeCheckenEilandVakken.push(wSpeler);

					LinkedList<Configuratie> wKandidaatBladConfiguratieFIFO = new LinkedList<Configuratie>();

					// temp:				
					//String wZoekboomNode = wHuidigeConfiguratie.zoekboomNode;
					// System.out.println("wHuidigeConfiguratie " + zoekboomNode + " (" + i + ") " + " heeft bladconfiguraties:");
					int wBlad = 0;

					// bereken eiland van speler en maak ondertussen nieuwe bladConfiguraties voor wHuidigeConfiguratie
					while (!wTeCheckenEilandVakken.isEmpty()) {
						Vak vakOpEilandRand = wTeCheckenEilandVakken.pop();
						Vak[] buren = vakOpEilandRand.getBuren();
						for (int j = 0; j < buren.length; j++) {
							Vak buur = buren[j]; // de 4 buren u,r,d,l overlopen
							if (buur != null) { // geen muur
								if (wBlokken.contains(buur)) { // buur is een blok
									Vak blokKandidaat = buur.getBuren()[j]; // buur van de buur, in dezelfde richting j
									// todo
									wStartVierkantNanos = System.nanoTime();;
									boolean vierkantGedetecteerd = false;
									if (blokKandidaat != null && !blokKandidaat.isEindvak() && blokKandidaat.ligtOpEindvakPad()) {
										// geen eindvak, want op een eindvak mag een blok soms wel een vierkant vormen
										// ligt op eindvakPad, dus als er twee opeenvolgende buren een blokkade vormen, dan is één van beide buren != null en dus een blok
										Vak[] blokKandidaatBuren = blokKandidaat.getBuren();
										boolean[] blokBlokkade = { false, false, false, false };
										boolean[] muurBlokkade = { false, false, false, false };
										for (int k = (j + 3) % 4; k != (j + 2) % 4; k = (k + 1) % 4) { // enkel de buren in richting j-1, j en j+1 mod 4
											Vak blokkadeKandidaat = blokKandidaatBuren[k];
											if (blokkadeKandidaat == null) {
												blokBlokkade[k] = true;
											} else if (wBlokken.contains(blokkadeKandidaat)) {
												muurBlokkade[k] = true;
											}

											if (blokBlokkade[k] || muurBlokkade[k]) {
												if (k != (j + 3) % 4 && blokBlokkade[(k + 3) % 4] == true) { // dit is niet de buur in richting j-1
													// en de vorige buur (dus tegenkloksgewijs) is ook een blokBlokkade
													Vak hoekPuntGemeenschappelijk = wSpeler; // zomaar een vak dat != null is en geen blok bevat
													if (blokKandidaat.getBuren()[(k + 3) % 4] != null) {
														hoekPuntGemeenschappelijk = blokKandidaat.getBuren()[(k + 3) % 4].getBuren()[k];
													} else if (blokkadeKandidaat != null) {
														hoekPuntGemeenschappelijk = blokkadeKandidaat.getBuren()[(k + 3) % 4];
													} else {
														// mag nooit bereikt worden
														System.out.println("ERROR: blokkade");
													}
													if (hoekPuntGemeenschappelijk == null || wBlokken.contains(hoekPuntGemeenschappelijk)) {
														vierkantGedetecteerd = true;
													}
												}
											}
										}
									}
									wTotalVierkantNanos = wTotalVierkantNanos + (System.nanoTime() - wStartVierkantNanos);

									// todo
									if (vierkantGedetecteerd) {
										aantalVierkanten = aantalVierkanten + 1;
										// niks 
									} else if (blokKandidaat != null && blokKandidaat.ligtOpEindvakPad() && !wBlokken.contains(blokKandidaat)) {
										// de blok op "buur" een stap verder duwen
										ArrayList<Vak> blokkenNew = (ArrayList<Vak>) wBlokken.clone(); // kopie van lijst, maar wel dezelfde elementen
										blokkenNew.set(wBlokken.indexOf(buur), blokKandidaat); // blok verschuiven
										//
										//int hashC = wHuidigeConfiguratie.hashC;
										//int hashR = wHuidigeConfiguratie.hashR;
										int hashValue = wHuidigeConfiguratie.getHash();  // hashValue = hashC + hashR*hashBase + hashEiland*hashBase^2
										switch (j) { // j is de richting van de blokverplaatsing
										case 0:
											//hashR = hashR - 1;
											hashValue = hashValue - spel.getHashBase();
											break;
										case 1:
											//hashC = hashC + 1;
											hashValue = hashValue + 1;
											break;
										case 2:
											//hashR = hashR + 1;
											hashValue = hashValue + spel.getHashBase();
											break;
										case 3:
											//hashC = hashC - 1;
											hashValue = hashValue - 1;
											break;
										default:
											break;
										}									

										Configuratie configuratieNew = new Configuratie(buur, blokkenNew, wHuidigeConfiguratie, hashValue);

										// temp:
										// System.out.println("zoekboomNode: " + configuratieNew.zoekboomNode);
										wBlad = wBlad + 1;

										//
										wKandidaatBladConfiguratieFIFO.add(configuratieNew);
									}
								} else if (!wSpelerEiland.contains(buur)) { // buur is geen blok en geen muur
									wSpelerEiland.add(buur);
									if (!wTeCheckenEilandVakken.contains(buur)) {
										wTeCheckenEilandVakken.push(buur);
									}
								} else {
									// reeds in eiland: ok
								}
							}
						}
					}

					// detecteren of wHuidigeConfiguratie reeds in wHistoriek zit
					// optimaliseren door een functie te gebruiken die een waarde geeft op basis van de posities van de blokken
					// dit om sneller te kunnen failen en dus minder te moeten checken
					// voorstel voor implementatie: totaal verticaal en totaal horizontaal van de blokken cumulatief bijhouden in Configuratie
					boolean wReedsInHistoriek = false;
					wHuidigeConfiguratie.setEilandHash(spel.getHashBase(), wSpelerEiland.size());
					wHashHuidigeConfiguratie = wHuidigeConfiguratie.getHash();

					if (wHistoriek.containsKey(wHashHuidigeConfiguratie)) {
						for (Configuratie configuratieHistoric : (ArrayList<Configuratie>) wHistoriek.get(wHashHuidigeConfiguratie)) { // doorloop alle configuraties met dezelfde waarde voor wHashHuidigeConfiguratie
							if (configuratieHistoric.equals(wHuidigeConfiguratie, wSpelerEiland)) {
								wReedsInHistoriek = true;
								aantalWortelConfiguratiesReedsInHistoriek = aantalWortelConfiguratiesReedsInHistoriek + 1;
								break;
							}
						}
					}

					if (!wReedsInHistoriek) {
						if (!wHistoriek.containsKey(wHashHuidigeConfiguratie)) {
							wHistoriek.put(wHashHuidigeConfiguratie, new ArrayList<Configuratie>());
						}
						((ArrayList<Configuratie>) wHistoriek.get(wHashHuidigeConfiguratie)).add(wHuidigeConfiguratie);
						// temp:
						// System.out.println("huidige configuratie toegevoegd aan historiek");

						// check of oplossingGevonden
						oplossingGevonden = false;

						// check de historiek van de andere zoekboom
						if (tHistoriek.containsKey(wHashHuidigeConfiguratie)) {
							for (Configuratie configuratieHistoric : (ArrayList<Configuratie>) tHistoriek.get(wHashHuidigeConfiguratie)) { // doorloop alle configuraties met dezelfde waarde voor whashConfiguratieNew
								if (configuratieHistoric.equals(wHuidigeConfiguratie, wSpelerEiland)) {// opgepast: "equals" is niet symmetrisch
									oplossingGevonden = true;
									wortelOplossingConfiguratie = wHuidigeConfiguratie;
									topOplossingConfiguratie = configuratieHistoric;
									break;
								}
							}
						}

						if (oplossingGevonden) {
							// temp:
							wDiepte = wDiepte + 1;
							System.out.println("DIEPTE: " + wDiepte);
							System.out.println("wortelAantalConfiguratiesVoorHuidigeDiepte: " + i); // de oplossing niet meegeteld

							System.out.println("OPLOSSING !!!");
							return;
						}

						int aantalKandidaatBladConfiguraties = wKandidaatBladConfiguratieFIFO.size();
						for (int j = 0; j < aantalKandidaatBladConfiguraties; j++) {
							// temp:
							//System.out.println("kandidaatBladConfiguratieFIFO.size(): " + kandidaatBladConfiguratieFIFO.size());

							Configuratie configuratieNew = wKandidaatBladConfiguratieFIFO.removeFirst();
							wBladConfiguratiesFIFO.add(configuratieNew);

							// temp:
							// System.out.println("zoekboomNode: " + configuratieNew.zoekboomNode);

							wAantalConfiguratiesOpVolgendeDiepte = wAantalConfiguratiesOpVolgendeDiepte + 1; // counter verhogen
						}


					} else {
						// temp:
						// System.out.println("huidigeConfiguratie is reeds in historiek");
						wLoops = wLoops + 1;
					}
				}
				
				wDiepte = wDiepte + 1;
				wAantalConfiguraties_PerDiepte.add(wDiepte, wAantalConfiguratiesOpVolgendeDiepte);
				
				wTotalNanos = wTotalNanos + (System.nanoTime() - wStartNanos);

			} else{
				tAantalConfiguratiesVoorHuidigeDiepte = tAantalConfiguratiesOpVolgendeDiepte;
				tAantalConfiguratiesOpVolgendeDiepte = 0; // counter resetten

				System.out.println("topAantalConfiguratiesVoorHuidigeDiepte: " + tAantalConfiguratiesVoorHuidigeDiepte);
				tStartNanos = System.nanoTime();
				
				//
				// --top
				//
				for (int i = 0; i < tAantalConfiguratiesVoorHuidigeDiepte; i++) {

					ArrayList<Vak> tSpelerEiland = new ArrayList<Vak>();
					Stack<Vak> tTeCheckenEilandVakken = new Stack<Vak>(); // vakken op de rand van het huidig onvoltooid eiland

					Configuratie tHuidigeConfiguratie = tBladConfiguratiesFIFO.removeFirst();
					ArrayList<Vak> tBlokken = tHuidigeConfiguratie.getBlokken();
					Vak tSpeler = tHuidigeConfiguratie.getSpeler();
					tSpelerEiland.add(tSpeler);
					tTeCheckenEilandVakken.push(tSpeler);

					LinkedList<Configuratie> tKandidaatBladConfiguratieFIFO = new LinkedList<Configuratie>();

					// temp:				
					//String tZoekboomNode = tHuidigeConfiguratie.zoekboomNode;
					// System.out.println("tHuidigeConfiguratie " + zoekboomNode + " (" + i + ") " + " heeft bladconfiguraties:");
					int tBlad = 0;

					// bereken eiland van speler en maak ondertussen nieuwe bladConfiguraties voor tHuidigeConfiguratie
					while (!tTeCheckenEilandVakken.isEmpty()) {
						Vak vakOpEilandRand = tTeCheckenEilandVakken.pop();
						Vak[] buren = vakOpEilandRand.getBuren();
						for (int j = 0; j < buren.length; j++) {
							Vak buur = buren[j]; // de 4 buren u,r,d,l overlopen
							if (buur != null) {
								if (tBlokken.contains(buur)) {
									Vak spelerKandidaat = buren[(j+2) % 4];

									// todo
									if (spelerKandidaat != null && !tBlokken.contains(spelerKandidaat) && vakOpEilandRand.ligtOpEindvakPad()) {
										// de blok op "buur" een stap terug trekken
										ArrayList<Vak> blokkenNew = (ArrayList<Vak>) tBlokken.clone(); // kopie van lijst, maar wel dezelfde elementen
										blokkenNew.set(tBlokken.indexOf(buur), vakOpEilandRand); // blok verschuiven
										//
										//int hashC = tHuidigeConfiguratie.hashC;
										//int hashR = tHuidigeConfiguratie.hashR;
										int hashValue = tHuidigeConfiguratie.getHash();  // hashValue = hashC + hashR*hashBase + hashEiland*hashBase^2
										switch ((j+2) % 4) { // (j+2) % 4 is de richting van de blokverplaatsing
										case 0:
											//hashR = hashR - 1;
											hashValue = hashValue - spel.getHashBase();
											break;
										case 1:
											//hashC = hashC + 1;
											hashValue = hashValue + 1;
											break;
										case 2:
											//hashR = hashR + 1;
											hashValue = hashValue + spel.getHashBase();
											break;
										case 3:
											//hashC = hashC - 1;
											hashValue = hashValue - 1;
											break;
										default:
											break;
										}									

										Configuratie configuratieNew = new Configuratie(spelerKandidaat, blokkenNew, tHuidigeConfiguratie, hashValue);

										// temp:
										// System.out.println("zoekboomNode: " + configuratieNew.zoekboomNode);
										tBlad = tBlad + 1;

										//
										tKandidaatBladConfiguratieFIFO.add(configuratieNew);
									}
								} else if (!tSpelerEiland.contains(buur)) {
									tSpelerEiland.add(buur);
									if (!tTeCheckenEilandVakken.contains(buur)) {
										tTeCheckenEilandVakken.push(buur);
									}
								} else {
									// reeds in eiland: ok
								}
							}
						}
					}

					// detecteren of tHuidigeConfiguratie reeds in tHistoriek zit
					// optimaliseren door een functie te gebruiken die een waarde geeft op basis van de posities van de blokken
					// dit om sneller te kunnen failen en dus minder te moeten checken
					// voorstel voor implementatie: totaal verticaal en totaal horizontaal van de blokken cumulatief bijhouden in Configuratie
					boolean tReedsInHistoriek = false;
					tHuidigeConfiguratie.setEilandHash(spel.getHashBase(), tSpelerEiland.size());					
					tHashHuidigeConfiguratie = tHuidigeConfiguratie.getHash();

					if (tHistoriek.containsKey(tHashHuidigeConfiguratie)) {
						for (Configuratie configuratieHistoric : (ArrayList<Configuratie>) tHistoriek.get(tHashHuidigeConfiguratie)) { // doorloop alle configuraties met dezelfde waarde voor tHashHuidigeConfiguratie
							if (configuratieHistoric.equals(tHuidigeConfiguratie, tSpelerEiland)) {
								tReedsInHistoriek = true;
								aantalTopConfiguratiesReedsInHistoriek = aantalTopConfiguratiesReedsInHistoriek + 1;
								break;
							}
						}
					}

					if (!tReedsInHistoriek) {
						if (!tHistoriek.containsKey(tHashHuidigeConfiguratie)) {
							tHistoriek.put(tHashHuidigeConfiguratie, new ArrayList<Configuratie>());
						}
						((ArrayList<Configuratie>) tHistoriek.get(tHashHuidigeConfiguratie)).add(tHuidigeConfiguratie);
						// temp:
						// System.out.println("huidige configuratie toegevoegd aan historiek");

						// check of oplossingGevonden
						oplossingGevonden = false;

						// check de historiek van de andere zoekboom
						if (wHistoriek.containsKey(tHashHuidigeConfiguratie)) {
							for (Configuratie configuratieHistoric : (ArrayList<Configuratie>) wHistoriek.get(tHashHuidigeConfiguratie)) { // doorloop alle configuraties met dezelfde waarde voor thashConfiguratieNew
								if (configuratieHistoric.equals(tHuidigeConfiguratie, tSpelerEiland)) {// opgepast: "equals" is niet symmetrisch
									oplossingGevonden = true;
									wortelOplossingConfiguratie = configuratieHistoric;
									topOplossingConfiguratie = tHuidigeConfiguratie;
									break;
								}
							}
						}

						if (oplossingGevonden) {
							// temp:
							wDiepte = wDiepte + 1;
							System.out.println("DIEPTE: " + wDiepte);
							System.out.println("topAantalConfiguratiesVoorHuidigeDiepte: " + i); // de oplossing niet meegeteld

							System.out.println("OPLOSSING !!!");
							return;
						}

						int aantalKandidaatBladConfiguraties = tKandidaatBladConfiguratieFIFO.size();
						for (int j = 0; j < aantalKandidaatBladConfiguraties; j++) {
							// temp:
							//System.out.println("kandidaatBladConfiguratieFIFO.size(): " + kandidaatBladConfiguratieFIFO.size());

							Configuratie configuratieNew = tKandidaatBladConfiguratieFIFO.removeFirst();
							tBladConfiguratiesFIFO.add(configuratieNew);

							// temp:
							// System.out.println("zoekboomNode: " + configuratieNew.zoekboomNode);

							tAantalConfiguratiesOpVolgendeDiepte = tAantalConfiguratiesOpVolgendeDiepte + 1; // counter verhogen
						}


					} else {
						// temp:
						// System.out.println("huidigeConfiguratie is reeds in historiek");
						tLoops = tLoops + 1;
					}

				}

				tDiepte = tDiepte + 1;
				tAantalConfiguraties_PerDiepte.add(tDiepte, tAantalConfiguratiesOpVolgendeDiepte);
				
				tTotalNanos = tTotalNanos + (System.nanoTime() - tStartNanos);
			}			
		}
		
		System.out.println("Geen oplossing gevonden in " + maxRuntime + " seconden.");

	}
	// als aantal bladVelden > GRENS of boom_diepte > GRENS, doe dan extra checks om bladvelden te elimineren;
	// bijvoorbeeld:
	// - checken of alle blokken wel op een eindveldpad liggen (wordt altijd gecheckt, dus deze commentaar is overbodig)
	// - checken of er een toewijzing van blokken aan eindvelden bestaat (volgens duivenhokprincipe)

}
