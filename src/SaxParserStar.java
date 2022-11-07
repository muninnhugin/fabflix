import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SaxParserStar extends DefaultHandler {
    ArrayList<Star> stars;

    private String tempVal;

    private Star star;

    public SaxParserStar() {
        stars = new ArrayList<>();
    }

    public void runParser() {
        parseDocument();
        try {
            addToDb();
        }
        catch (SQLException | ClassNotFoundException | NamingException e)
        {
            e.printStackTrace();
        }
    }

    private void parseDocument() {

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

    /**
     * Iterate through the list and print
     * the contents
     */
    private void addToDb() throws SQLException, ClassNotFoundException, NamingException {
        Iterator<Star> it = stars.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toJson().toString());
        }

        String statement = "CALL add_star(?, ?, ?, ?)";

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Fabflix", "mytestuser", "My6$Password");
        CallableStatement procedure = connection.prepareCall(statement);
        for(Star star : stars)
        {
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
            System.out.println(procedure.getString(4));
        }
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
            //add it to the list
            stars.add(star);
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
        SaxParserStar spe = new SaxParserStar();
        spe.runParser();
    }

}