package br.com.davi.projeto.conexao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private final String url = "jdbc:mysql://localhost:3306/byte_bank";
    private final String user = "root";
    private final String password = "Davi@12345";
    public Connection openConnection(){

        try {
            return createDataSource().getConnection();
        }
        catch(SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public HikariDataSource createDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
