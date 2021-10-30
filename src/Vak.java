
public class Vak {

	private int row;
	private int col;
	private Vak[] buren;
	private boolean ligtOpEindvakPad;		// blok kan hier passeren om naar eindveld te gaan
	private boolean eindvak;
	
	public Vak(int row, int col, boolean eindvak) {
		this.row = row;
		this.col = col;
		this.eindvak = eindvak;
		this.buren = new Vak[4];			// 0..4 is u,r,d,l
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setBoven(Vak buur) {
		this.buren[0] = buur;
	}
	
	public void setRechts(Vak buur) {
		this.buren[1] = buur;
	}
	
	public void setOnder(Vak buur) {
		this.buren[2] = buur;
	}
	
	public void setLinks(Vak buur) {
		this.buren[3] = buur;
	}
	
	public Vak[] getBuren(){
		return buren;
	}
	
	public Vak getBoven() {
		return buren[0];
	}
	
	public Vak getRechts() {
		return buren[1];
	}
	
	public Vak getOnder() {
		return buren[2];
	}
	
	public Vak getLinks() {
		return buren[3];
	}

	public boolean ligtOpEindvakPad() {
		return ligtOpEindvakPad;
	}

	public void setLigtOpEindvakPad(boolean ligtOpEindvakPad) {
		this.ligtOpEindvakPad = ligtOpEindvakPad;
	}

	public boolean isEindvak() {
		return eindvak;
	}

	

	

	
}
