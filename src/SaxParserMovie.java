import java.io.IOException;
import java.sql.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class SaxParserMovie extends DefaultHandler {
    private Connection connection;
    private CallableStatement procedure;

    private String tempVal;
    private Movie movie = new Movie();
    private String director;

    public SaxParserMovie() throws ClassNotFoundException, SQLException {
        String addMovieStatement = "CALL add_movie(?, ?, ?, ?, ?, ?, ?, ?)";

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
            sp.parse("stanford-movies/mains243.xml", this);

        } catch (SAXException | ParserConfigurationException | IOException se) {
            se.printStackTrace();
        }
    }

    private void addMovieToDb() throws SQLException {
        procedure.setString(1, movie.getTitle());
        if(movie.getYear() == 0)
        {
            procedure.setInt(2, 0);
            System.out.println(movie.getTitle() + " has invalid year '" + tempVal + "'. Treated as year 0.");
        }
        else {
            procedure.setInt(2, movie.getYear());
        }
        procedure.setString(3, director);
        procedure.setNull(4, Types.VARCHAR);
        procedure.setNull(5, Types.VARCHAR);
        procedure.setNull(6, Types.VARCHAR);
        procedure.registerOutParameter(7, Types.VARCHAR);
        procedure.registerOutParameter(8, Types.VARCHAR);
        procedure.execute();

        movie.setId(procedure.getString(7));
//        System.out.println(procedure.getString(8));
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempVal = "";
        if (qName.equalsIgnoreCase("Film")) {
            movie = new Movie();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("Film")) {
            try {
                addMovieToDb();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        } else if (qName.equalsIgnoreCase("Dirname")) {
            director = tempVal;
        } else if (qName.equalsIgnoreCase("Year")) {
            try {
                movie.setYear(Integer.parseInt(tempVal));
            }
            catch (NumberFormatException ignored) {
                System.out.println(movie.getTitle() + " has invalid year '" + tempVal + "'. Treated as year 0.");
            }
        } else if (qName.equalsIgnoreCase("T")) {
            movie.setTitle(tempVal);
        }

    }

    public static void main(String[] args) {
        SaxParserMovie spe = null;
        try {
            spe = new SaxParserMovie();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        assert spe != null;
        spe.parseDocument();
        System.out.println("Done parsing movies");
    }

}
