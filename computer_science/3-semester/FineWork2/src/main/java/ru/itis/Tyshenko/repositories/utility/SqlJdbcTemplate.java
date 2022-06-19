package ru.itis.Tyshenko.repositories.utility;

import java.sql.*;
import java.util.Optional;

public class SqlJdbcTemplate<T> {

    Connection connection;

    public SqlJdbcTemplate(Connection connection) {
        this.connection = connection;
    }

    public Optional<T> queryForObject(String sql, RowMapper<T> mapper, Object ... args) {
        try(ResultSet resultSet = addParametersInStatement(sql, args).executeQuery()) {
            if (resultSet.next()) {
                return  Optional.of(mapper.mapRow(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public void execute(String sql, Object ... args) {
        try (PreparedStatement statement = addParametersInStatement(sql, args)) {
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private PreparedStatement addParametersInStatement(String sql, Object[] args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }
        return statement;
    }
}
