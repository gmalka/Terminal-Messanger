package com.repositories;

import com.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageRepositoryReal implements MessageRepository{

    private JdbcTemplate jdbcTemplate;

    private final String CREATE_MESSAGE = "CREATE TABLE IF NOT EXISTS server.message (\nmessage text not null,\nauthor varchar(100) not null,\ntitle varchar(40) not null, \ntime timestamp default current_timestamp);";

    private final String ALL_SELECT_QUERY = "SELECT * FROM server.message";

    private final String INSERT_INTO_QUERY = "INSERT INTO server.message (message, author, title) VALUES (?, ?, ?)";

    @Autowired
    public MessageRepositoryReal(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
        startThis();
    }

    private void startThis() {
        //jdbcTemplate.execute("DROP  TABLE server.message;");
        jdbcTemplate.execute(CREATE_SERVER);
        jdbcTemplate.execute(CREATE_MESSAGE);
    }

    class MessageRowMapper implements RowMapper<Message> {

        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Message(rs.getString("message"), rs.getString("author"), rs.getString("title"), rs.getTimestamp("time").toLocalDateTime());
        }
    }

    @Override
    public List<Message> findAllInRoom(String title) {
        List<Message> list = null;
        try {
            String query = "SELECT * FROM (SELECT * FROM server.message WHERE title = '" + title + "' ORDER BY time DESC LIMIT 30) order by time asc";
            list = jdbcTemplate.query(query, new MessageRowMapper());
        } catch (Exception ignored) {}
        return list;
    }

    @Override
    public Message getLastAuthorsMessage(String author) {
        List<Message> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM server.message WHERE author = '" + author + "' ORDER BY time DESC LIMIT 1";
            list = jdbcTemplate.query(query, new MessageRowMapper());
        }
        catch (Exception ignored) {}
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Message findById(int id) {
        return null;
    }

    @Override
    public List<Message> getAll() {
        List<Message> list = jdbcTemplate.query(ALL_SELECT_QUERY, new MessageRowMapper());
        return list;
    }

    @Override
    public void add(Message element) {
        if (jdbcTemplate.update(INSERT_INTO_QUERY, element.getMessage(), element.getAuthor(), element.getTitleRoom()) == 0)
            System.err.println("Can't save message " + element);
    }

    @Override
    public void update(Message element) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }
}
