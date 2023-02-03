package sg.edu.nus.iss.day22_workshop.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.sym.Name;

import jakarta.transaction.TransactionScoped;
import sg.edu.nus.iss.day22_workshop.model.RSVP;

// RsvpRepo interface class  was left out. the RsvpRepoImpl should implement RsvpRepo.

@Repository
public class RsvpRepoImpl {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String findAllSQL = """
            select *
            from rsvp""";

    private final String findByNameSQL = """
            select*
            from rsvp
            where full_name like '%' ? '%'
            """;

    private final String insertSQL = """
            insert into
            rsvp (full_name, email, phone, confirmation_date, comments)
            values (?, ?, ?, ?, ?)
            """;

    private final String updateSQL = """
            update rsvp
            set full_name = ?, email = ?, phone = ?,
            confirmation_date = ?, comments = ?
            where id = ?
            """;

    private final String countSQL = """
            select count(*) as cnt
            from rsvp
            """;

    private final String findByIdSQL = """
            select * from rsvp
            where id = ?
            """;

    private final String findByEmailSQL = """
            select*
            from rsvp
            where email like '%' ? '%'
            """;

    public List<RSVP> findAll() {

        List<RSVP> resultList = new ArrayList<>();
        resultList = jdbcTemplate.query(findAllSQL, BeanPropertyRowMapper.newInstance(RSVP.class));
        return resultList;
    }

    public List<RSVP> findByName(String name) {

        List<RSVP> resultList = new ArrayList<>();
        resultList = jdbcTemplate.query(findByNameSQL, BeanPropertyRowMapper.newInstance(RSVP.class), name);
        return resultList;
    }

    public Boolean save(RSVP rsvp) {

        Integer result = jdbcTemplate.update(insertSQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, rsvp.getFullName());
                ps.setString(2, rsvp.getEmail());
                ps.setInt(3, rsvp.getPhone());
                ps.setDate(4, rsvp.getConfirmationDate());
                ps.setString(5, rsvp.getComments());

            }
        });

        return result > 0 ? true : false;
    }

    public Boolean update(RSVP rsvp) {

        Integer result = jdbcTemplate.update(updateSQL, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, rsvp.getFullName());
                ps.setString(2, rsvp.getEmail());
                ps.setInt(3, rsvp.getPhone());
                ps.setDate(4, rsvp.getConfirmationDate());
                ps.setString(5, rsvp.getComments());
                ps.setInt(6, rsvp.getId());

            }
        });
        return result > 0;
    }

    public Integer countAll() {

        Integer result = jdbcTemplate.queryForObject(countSQL, Integer.class);
        return result;
    }

    public RSVP findById(Integer id) {
        return jdbcTemplate.queryForObject(findByIdSQL, BeanPropertyRowMapper.newInstance(RSVP.class), id);
    }

    // @Transactional
    public int[] batchInsert(List<RSVP> rsvp) {
        return jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, rsvp.get(i).getFullName());
                ps.setString(2, rsvp.get(i).getEmail());
                ps.setInt(3, rsvp.get(i).getPhone());
                ps.setDate(4, rsvp.get(i).getConfirmationDate());
                ps.setString(5, rsvp.get(i).getComments());

            }

            @Override
            public int getBatchSize() {
                return rsvp.size();
            }

        });
    }

    // @Transactional
    public int[] batchUpdate(List<RSVP> rsvp) {
        return jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, rsvp.get(i).getFullName());
                ps.setString(2, rsvp.get(i).getEmail());
                ps.setInt(3, rsvp.get(i).getPhone());
                ps.setDate(4, rsvp.get(i).getConfirmationDate());
                ps.setString(5, rsvp.get(i).getComments());
                ps.setInt(6, rsvp.get(i).getId());

            }

            @Override
            public int getBatchSize() {
                return rsvp.size();
            }

        });
    }

    public RSVP findByEmail(String email) { 
        System.out.println("at findByEmail method >>>>> ");
        RSVP rsvp = new RSVP();
        try {
            rsvp = jdbcTemplate.queryForObject(findByEmailSQL, BeanPropertyRowMapper.newInstance(RSVP.class), email);
            return rsvp;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }
}
