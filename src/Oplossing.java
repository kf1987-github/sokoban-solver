import java.util.ArrayList;

public class Oplossing {

	ArrayList<Configuratie> configuratiesPad = new ArrayList<Configuratie>();
	ArrayList<Vak> spelerPad = new ArrayList<Vak>();
	ArrayList<Integer> stappenPad = new ArrayList<Integer>();
	
	private ArrayList<Configuratie> wortelConfiguratiesPad = new ArrayList<Configuratie>(); // pad vanaf wortelConfiguratie
	private ArrayList<Configuratie> topConfiguratiesPad = new ArrayList<Configuratie>(); // pad naar topConfiguratie
		
	public Oplossing(Configuratie wortelOplossingConfiguratie, Configuratie topOplossingConfiguratie) {
		berekenConfiguratiesPad(wortelOplossingConfiguratie, topOplossingConfiguratie);
		spelerPad.add(configuratiesPad.get(0).getSpeler());	// beginpositie
		berekenOplossingsStappen();
	}

	// bereken configuratiesPad van wortel naar oplossing
	private void berekenConfiguratiesPad(Configuratie wortelOplossingConfiguratie, Configuratie topOplossingConfiguratie) {
		berekenVanafWortelConfiguratiesPad(wortelOplossingConfiguratie);
		berekenNaarTopConfiguratiesPad(topOplossingConfiguratie);
		
		for(int i = 0; i < wortelConfiguratiesPad.size(); i++){
			configuratiesPad.add(wortelConfiguratiesPad.get(i));
		}
		// wortelConfiguratiesPad.get(wortelConfiguratiesPad.size()-1) is dezelfde configuratie als topConfiguratiesPad.get(0),
		// maar de speler kan zich op een andere positie binnen hetzelfde spelereiland bevinden
		for(int j = 0; j < topConfiguratiesPad.size(); j++){
			configuratiesPad.add(topConfiguratiesPad.get(j));
		}		
	}
	
	// bereken configuratiesPad van wortelConfiguratie naar bladConfiguratie
	private void berekenVanafWortelConfiguratiesPad(Configuratie bladConfiguratie) {
		if (bladConfiguratie != null) {
			berekenVanafWortelConfiguratiesPad(bladConfiguratie.getVorig());
			wortelConfiguratiesPad.add(bladConfiguratie);
		}
	}
	
	// bereken configuratiesPad van bladConfiguratie naar topConfiguratie
	private void berekenNaarTopConfiguratiesPad(Configuratie bladConfiguratie) {
		if (bladConfiguratie != null) {
			topConfiguratiesPad.add(bladConfiguratie);
			berekenNaarTopConfiguratiesPad(bladConfiguratie.getVorig());
		}
	}

	public void printBlokkenPad() {
		for (int j = 0; j < configuratiesPad.size(); j++) {
			Configuratie configuratie = configuratiesPad.get(j);
			ArrayList<Vak> blokken = configuratie.getBlokken();
			for (int i = 0; i < blokken.size(); i++) {
				Vak blok = blokken.get(i);
				System.out.print("(" + blok.getRow() + "," + blok.getCol() + ") ");
			}
			//
			System.out.print("\t speler: ");
			System.out.print("(" + configuratie.getSpeler().getRow() + "," + configuratie.getSpeler().getCol() + ") ");
			
			System.out.println("");
		}
	}
	
	public void printOplossingsStappen(){
		for (int i = 0; i < stappenPad.size(); i++) {
			switch (stappenPad.get(i)) {
			case 0:
				System.out.print("u");
				break;
			case 1:
				System.out.print("r");
				break;
			case 2:
				System.out.print("d");
				break;
			case 3:
				System.out.print("l");
				break;
			default:
				break;
			}
		}
		System.out.println("");
	}
	
	private void berekenOplossingsStappen() {	
		berekenWortelOplossingsStappen();		
		berekenWortelNaarTopOplossingsStappen();	// pad van laatste wortelconfiguratie naar eerste topConfiguratie berekenen
		berekenTopOplossingsStappen();
	}

