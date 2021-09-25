package com.amisoft.tds.tradedatastore;

import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class TestMain {

    public static void main(String[] args) {

        try(Connection template = mock(Connection.class)){

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try(AutoCloseable template1 = mock(AutoCloseable.class)){

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
