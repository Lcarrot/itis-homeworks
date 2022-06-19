package ru.itis.Tyshenko.repositories;

import ru.itis.Tyshenko.entity.Cottage;
import ru.itis.Tyshenko.repositories.utility.RowMapper;
import ru.itis.Tyshenko.repositories.utility.SqlJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;

public class CottageRepositoryImpl implements CottageRepository {

    RowMapper<Cottage> rowMapper =
            mapRow -> Cottage.builder().address(mapRow.getString("address"))
                    .id(mapRow.getInt("id"))
                    .price(mapRow.getInt("price")).build();

    private final SqlJdbcTemplate<Cottage> template;


    public CottageRepositoryImpl(Connection connection) {
        template = new SqlJdbcTemplate<>(connection);
    }

    //language=SQL
    private final String SQL_SELECT = "select * from cottages where id = ?";
    @Override
    public Optional<Cottage> get(Cottage cottage) {
        return template.queryForObject(SQL_SELECT, rowMapper, cottage.getId());
    }

    //language=SQL
    private final String SQL_INSERT = "insert into cottages(id, address, price) values (?,?,?)";

    @Override
    public void add(Cottage cottage) {
        template.execute(SQL_INSERT, cottage.getId(), cottage.getAddress(), cottage.getPrice());
    }

    //language=SQL
    private final String SQL_UPDATE = "update cottages set address = ?, price = ? where id = ?";

    @Override
    public void update(Cottage cottage) {
        template.execute(SQL_UPDATE, cottage.getAddress(), cottage.getPrice(), cottage.getId());
    }

    //language=SQL
    private final String SQL_DELETE = "delete from cottages where id = ?";

    @Override
    public void delete(Cottage cottage) {
        template.execute(SQL_DELETE, cottage.getId());
    }
}
