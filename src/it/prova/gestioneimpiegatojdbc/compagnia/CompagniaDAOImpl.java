package it.prova.gestioneimpiegatojdbc.compagnia;

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

public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	public CompagniaDAOImpl() {

	}

	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Compagnia> list() throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni, connessione non stabilita");

		List<Compagnia> result = new ArrayList<>();
		Compagnia temp;
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from compagnia")) {

			while (resultSet.next()) {
				temp = new Compagnia();
				temp.setId(resultSet.getLong("id"));
				temp.setRagioneSociale(resultSet.getString("ragionesociale"));
				temp.setFatturatoAnnuo(resultSet.getInt("fatturatoannuo"));
				temp.setDataFondazione(resultSet.getDate("datafondazione"));
				result.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public Compagnia get(Long idInput) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazione sul DB, connnessione non stabilita");

		if (idInput == null || idInput < 1)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, input non valido");

		Compagnia result = new Compagnia();

		try (PreparedStatement preparedStatement = connection
				.prepareStatement("select * from compagnia where id = ?")) {
			preparedStatement.setLong(1, idInput);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					result.setId(resultSet.getLong("id"));
					result.setRagioneSociale(resultSet.getString("ragionesociale"));
					result.setDataFondazione(resultSet.getDate("datafondazione"));
					result.setFatturatoAnnuo(resultSet.getInt("fatturatoannuo"));
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
	public int update(Compagnia input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile efffettuare operazioni sul DB, connessione non stabilita");

		if (list().size() < 1)
			throw new RuntimeException("impossibile effettuare operazioni a DB vuoto");

		if (input == null)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, input non valido");

		int rowsAffected = 0;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"" + "update compagnia set ragionesociale = ?, fatturatoannuo = ?, datafondazione = ? where id = ?")) {
			preparedStatement.setString(1, input.getRagioneSociale());
			preparedStatement.setInt(2, input.getFatturatoAnnuo());
			preparedStatement.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			preparedStatement.setLong(4, input.getId());
			rowsAffected = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowsAffected;
	}

	@Override
	public int insert(Compagnia input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni sul DB, connessione non stabilita");

		if (input == null)
			throw new RuntimeException("impossobile effettuare operazioni sul DB, input non valido");

		int rowsAffected = 0;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"insert into compagnia (ragionesociale, fatturatoannuo, datafondazione) values (?,?,?)")) {
			preparedStatement.setString(1, input.getRagioneSociale());
			preparedStatement.setInt(2, input.getFatturatoAnnuo());
			preparedStatement.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			rowsAffected = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowsAffected;
	}

	@Override
	public int delete(Compagnia input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni sul DB, connessione non stabilita");

		if (list().size() < 1)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, DB vuoto");

		if (input == null)
			throw new RuntimeException("impossibile effettuare operazioni sul DB, input non valido");

		int rowsAffected = 0;

		try (PreparedStatement preparedStatement = connection.prepareStatement("delete from compagnia where id = ? ")) {
			preparedStatement.setLong(1, input.getId());
			rowsAffected = preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rowsAffected;
	}

	@Override
	public List<Compagnia> findByExample(Compagnia input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni, connessione non stabilita");

		String query = "select * from compagnia where 1=1 ";

		if (input == null)
			return list();

		if (input.getRagioneSociale() != null)
			query += " and ragionesociale like '" + input.getRagioneSociale() + "%' ";
		if (input.getFatturatoAnnuo() >= 0)
			query += " and fatturatoannuo > " + input.getFatturatoAnnuo();
		if (input.getDataFondazione() != null)
			query += " and datafondazione > " + input.getDataFondazione();

		List<Compagnia> result = new ArrayList<>();
		Compagnia temp;

		try (Statement statement = connection.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(query)) {
				while (resultSet.next()) {
					temp = new Compagnia();
					temp.setId(resultSet.getLong("id"));
					temp.setRagioneSociale(resultSet.getString("ragionesociale"));
					temp.setFatturatoAnnuo(resultSet.getInt("fatturatoannuo"));
					temp.setDataFondazione(resultSet.getDate("datafondazione"));
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
	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date date) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni, connessione non stabilita");

		List<Impiegato> result = new ArrayList<>();
		Impiegato temp;
		Compagnia tempCompagnia;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select * from compagnia c inner join impiegato i on c.id = i.compagnia_id where i.dataassunzione > ?")) {
			preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
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
	public List<Compagnia> findAllByRagioneSocialeContiene(String input) throws Exception {
		if (isNotActive())
			throw new RuntimeException("impossibile effettuare operazioni sul DB, connessione non stabilita");

		if (input == null)
			throw new RuntimeException("impossibile effettuare operazoini sul DB, input non valido");

		List<Compagnia> result = new ArrayList<>();
		Compagnia temp;

		try (PreparedStatement preparedStatement = connection
				.prepareStatement("select * from compagnia where ragionesociale like ?")) {
			preparedStatement.setString(1, input + "%");
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					temp = new Compagnia();
					temp.setId(resultSet.getLong("id"));
					temp.setRagioneSociale(resultSet.getString("ragionesociale"));
					temp.setFatturatoAnnuo(resultSet.getInt("fatturatoannuo"));
					temp.setDataFondazione(resultSet.getDate("datafondazione"));
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

}