	private void berekenWortelOplossingsStappen() {		
		Configuratie huidigeConfiguratie = configuratiesPad.get(0);
		
		//
//		ArrayList<Vak> tempBlokken = huidigeConfiguratie.getBlokken();
//		System.out.print("blokken: ");
//		for (int i = 0; i < tempBlokken.size(); i++) {
//			System.out.print("(" + tempBlokken.get(i).getRow() + "," + tempBlokken.get(i).getCol() + ") ");
//		}
//		System.out.println("");
		//
		Vak tempSpeler = huidigeConfiguratie.getSpeler();
//		System.out.print("speler: ");
//		System.out.println("(" + tempSpeler.getRow() + "," + tempSpeler.getCol() + ") ");
		
		Configuratie volgendeConfiguratie;
		Vak huidigeSpeler;
		Vak volgendeSpeler;
		ArrayList<Vak> volgendeBlokken;
		ArrayList<Vak> huidigeBlokken;
		int aantalConfiguraties = wortelConfiguratiesPad.size();
		for (int i = 1; i < aantalConfiguraties; i++) {	// enkel wortelOplossingsStappen, beginnen vanaf volgende configuratie
			//
//			System.out.println(i + ": ");
			
			volgendeConfiguratie = configuratiesPad.get(i);
			huidigeSpeler = huidigeConfiguratie.getSpeler();
			volgendeSpeler = volgendeConfiguratie.getSpeler();
			huidigeBlokken = huidigeConfiguratie.getBlokken();
			volgendeBlokken = volgendeConfiguratie.getBlokken();
			Vak volgendeBlok = null;
			for (int j = 0; j < volgendeBlokken.size(); j++) {
				volgendeBlok = volgendeBlokken.get(j);
				if (!huidigeBlokken.contains(volgendeBlok)) {
					break;
				}
			}
			//
//			System.out.println("volgendeBlok: (" + volgendeBlok.getRow() + "," + volgendeBlok.getCol() + ") ");
			
			Vak voorlaatsteVak = null;
			int laatsteStap = -1;	// default waarde, moet gewijzigd worden
			Vak[] buren = volgendeBlok.getBuren();
			for (int j = 0; j < 4; j++) {
				if (volgendeSpeler == buren[j]) { // per constructie van de configuraties (zie DubbeleZoekboom)
					voorlaatsteVak = volgendeSpeler.getBuren()[j];
					laatsteStap = (j + 2) % 4;
					break;
				}
			}
			//
//			System.out.println("voorlaatsteVak: (" + voorlaatsteVak.getRow() + "," + voorlaatsteVak.getCol() + ") ");
			
			// pad berekenen
			ArrayList<Vak> kortstePad =  huidigeConfiguratie.getKortstePad(voorlaatsteVak); // pad van voorlaatsteVak naar huidigeConfiguratie.getspeler(); deze beide vakken inclusief
			
			//
//			System.out.print("kortstePad: ");
//			for (int k = 0; k < kortstePad.size(); k++) {
//				System.out.print("(" + kortstePad.get(k).getRow() + "," + kortstePad.get(k).getCol() + ") ");
//			}
//			System.out.println("");
			
			int padLengte = kortstePad.size();
			Vak huidigKortstePadVak;
			Vak vorigKortstePadVak = spelerPad.get(spelerPad.size()-1);		// == kortstePad.get(padLengte - 1)
			
			//
//			System.out.print("vorigKortstePadVak: ");
//			System.out.println("(" + vorigKortstePadVak.getRow() + "," + vorigKortstePadVak.getCol() + ") ");

			
			Vak[] vorigeBuren;
			for (int j = 1; j < padLengte; j++) {
				vorigeBuren = vorigKortstePadVak.getBuren();				
				huidigKortstePadVak = kortstePad.get(padLengte-1-j);
				spelerPad.add(huidigKortstePadVak);
				// zoek de stap tussen vorigKortstePadVak en huidigKortstePadVak
				for (int k = 0; k < vorigeBuren.length; k++) {
					if (huidigKortstePadVak == vorigeBuren[k]) { // per constructie van de configuraties (zie DubbeleZoekboom)
						stappenPad.add(k);
						//
//						System.out.print("(" + vorigKortstePadVak.getRow() + "," + vorigKortstePadVak.getCol() + ") ");
//						switch (k) {
//						case 0:
//							System.out.print("u");
//							break;
//						case 1:
//							System.out.print("r");
//							break;
//						case 2:
//							System.out.print("d");
//							break;
//						case 3:
//							System.out.print("l");
//							break;
//						default:
//							break;
//						}
//						System.out.println(" (" + huidigKortstePadVak.getRow() + "," + huidigKortstePadVak.getCol() + ")");
						break;
					}
				}
				vorigKortstePadVak = huidigKortstePadVak;
			}
			
			spelerPad.add(volgendeSpeler);
			stappenPad.add(laatsteStap);
			
			//
//			System.out.print("(" + voorlaatsteVak.getRow() + "," + voorlaatsteVak.getCol() + ") ");
//			switch (laatsteStap) {
//			case 0:
//				System.out.print("u");
//				break;
//			case 1:
//				System.out.print("r");
//				break;
//			case 2:
//				System.out.print("d");
//				break;
//			case 3:
//				System.out.print("l");
//				break;
//			default:
//				break;
//			}
//			System.out.println(" (" + volgendeSpeler.getRow() + "," + volgendeSpeler.getCol() + ")");
//			System.out.println("");			

			huidigeConfiguratie = volgendeConfiguratie;
		}
//		System.out.println("");
	}
	
