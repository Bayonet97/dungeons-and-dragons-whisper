package jdbcproperties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class JDBCPropertiesGenerator {

//    private static final Logger LOG = Logger.getLogger(JDBCPropertiesGenerator.class.getName());
//
//    public static void main(String[] args) {
//
//        Properties prop = new Properties();
//        OutputStream output = null;
//
//        try {
//
//            output = new FileOutputStream("db.prop");
//
//            // set the properties value
//            prop.setProperty("jdbc.drivers", "org.sqlite.JDBC");
//            prop.setProperty("jdbc.url", "jdbc:sqlite:dandwhisper.db");
//            prop.setProperty("jdbc.username", "na");
//            prop.setProperty("jdbc.password", "na");
//
//            // save properties to project root folder
//            prop.store(output, null);
//
//        } catch (IOException io) {
//            LOG.warning(io.getMessage());
//        } finally {
//            if (output != null) {
//                try {
//                    output.close();
//                } catch (IOException e) {
//                    LOG.warning(e.getMessage());
//                }
//            }
//
//        }
//    }

}