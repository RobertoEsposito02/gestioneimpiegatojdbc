package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.compagnia.CompagniaDAO;
import it.prova.gestioneimpiegatojdbc.compagnia.CompagniaDAOImpl;
import it.prova.gestioneimpiegatojdbc.connection.MyConnection;
import it.prova.gestioneimpiegatojdbc.dao.Constants;
import it.prova.gestioneimpiegatojdbc.impiegato.ImpiegatoDAO;
import it.prova.gestioneimpiegatojdbc.impiegato.ImpiegatoDAOImpl;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class TestImpiegati {
	public static void main(String[] args) {
		ImpiegatoDAO impiegatoDAO = null;

		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			impiegatoDAO = new ImpiegatoDAOImpl(connection);

			testListImpiegati(impiegatoDAO);

			testGetImpiegati(impiegatoDAO);

			testUpdateImpiegati(impiegatoDAO);

			System.out.println("size elenco prima insert: " + impiegatoDAO.list().size());
			testInsertImpiegati(impiegatoDAO);
			System.out.println("size elenco dopo insert: " + impiegatoDAO.list().size());

			testDeleteImpiegati(impiegatoDAO);
			
			testFindByExample(impiegatoDAO);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testListImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("----------testListImpiegati------------");

		List<Impiegato> result = impiegatoDAOInstance.list();
		for (Impiegato impiegato : result) {
			System.out.println(impiegato);
		}

		System.out.println("----------testListImpiegati PASSED------------");
	}

	private static void testGetImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("----------testGetImpiegati------------");

		if (impiegatoDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione non riuscita, elenco vuoto");

		Long idInput = impiegatoDAOInstance.list().get(0).getId();
		System.out.println(impiegatoDAOInstance.get(idInput));

		System.out.println("----------testGetImpiegati PASSED------------");
	}

	private static void testUpdateImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("----------testUpdateImpiegati------------");

		if(impiegatoDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione non riuscita DB vuoto");
		
		Impiegato impiegatoDaAggiungereSuCuiFareUpdate = new Impiegato("luigi", "bianchi", "codiceFiscaleLuigiBianchi",
				new SimpleDateFormat("yyyy/MM/dd").parse("1950/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("1999/01/01"));
		impiegatoDAOInstance.insert(impiegatoDaAggiungereSuCuiFareUpdate);

		Impiegato impiegatoUpdate = new Impiegato(impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size()-1).getId(),
				"gigi", "verdi", "codiceFiscaleGigiVerdi",
				new SimpleDateFormat("yyyy/MM/dd").parse("1951/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("2000/01/01"));

		if (impiegatoDAOInstance.update(impiegatoUpdate) != 1)
			throw new RuntimeException("operazione non riuscita 0 rowsAffected");
		Impiegato ultimoDellaLista = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1);

		if (ultimoDellaLista.getNome().equals(impiegatoDaAggiungereSuCuiFareUpdate.getNome())
				|| ultimoDellaLista.getCognome().equals(impiegatoDaAggiungereSuCuiFareUpdate.getCognome())
				|| ultimoDellaLista.getCodiceFiscale().equals(impiegatoDaAggiungereSuCuiFareUpdate.getCodiceFiscale())
				|| ultimoDellaLista.getDataAssunzione().equals(impiegatoDaAggiungereSuCuiFareUpdate.getDataAssunzione())
				|| ultimoDellaLista.getDataNascita().equals(impiegatoDaAggiungereSuCuiFareUpdate.getDataNascita()))
			throw new RuntimeException("operazione non riuscita i valori non sono cambiati");

		System.out.println("----------testUpdateImpiegati PASSED------------");
	}

	private static void testInsertImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("----------testInsertImpiegati------------");

		Impiegato impiegatoDaInserire = new Impiegato("luigi", "bianchi", "codiceFiscaleLuigiBianchi",
				new SimpleDateFormat("yyyy/MM/dd").parse("1950/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("1999/01/01"));

		if (impiegatoDAOInstance.insert(impiegatoDaInserire) != 1)
			throw new RuntimeException("operazione non riuscita, nessun elemento inserito");

		System.out.println("----------testInsertImpiegati PASSED------------");
	}
	
	private static void testDeleteImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("----------testDeleteImpiegati------------");
		
		impiegatoDAOInstance.insert(new Impiegato("luigi", "bianchi", "codiceFiscaleLuigiBianchi",
				new SimpleDateFormat("yyyy/MM/dd").parse("1950/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("1999/01/01")));
		
		if (impiegatoDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione eliminazione non effettuare, elenco vuoto");
		
		Impiegato impiegatoDaEliminare = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() -1);
		
		if(impiegatoDAOInstance.delete(impiegatoDaEliminare) != 1)
			throw new RuntimeException("operazione non riuscita nessun elemento eliminato");
		
		System.out.println("----------testDeleteImpiegati PASSED------------");
	}
	
	private static void testFindByExample(ImpiegatoDAO impiegatoDAOInstance) throws Exception{
		System.out.println("----------testFindByExample------------");
		
		Impiegato impiegatoExample = new Impiegato();
		impiegatoExample.setNome("luigi");
		impiegatoExample.setCodiceFiscale("codiceFiscaleLuigiBianchi");
		List<Impiegato> result = impiegatoDAOInstance.findByExample(impiegatoExample);
		for (Impiegato impiegato : result) {
			System.out.println(impiegato);
		}
		System.out.println("----------testFindByExample PASSED------------");
	}
}
