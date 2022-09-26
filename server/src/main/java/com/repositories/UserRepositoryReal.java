package com.repositories;

import com.model.Chatroom;
import com.model.User;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Component
public class UserRepositoryReal implements UserRepository{

    JdbcTemplate jdbcTemplate;

    private final String CREATE_USERS = "CREATE TABLE IF NOT EXISTS server.user (\nid serial primary key,\nname varchar(40) not null unique,\npassword varchar(100) not null);";
    private final String GET_BY_ID = "SELECT * FROM server.user WHERE id = ?";
    private final String GET_ALL = "SELECT * FROM server.user";
    private final String ADD = "INSERT INTO server.user (name, password) VALUES (?, ?)";
    private final String UPDATE = "INSERT INTO server.user (name, password) VALUES (?, ?) WHERE id = ?";
    private final String DELETE = "DELETE FROM server.user WHERE id = ?";
    private final String GET_BY_NAME = "SELECT * FROM server.user WHERE name = ?";
    private final String DELETE_BY_NAME = "DELETE FROM server.user WHERE name = ?";

    @Autowired
    public UserRepositoryReal(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
        startThis();
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("id"), rs.getString("name"), rs.getString("password"));
        }
    }

    private void startThis() {
        //jdbcTemplate.execute("DROP  TABLE server.user;");
        jdbcTemplate.execute(CREATE_SERVER);
        jdbcTemplate.execute(CREATE_USERS);
    }

    @Override
    public User findById(int id) {
        return jdbcTemplate.query(GET_BY_ID, new Object[]{id}, new int[]{Types.INTEGER}, new UserRowMapper()).stream()
                .findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        List<User> list;
        list = jdbcTemplate.query(GET_ALL, new UserRowMapper());
        return list;
    }

    @Override
    public void add(User element) {
        if (jdbcTemplate.update(ADD, element.getName(), element.getPassword()) == 0)
            System.err.println("Can't add " + element);
    }

    @Override
    public void update(User element) {
        if (jdbcTemplate.update(UPDATE, element.getName(), element.getPassword(), element.getId()) == 0)
            System.err.println("Can't update " + element);
    }

    @Override
    public void delete(Long id) {
        if (jdbcTemplate.update(DELETE, id) == 0)
            System.err.println("Can't delete " + id);
    }

    @Override
    public User getUserByName(String name) {
        return jdbcTemplate.query(GET_BY_NAME, new Object[]{name}, new int[]{Types.VARCHAR}, new UserRowMapper()).
                stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public void deleteByName(String name) {
        if (jdbcTemplate.update(DELETE_BY_NAME, name) == 0)
            System.err.println("Can't delete " + name);
    }
}
