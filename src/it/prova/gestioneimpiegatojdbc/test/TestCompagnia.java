package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.connection.MyConnection;
import it.prova.gestioneimpiegatojdbc.dao.Constants;
import it.prova.gestioneimpiegatojdbc.compagnia.CompagniaDAO;
import it.prova.gestioneimpiegatojdbc.compagnia.CompagniaDAOImpl;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class TestCompagnia {
	public static void main(String[] args) {
		CompagniaDAO compagniaDAO = null;

		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			compagniaDAO = new CompagniaDAOImpl(connection);

			testListCompagnia(compagniaDAO);

			testGetCompagnia(compagniaDAO);

			testUpdateCompagnia(compagniaDAO);

			System.out.println("size elenco compagnia prima dell'insert: " + compagniaDAO.list().size());
			testInsertCompagnia(compagniaDAO);
			System.out.println("size elenco compagnia dopo l'insert: " + compagniaDAO.list().size());

			testDeleteCompagnia(compagniaDAO);
			System.out.println("size elenco compagnia dopo delete: " + compagniaDAO.list().size());

			testFindByExample(compagniaDAO);

			testFindAllByDataAssunzioneMaggioreDi(compagniaDAO);

			testFindfindAllByRagioneSocialeContiene(compagniaDAO);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testListCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testListCompagnia------------");
		
		List<Compagnia> result = compagniaDAOInstance.list();
		for (Compagnia compagnia : result) {
			System.out.println(compagnia);
		}

		System.out.println("----------testListCompagnia PASSED---------");
	}

	private static void testGetCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testGetCompagnia--------------");

		if(compagniaDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione non riuscita, elenco vuoto");
		
		Long idInput = compagniaDAOInstance.list().get(0).getId();
		System.out.println(compagniaDAOInstance.get(idInput));

		System.out.println("----------testGetCompagnia PASSED---------");
	}

	private static void testUpdateCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testUpdateCompagnia-----------");

		Compagnia compagniaDaAggiornare = compagniaDAOInstance.list().get(0);
		Compagnia compagniaAggiornata = new Compagnia(compagniaDaAggiornare.getId(), "Compagnia1", 1200,
				new SimpleDateFormat("yyyy/MM/dd").parse("1990/02/12"));

		if (compagniaDAOInstance.update(compagniaAggiornata) != 1)
			throw new RuntimeException("operazione non riuscita 0 rowsAffected");

		if (compagniaDaAggiornare.getRagioneSociale().equals(compagniaAggiornata.getRagioneSociale())
				&& compagniaDaAggiornare.getFatturatoAnnuo() == compagniaAggiornata.getFatturatoAnnuo()
				&& compagniaDaAggiornare.getDataFondazione().equals(compagniaAggiornata.getDataFondazione()))
			throw new RuntimeException("operazione non riuscita, i valori non corrispondono");

		System.out.println(compagniaDAOInstance.list().get(0));

		System.out.println("----------testUpdateCompagnia PASSED-----------");
	}

	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testInsertCompagnia-----------");

		Compagnia compagniaDaInserire = new Compagnia("compagnia2", 180000,
				new SimpleDateFormat("yyyy/MM/dd").parse("1999/04/21"));

		if (compagniaDAOInstance.insert(compagniaDaInserire) != 1)
			throw new RuntimeException("operazione non riuscita, nessun elemento inserito");

		System.out.println("----------testInsertCompagnia PASSED-----------");
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testDeleteCompagnia-----------");

		compagniaDAOInstance
				.insert(new Compagnia("compagniaDelete", 10, new SimpleDateFormat("yyyy/MM/dd").parse("2002/01/01")));

		if (compagniaDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione eliminazione non effettuare, elenco vuoto");

		Compagnia compagniaDaEliminare = compagniaDAOInstance.list().get(compagniaDAOInstance.list().size() - 1);

		if (compagniaDAOInstance.delete(compagniaDaEliminare) != 1)
			throw new RuntimeException("operazione non riuscita nessun elemento eliminato");

		System.out.println("----------testDeleteCompagnia PASSED-----------");
	}

	private static void testFindByExample(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testFindByExample-----------");

		Compagnia compagniaExample = new Compagnia();
		compagniaExample.setRagioneSociale("compagnia1");
		compagniaExample.setFatturatoAnnuo(1000);
		List<Compagnia> result = compagniaDAOInstance.findByExample(compagniaExample);
		for (Compagnia compagnia : result) {
			System.out.println(compagnia);
		}

		System.out.println("----------testFindByExample PASSED-----------");
	}

	private static void testFindAllByDataAssunzioneMaggioreDi(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testFindAllByDataAssunzioneMaggioreDi-----------");

		Date date = new SimpleDateFormat("yyyy/MM/dd").parse("1990/01/01");
		List<Impiegato> result = compagniaDAOInstance.findAllByDataAssunzioneMaggioreDi(date);
		for (Impiegato impiegato : result) {
			System.out.println(impiegato);
		}

		System.out.println("----------testFindAllByDataAssunzioneMaggioreDi PASSED-----------");
	}

	private static void testFindfindAllByRagioneSocialeContiene(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("----------testFindfindAllByRagioneSocialeContiene-----------");

		if (compagniaDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione fallita, database vuoto");

		String input = "c";

		List<Compagnia> result = compagniaDAOInstance.findAllByRagioneSocialeContiene(input);
		for (Compagnia compagnia : result) {
			System.out.println(compagnia);
		}

		System.out.println("----------testFindfindAllByRagioneSocialeContiene PASSED-----------");
	}
}