	private void berekenWortelNaarTopOplossingsStappen(){
		Configuratie huidigeConfiguratie = configuratiesPad.get(wortelConfiguratiesPad.size()-1);
		Configuratie volgendeConfiguratie = configuratiesPad.get(wortelConfiguratiesPad.size());
		
		Vak huidigeSpeler = huidigeConfiguratie.getSpeler();
		
		// pad berekenen		
		ArrayList<Vak> kortstePad =  volgendeConfiguratie.getKortstePad(huidigeSpeler); // pad van huidigeSpeler naar volgendeConfiguratie.getspeler(); deze beide vakken inclusief
		
		int padLengte = kortstePad.size();
		Vak vorigKortstePadVak = huidigeSpeler;
		Vak huidigKortstePadVak;
		Vak[] vorigeBuren;
		for (int j = 1; j < padLengte; j++) {
			vorigeBuren = vorigKortstePadVak.getBuren();				
			huidigKortstePadVak = kortstePad.get(j);
			spelerPad.add(huidigKortstePadVak);
			// zoek de stap tussen vorigKortstePadVak en huidigKortstePadVak
			for (int k = 0; k < vorigeBuren.length; k++) {
				if (huidigKortstePadVak == vorigeBuren[k]) { // per constructie van de configuraties (zie DubbeleZoekboom)
					stappenPad.add(k);
					//
					//						System.out.print("(" + vorigKortstePadVak.getRow() + "," + vorigKortstePadVak.getCol() + ") ");
					//						switch (k) {
					//						case 0:
					//							System.out.print("u");
					//							break;
					//						case 1:
					//							System.out.print("r");
					//							break;
					//						case 2:
					//							System.out.print("d");
					//							break;
					//						case 3:
					//							System.out.print("l");
					//							break;
					//						default:
					//							break;
					//						}
					//						System.out.println(" (" + huidigKortstePadVak.getRow() + "," + huidigKortstePadVak.getCol() + ")");
					break;
				}
			}
			vorigKortstePadVak = huidigKortstePadVak;
		}

	}
	
