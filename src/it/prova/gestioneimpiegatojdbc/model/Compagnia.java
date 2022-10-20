package it.prova.gestioneimpiegatojdbc.model;

import java.util.Date;

public class Compagnia {
	private Long id;
	private String ragioneSociale;
	private int fatturatoAnnuo;
	private Date dataFondazione;
	private Impiegato[] impiegati;

	public Compagnia() {
	}

	public Compagnia(String ragioneSociale, int fatturatoAnnuo, Date dataFondazione) {
		this.ragioneSociale = ragioneSociale;
		this.fatturatoAnnuo = fatturatoAnnuo;
		this.dataFondazione = dataFondazione;
	}

	public Compagnia(Long id, String ragioneSociale, int fatturatoAnnuo, Date dataFondazione) {
		this.ragioneSociale = ragioneSociale;
		this.fatturatoAnnuo = fatturatoAnnuo;
		this.dataFondazione = dataFondazione;
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public int getFatturatoAnnuo() {
		return fatturatoAnnuo;
	}

	public void setFatturatoAnnuo(int fatturatoAnnuo) {
		this.fatturatoAnnuo = fatturatoAnnuo;
	}

	public Date getDataFondazione() {
		return dataFondazione;
	}

	public void setDataFondazione(Date dataFondazione) {
		this.dataFondazione = dataFondazione;
	}

	public Impiegato[] getImpiegati() {
		return impiegati;
	}

	public void setImpiegati(Impiegato[] impiegati) {
		this.impiegati = impiegati;
	}

	public String toString() {
		return "Compagnia: " + "[id: " + getId() + ", RagioneSociale: " + getRagioneSociale() + ", fatturatoAnnuo: "
				+ getFatturatoAnnuo() + ", dataFondazione: " + getDataFondazione() + "]";
	}
}
