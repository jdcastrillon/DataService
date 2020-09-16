package Conecion;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

public class PoolConeciones {

    private DataSource dataSource;
    private DataSource data_test;
    private DataSource data_PerfuKing;

    public PoolConeciones() {
        System.out.println("****************** Pool DataSource_Test (1)");
        BasicDataSource DataSource_Test = new BasicDataSource();
        DataSource_Test.setDriverClassName("org.postgresql.Driver");
        DataSource_Test.setUsername("postgres");
        DataSource_Test.setPassword("123456");
        DataSource_Test.setUrl("jdbc:postgresql://localhost:5434/DataERP");
        DataSource_Test.setMaxActive(50);
        data_test = DataSource_Test;

        System.out.println("****************** Pool DataSource_PerfuKing (2)");
        BasicDataSource DataSource_PerfuKing = new BasicDataSource();
        DataSource_PerfuKing.setDriverClassName("org.postgresql.Driver");
        DataSource_PerfuKing.setUsername("postgres");
        DataSource_PerfuKing.setPassword("123456");
        DataSource_PerfuKing.setUrl("jdbc:postgresql://localhost:5434/Data_Permuking");
        DataSource_PerfuKing.setMaxActive(50);
        data_PerfuKing = DataSource_PerfuKing;
    }

    public DataSource getDataSource(int base) {
        switch (base) {
            case 1:
                return data_test;
            case 2:
                return data_PerfuKing;
        }
        return null;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
