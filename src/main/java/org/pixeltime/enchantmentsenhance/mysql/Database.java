package org.pixeltime.enchantmentsenhance.mysql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.pixeltime.enchantmentsenhance.Main;
import org.pixeltime.enchantmentsenhance.manager.SettingsManager;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class Database {

    private final String connectionUri;
    private final String username;
    private final String password;
    private Connection connection;

    public Database() throws ClassNotFoundException, SQLException {
        final String hostname = SettingsManager.config.getString("mysql.host");
        final int port = SettingsManager.config.getInt("mysql.port");
        final String database = SettingsManager.config.getString("mysql.database");

        connectionUri = String.format("jdbc:mysql://%s:%d/%s", hostname, port, database);
        username = SettingsManager.config.getString("mysql.user");
        password = SettingsManager.config.getString("mysql.password");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect();

        } catch (SQLException sqlException) {
            close();
            throw sqlException;
        }
    }

    private void connect() throws SQLException {
        if (connection != null) {
            try {
                connection.createStatement().execute("SELECT 1;");

            } catch (SQLException sqlException) {
                if (sqlException.getSQLState().equals("08S01")) {
                    try {
                        connection.close();

                    } catch (SQLException ignored) {
                    }
                }
            }
        }

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(connectionUri, username, password);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

        } catch (SQLException ignored) {

        }

        connection = null;
    }

    public boolean checkConnection() {
        try {
            connect();
        } catch (SQLException sqlException) {
            close();
            sqlException.printStackTrace();
            return false;
        }
        return true;
    }

    public void createTables() throws IOException, SQLException {
        URL resource = Resources.getResource(Main.class, "/tables.sql");
        String[] databaseStructure = Resources.toString(resource, Charsets.UTF_8).split(";");

        if (databaseStructure.length == 0) {
            return;
        }

        Statement statement = null;

        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            for (String query : databaseStructure) {
                query = query.trim();

                if (query.isEmpty()) {
                    continue;
                }

                statement.execute(query);
            }

            connection.commit();

        } finally {
            connection.setAutoCommit(true);

            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        }
    }

    public boolean doesPlayerExist(String fId) {
        if (!checkConnection()) {
            return false;
        }

        int count = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT Count(`player_id`) ");
            queryBuilder.append("FROM `sw_player` ");
            queryBuilder.append("WHERE `uuid` = ? ");
            queryBuilder.append("LIMIT 1;");

            preparedStatement = connection.prepareStatement(queryBuilder.toString());
            preparedStatement.setString(1, fId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();

        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (final SQLException ignored) {
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (final SQLException ignored) {
                }
            }
        }

        return count > 0;
    }

    public void createNewPlayer(String name) {
        if (!checkConnection()) {
            return;
        }

        PreparedStatement preparedStatement = null;

        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO `ee_userdata` ");
            queryBuilder.append("(`player_id`, `player_name`, `failstack`, `advice_of_valks`, `stone1`, `stone2`, `stone3`, `stone4`) ");
            queryBuilder.append("VALUES ");
            queryBuilder.append("(NULL, ?, 0, 0, 0, 0, 0, 0);");
            preparedStatement = connection.prepareStatement(queryBuilder.toString());
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "none");
            preparedStatement.setString(3, "none");
            preparedStatement.setString(4, "none");
            preparedStatement.setString(5, "none");
            preparedStatement.setString(6, "none");
            preparedStatement.setString(7, "none");

            preparedStatement.executeUpdate();

        } catch (final SQLException sqlException) {
            sqlException.printStackTrace();

        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (final SQLException ignored) {
                }
            }
        }
    }

}