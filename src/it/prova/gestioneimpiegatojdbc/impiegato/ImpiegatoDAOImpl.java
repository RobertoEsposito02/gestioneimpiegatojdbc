package it.prova.gestioneimpiegatojdbc.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.AbstractMySQLDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	public ImpiegatoDAOImpl() {
	}

	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Impiegato> list() throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni, connessione non stabilita");

		List<Impiegato> result = new ArrayList<>();
		Impiegato temp;
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from impiegato")) {

			while (resultSet.next()) {
				temp = new Impiegato();
				temp.setId(resultSet.getLong("id"));
				temp.setNome(resultSet.getString("nome"));
				temp.setCognome(resultSet.getString("cognome"));
				temp.setCodiceFiscale(resultSet.getString("codicefiscale"));
				temp.setDataNascita(resultSet.getDate("datanascita"));
				temp.setDataAssunzione(resultSet.getDate("dataassunzione"));
				result.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazione sul DB, connnessione non stabilita");

		if (idInput == null || idInput < 1)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, input non valido");

		Impiegato result = new Impiegato();

		try (PreparedStatement preparedStatement = connection
				.prepareStatement("select * from impiegato where id = ?")) {
			preparedStatement.setLong(1, idInput);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					result = new Impiegato();
					result.setId(resultSet.getLong("id"));
					result.setNome(resultSet.getString("nome"));
					result.setCognome(resultSet.getString("cognome"));
					result.setCodiceFiscale(resultSet.getString("codicefiscale"));
					result.setDataNascita(resultSet.getDate("datanascita"));
					result.setDataAssunzione(resultSet.getDate("dataassunzione"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int update(Impiegato input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile efffettuare operazioni sul DB, connessione non stabilita");

		if (list().size() < 1)
			throw new RuntimeException("impossibile effettuare operazioni a DB vuoto");

		if (input == null)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, input non valido");

		int rowsAffected = 0;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"" + "update impiegato set nome = ?, cognome = ?, codicefiscale = ?, datanascita = ?, "
						+ "dataassunzione = ? where id = ?")) {
			preparedStatement.setString(1, input.getNome());
			preparedStatement.setString(2, input.getCognome());
			preparedStatement.setString(3, input.getCodiceFiscale());
			preparedStatement.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			preparedStatement.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			preparedStatement.setLong(6, input.getId());
			rowsAffected = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowsAffected;
	}

	@Override
	public int insert(Impiegato input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni sul DB, connessione non stabilita");

		if (input == null)
			throw new RuntimeException("impossobile effettuare operazioni sul DB, input non valido");

		int rowsAffected = 0;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"insert into impiegato (nome, cognome, codicefiscale, datanascita, dataassunzione)"
				+ " values (?,?,?,?,?)")) {
			preparedStatement.setString(1, input.getNome());
			preparedStatement.setString(2, input.getCognome());
			preparedStatement.setString(3, input.getCodiceFiscale());
			preparedStatement.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			preparedStatement.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			rowsAffected = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowsAffected;
	}

	@Override
	public int delete(Impiegato input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni sul DB, connessione non stabilita");

		if (list().size() < 1)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, DB vuoto");

		if (input == null)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, input non valido");

		int rowsAffected = 0;

		try (PreparedStatement preparedStatement = connection.prepareStatement("delete from impiegato where id = ? ")) {
			preparedStatement.setLong(1, input.getId());
			rowsAffected = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowsAffected;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni, connessione non stabilita");

		String query = "select * from impiegato where 1=1 ";

		if (input == null)
			return list();

		if (input.getNome() != null)
			query += " and nome like '" + input.getNome() + "%' ";
		if (input.getCognome() != null)
			query += " and cognome like '" + input.getCognome() + "%' ";
		if (input.getCodiceFiscale() != null)
			query += " and codicefiscale like '" + input.getCodiceFiscale() + "%' ";
		if (input.getDataNascita() != null)
			query += " and datanascita > " + input.getDataNascita();
		if (input.getDataAssunzione() != null)
			query += " and dataassunzione > " + input.getDataAssunzione();

		List<Impiegato> result = new ArrayList<>();
		Impiegato temp;

		try (Statement statement = connection.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(query)) {
				while (resultSet.next()) {
					temp = new Impiegato();
					temp.setId(resultSet.getLong("id"));
					temp.setNome(resultSet.getString("nome"));
					temp.setCognome(resultSet.getString("cognome"));
					temp.setCodiceFiscale(resultSet.getString("codicefiscale"));
					temp.setDataNascita(resultSet.getDate("datanascita"));
					temp.setDataAssunzione(resultSet.getDate("dataassunzione"));
					result.add(temp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagnia) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni, connessione non stabilita");
		
		if (list().size() < 1)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, DB vuoto");

		Impiegato temp;
		Compagnia tempCompagnia;
		List<Impiegato> result = new ArrayList<>();
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(""
				+ "select * from impiegato i inner join compagnia c on i.compagnia_id = c.id where c.id = ? ")){
			preparedStatement.setLong(1, compagnia.getId());
			try (ResultSet resultSet = preparedStatement.executeQuery()){
				while(resultSet.next()) {
					tempCompagnia = new Compagnia();
					tempCompagnia.setRagioneSociale(resultSet.getString("ragioneSociale"));
					tempCompagnia.setFatturatoAnnuo(resultSet.getInt("fatturatoannuo"));
					tempCompagnia.setDataFondazione(resultSet.getDate("datafondazione"));
					tempCompagnia.setId(resultSet.getLong("c.id"));

					temp = new Impiegato();
					temp.setId(resultSet.getLong("i.id"));
					temp.setCodiceFiscale(resultSet.getString("codicefiscale"));
					temp.setNome(resultSet.getString("nome"));
					temp.setCognome(resultSet.getString("cognome"));
					temp.setDataAssunzione(resultSet.getDate("dataassunzione"));
					temp.setDataNascita(resultSet.getDate("datanascita"));
					temp.setCompagnia(tempCompagnia);
					result.add(temp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int countByDataFondazioneCompagniaGreaterThan(Date date) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Impiegato> findAllErroriAssunzione() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
