import java.io.IOException;
import java.sql.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SaxParserCast extends DefaultHandler {
    private Connection connection;
    private CallableStatement procedure;

    private String tempVal;
    private String starName;
    private String movieTitle;

    public SaxParserCast() throws ClassNotFoundException, SQLException {
        String addMovieStatement = "CALL link_star_name_to_movie_title(?, ?)";

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Fabflix", "mytestuser", "My6$Password");
        procedure = connection.prepareCall(addMovieStatement);
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("stanford-movies/casts124.xml", this);

        } catch (SAXException | ParserConfigurationException | IOException se) {
            se.printStackTrace();
        }
    }

    private void linkStarToMovie() throws SQLException {
        if(starName != null) {
//            System.out.println("Linking " + starName + " with " + movieTitle);
            procedure.setString(1, starName);
            procedure.setString(2, movieTitle);
            procedure.execute();
        }
        starName = "";
        movieTitle = "";
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("T")) {

            movieTitle = tempVal;

        } else if (qName.equalsIgnoreCase("A")) {
            if(!tempVal.equals("sa"))
            {
                starName = tempVal;
            }
            else
            {
                starName = null;
                System.out.println("No star name listed. ");
            }
            try {
                linkStarToMovie();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SaxParserCast spe = null;
        try {
            spe = new SaxParserCast();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        assert spe != null;
        spe.parseDocument();
        System.out.println("Done parsing cast");
    }

}
