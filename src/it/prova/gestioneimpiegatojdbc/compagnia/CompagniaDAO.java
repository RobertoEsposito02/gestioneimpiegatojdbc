package it.prova.gestioneimpiegatojdbc.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.IBaseDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public interface CompagniaDAO extends IBaseDAO<Compagnia>{
	
	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date date) throws Exception;
	public List<Compagnia> findAllByRagioneSocialeContiene(String input) throws Exception;
	
}
