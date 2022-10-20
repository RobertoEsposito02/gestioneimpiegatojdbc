package it.prova.gestioneimpiegatojdbc.dao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractMySQLDAO {

	protected Connection connection;

	public AbstractMySQLDAO() {
	}

	public AbstractMySQLDAO(Connection connection) {
		this.connection = connection;
	}

	protected boolean isNotActive() throws SQLException {
		try {
			return this.connection == null || this.connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
