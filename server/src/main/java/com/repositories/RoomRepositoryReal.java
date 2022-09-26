package com.repositories;

import com.model.Chatroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoomRepositoryReal implements RoomRepository{

    JdbcTemplate jdbcTemplate;

    private final String CREATE_CHATROOM = "CREATE TABLE IF NOT EXISTS server.rooms (\nid serial primary key,\ntitle varchar(40) not null unique, \nowner varchar(100) not null);";
    private final String FIND_BY_ID = "SELECT * FROM server.rooms WHERE id = ?";
    private final String FIND_ALL = "SELECT * FROM server.rooms";
    private final String INSERT_INTO = "INSERT INTO server.rooms (title, owner) VALUES (?, ?)";
    private final String UPDATE_QUERY = "UPDATE server.rooms SET title = ?, owner = ? WHERE id = ?";
    private final String DELETE_QUERY = "DELETE FROM server.rooms WHERE title = ?";
    private final String FIND_BY_TITLE = "SELECT * FROM server.rooms WHERE title = ?";
    private final String DELETE_BY_NAME = "DELETE FROM server.rooms WHERE name = ?";

    @Autowired
    public RoomRepositoryReal(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
        startThis();
    }

    private void startThis() {
        //jdbcTemplate.execute("DROP  TABLE server.rooms;");
        jdbcTemplate.execute(CREATE_SERVER);
        jdbcTemplate.execute(CREATE_CHATROOM);
    }

    private class ChatroomRowMapper implements RowMapper<Chatroom> {

        @Override
        public Chatroom mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Chatroom(rs.getLong("id"), rs.getString("owner"), rs.getString("title"), new ArrayList<>());
        }
    }

    @Override
    public Chatroom findById(int id) {
        Chatroom room;
        room = jdbcTemplate.query(FIND_BY_ID, new Object[]{id}, new int[]{Types.INTEGER}, new ChatroomRowMapper()).stream().findAny().orElse(null);
        return room;
    }

    @Override
    public List<Chatroom> getAll() {
        List<Chatroom> list;
        list = jdbcTemplate.query(FIND_ALL, new ChatroomRowMapper());
        return list;
    }

    @Override
    public void add(Chatroom element) {
        if (jdbcTemplate.update(INSERT_INTO, element.getTitle(), element.getTitle()) == 0)
            System.err.println("Can't add " + element);
    }

    @Override
    public void update(Chatroom element) {
        if (jdbcTemplate.update(UPDATE_QUERY, element.getTitle(), element.getOwner(), element.getId()) == 0)
            System.err.println("Can't update " + element);

    }

    @Override
    public void delete(Long id) {
        if (jdbcTemplate.update(DELETE_QUERY, id) == 0)
            System.err.println("Can't find " + id + " chatroom");
    }

    @Override
    public void deleteByName(String name) {
        if (jdbcTemplate.update(DELETE_BY_NAME, name) == 0)
            System.err.println("Can't delete " + name);
    }

    @Override
    public Chatroom findByTitle(String title) {
        Chatroom room = jdbcTemplate.query(FIND_BY_TITLE, new Object[]{title}, new int[]{Types.VARCHAR}, new ChatroomRowMapper()).stream().findAny().orElse(null);
        return room;
    }
}
