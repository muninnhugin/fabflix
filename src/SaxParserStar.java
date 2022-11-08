import java.io.IOException;
import java.sql.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SaxParserStar extends DefaultHandler {
    private Connection connection;
    private CallableStatement procedure;

    private String tempVal;
    private Star star;


    public SaxParserStar() throws SQLException {
        try {
            String addStarStatement = "CALL add_star(?, ?, ?, ?)";

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Fabflix", "mytestuser", "My6$Password");
            procedure = connection.prepareCall(addStarStatement);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void parseStars() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("stanford-movies/actors63.xml", this);

        } catch (SAXException | ParserConfigurationException | IOException se) {
            se.printStackTrace();
        }
    }

    private void addStarToDb() throws SQLException {
        procedure.setString(1, star.getName());
        if(star.getBirthYear() == 0)
        {
            procedure.setNull(2, java.sql.Types.INTEGER);
        }
        else {
            procedure.setInt(2, star.getBirthYear());
        }
        procedure.registerOutParameter(4, Types.VARCHAR);
        procedure.execute();

        star.setId(procedure.getString(3));
//        System.out.println(procedure.getString(4));
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            star = new Star();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("actor")) {
            try {
                addStarToDb();
//                System.out.println("Inserted " + star.toJson().toString());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            star.clearInfo();

        } else if (qName.equalsIgnoreCase("stagename")) {
            star.setName(tempVal);
        } else if (qName.equalsIgnoreCase("dob")) {
            try {
                star.setBirthYear(Integer.parseInt(tempVal));
            }
            catch (NumberFormatException ignored) {
                System.out.println(star.getName() + " has invalid dob '" + tempVal + "'. Treated as no dob.");
            }
        }
    }

    public static void main(String[] args) {
        SaxParserStar spe = null;
        try {
            spe = new SaxParserStar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert spe != null;
        spe.parseStars();
        System.out.println("Done parsing stars");
    }

}