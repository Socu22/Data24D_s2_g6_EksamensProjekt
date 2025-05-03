package Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;

@Repository
public class DirekteRepository {

    @Autowired
    DataSource dataSource;
    @Autowired
    Connection connection;
}