	private void berekenTopOplossingsStappen() {
		// index van eerste element uit topConfiguratiesPad dat voorkomt in configuratiesPad,
		// dus wortelConfiguratiesPad.size() + 1
		Configuratie huidigeConfiguratie = configuratiesPad.get(wortelConfiguratiesPad.size());
		
		//
//		ArrayList<Vak> tempBlokken = huidigeConfiguratie.getBlokken();
//		System.out.print("blokken: ");
//		for (int i = 0; i < tempBlokken.size(); i++) {
//			System.out.print("(" + tempBlokken.get(i).getRow() + "," + tempBlokken.get(i).getCol() + ") ");
//		}
//		System.out.println("");
		//
		//Vak tempSpeler = huidigeConfiguratie.getSpeler();
//		System.out.print("speler: ");
//		System.out.println("(" + tempSpeler.getRow() + "," + tempSpeler.getCol() + ") ");
		
		Configuratie volgendeConfiguratie;
		Vak huidigeSpeler;
		Vak volgendeSpeler;
		ArrayList<Vak> volgendeBlokken;
		ArrayList<Vak> huidigeBlokken;
		int aantalPadVakken = wortelConfiguratiesPad.size(); // pad van laatste wortelconfiguratie naar eerste topConfiguratie is reeds berekend
		for (int i = aantalPadVakken + 1; i < configuratiesPad.size(); i++) {	// enkel topOplossingsStappen, beginnen vanaf volgende configuratie
			//
//			System.out.println(i + ": ");
			
			volgendeConfiguratie = configuratiesPad.get(i);			
			huidigeSpeler = huidigeConfiguratie.getSpeler();
			volgendeSpeler = volgendeConfiguratie.getSpeler();
			huidigeBlokken = huidigeConfiguratie.getBlokken();
			volgendeBlokken = volgendeConfiguratie.getBlokken();
			Vak volgendeBlok = null;
			Vak volgendePositieHuidigeSpeler = null;
			for (int j = 0; j < volgendeBlokken.size(); j++) {
				volgendeBlok = volgendeBlokken.get(j);
				if (!huidigeBlokken.contains(volgendeBlok)) {
					break;
				}
			}
			//
//			System.out.println("volgendeBlok: (" + volgendeBlok.getRow() + "," + volgendeBlok.getCol() + ") ");
			
			//Vak tweedeVak = volgendeBlok;
			int eersteStap = -1;	// default waarde, moet gewijzigd worden
			Vak[] buren = volgendeBlok.getBuren();
			for (int j = 0; j < 4; j++) {
				Vak[] huidigeSpelerBuren = huidigeSpeler.getBuren();
				for (int k = 0; k < huidigeSpelerBuren.length; k++) {
					if (huidigeSpelerBuren[k] != null && huidigeSpelerBuren[k] == buren[j]) { // per constructie van de configuraties (zie DubbeleZoekboom)
						volgendePositieHuidigeSpeler = huidigeSpelerBuren[k];
						eersteStap = k;										
						break;
					}
				}
			}
			//
//			System.out.println("voorlaatsteVak: (" + voorlaatsteVak.getRow() + "," + voorlaatsteVak.getCol() + ") ");
			
			spelerPad.add(volgendePositieHuidigeSpeler);
			stappenPad.add(eersteStap);
			
			// pad berekenen
			ArrayList<Vak> kortstePad =  volgendeConfiguratie.getKortstePad(volgendePositieHuidigeSpeler); // pad van volgendePositieHuidigeSpeler naar volgendeConfiguratie.getspeler(); deze beide vakken inclusief
			
			//
//			System.out.print("kortstePad: ");
//			for (int k = 0; k < kortstePad.size(); k++) {
//				System.out.print("(" + kortstePad.get(k).getRow() + "," + kortstePad.get(k).getCol() + ") ");
//			}
//			System.out.println("");
			
			int padLengte = kortstePad.size();
			Vak huidigKortstePadVak;
			Vak vorigKortstePadVak = spelerPad.get(spelerPad.size()-1);		// == kortstePad.get(0) == volgendePositieHuidigeSpeler
			
			//
//			System.out.print("vorigKortstePadVak: ");
//			System.out.println("(" + vorigKortstePadVak.getRow() + "," + vorigKortstePadVak.getCol() + ") ");

			
			Vak[] vorigeBuren;
			for (int j = 1; j < padLengte; j++) {
				vorigeBuren = vorigKortstePadVak.getBuren();				
				huidigKortstePadVak = kortstePad.get(j);
				spelerPad.add(huidigKortstePadVak);
				// zoek de stap tussen vorigKortstePadVak en huidigKortstePadVak
				for (int k = 0; k < vorigeBuren.length; k++) {
					if (huidigKortstePadVak == vorigeBuren[k]) { // per constructie van de configuraties (zie DubbeleZoekboom)
						stappenPad.add(k);
						//
//						System.out.print("(" + vorigKortstePadVak.getRow() + "," + vorigKortstePadVak.getCol() + ") ");
//						switch (k) {
//						case 0:
//							System.out.print("u");
//							break;
//						case 1:
//							System.out.print("r");
//							break;
//						case 2:
//							System.out.print("d");
//							break;
//						case 3:
//							System.out.print("l");
//							break;
//						default:
//							break;
//						}
//						System.out.println(" (" + huidigKortstePadVak.getRow() + "," + huidigKortstePadVak.getCol() + ")");
						break;
					}
				}
				vorigKortstePadVak = huidigKortstePadVak;
			}
			
						
			//
//			System.out.print("(" + voorlaatsteVak.getRow() + "," + voorlaatsteVak.getCol() + ") ");
//			switch (laatsteStap) {
//			case 0:
//				System.out.print("u");
//				break;
//			case 1:
//				System.out.print("r");
//				break;
//			case 2:
//				System.out.print("d");
//				break;
//			case 3:
//				System.out.print("l");
//				break;
//			default:
//				break;
//			}
//			System.out.println(" (" + volgendeSpeler.getRow() + "," + volgendeSpeler.getCol() + ")");
//			System.out.println("");			

			huidigeConfiguratie = volgendeConfiguratie;
		}
//		System.out.println("");
	}
		
	
	
	
	
	
	
}
