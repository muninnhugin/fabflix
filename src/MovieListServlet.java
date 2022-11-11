import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;



// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "MovieListServlet", urlPatterns = "/api/movie-list")
public class MovieListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_PAGE_SIZE = 25;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Fabflix");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try
        {
            JsonArray movieJsons = getMoviesJson(request);

            // Log to localhost log
            request.getServletContext().log("getting " + movieJsons.size() + " results");

            // Write JSON string to output
            out.write(movieJsons.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {

            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

    private Movie getGenres(Movie movie) throws SQLException
    {
        String query = "SELECT *\n" +
                "FROM movies m, genres_in_movies gm, genres g\n" +
                "WHERE m.id = ? AND m.id = gm.movieId AND gm.genreId = g.id\n" +
                "ORDER BY g.name ASC";

        Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, movie.getId());
        ResultSet rs = statement.executeQuery();

        while(rs.next()) {
            Genre genre = new Genre(rs);
            movie.addGenre(genre);
        }

        conn.close();
        statement.close();
        rs.close();

        return movie;
    }

    private Movie getStars(Movie movie) throws SQLException // sorted by most number of movies in
    {
        String query = "SELECT starId, s.name, s.birthYear, COUNT(movieId)\n" +
                "FROM stars_in_movies, stars s\n" +
                "WHERE starId IN (SELECT sm.starId\n" +
                "\t\t\t\tFROM movies m, stars_in_movies sm, stars s\n" +
                "\t\t\t\tWHERE m.id = ? AND m.id = sm.movieId AND sm.starId = s.id)\n" +
                "\tAND starId = s.id\n" +
                "GROUP BY starId\n" +
                "ORDER BY COUNT(movieId) DESC, name ASC";
        Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, movie.getId());
        ResultSet rs = statement.executeQuery();

        while(rs.next())
        {
            Star star = new Star(rs);
            movie.addStar(star);
        }

        conn.close();
        statement.close();
        rs.close();

        return movie;
    }


    private JsonArray getMoviesJson(HttpServletRequest request) throws SQLException
    {
        String selectClause = "SELECT * ";
        String fromClause = "FROM movies m, ratings r ";
        String whereClause = "WHERE m.id = r.movieId ";
        String orderByClause = "ORDER BY ";
        String limitClause = "LIMIT ";
        String offsetClause = "OFFSET ";

        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String starName = request.getParameter("star_name");
        String genreId = request.getParameter("genre_id");

        String orderBy = request.getParameter("order_by");
        String titleOrder = request.getParameter("title_order");
        String ratingOrder = request.getParameter("rating_order");
        String recordsPerPage = request.getParameter("records_per_page");
        String pageNumber = request.getParameter("page_number");

        int numberOfRecords;
        if(!Helper.isValid(recordsPerPage)) {  numberOfRecords = DEFAULT_PAGE_SIZE; }
        else {  numberOfRecords = Integer.parseInt(recordsPerPage); }

        ArrayList<Object> argList = new ArrayList<>();

        if(Helper.isValid(title))
        {
            if(!title.equals("*"))
            {
                String[] tokens = title.split(" ");
                whereClause += " AND MATCH(m.title) AGAINST ('";
                for(String token : tokens)
                {
                    whereClause += "+" + token + "* ";
                }
                whereClause += "' IN BOOLEAN MODE)";
            }
            else {  whereClause += " AND m.title REGEXP '^[^A-Z0-9]' ";  }
        }
        if(Helper.isValid(year))
        {
            whereClause += " AND m.year = ?";
            argList.add(Integer.parseInt(year));
        }
        if(Helper.isValid(director))
        {
            whereClause += " AND m.director LIKE ?";
            argList.add(like(director));
        }
        if(Helper.isValid(starName))
        {
            fromClause += ", stars_in_movies sm, stars s ";
            whereClause +=  " AND m.id = sm.movieId AND sm.starId = s.id " +
                    " AND s.name LIKE ? ";
            argList.add(like(starName));
        }
        if(Helper.isValid(genreId))
        {
            fromClause += ", genres_in_movies gm, genres g ";
            whereClause +=  " AND m.id = gm.movieId AND gm.genreId = g.id " +
                    " AND g.id = ? ";
            argList.add(Integer.parseInt(genreId));
        }

        limitClause += " ? ";
        argList.add(numberOfRecords);

        offsetClause += " ? ";
        argList.add(getOffset(recordsPerPage, pageNumber));

        // getOrderByArgs check for a hardcoded string and return a hardcoded string
        // so I decide to add its value as is
        orderByClause += getOrderByArgs(orderBy, titleOrder, ratingOrder);

        String query =  selectClause + "\n" +
                fromClause + "\n" +
                whereClause + "\n" +
                orderByClause + "\n" +
                limitClause + "\n" +
                offsetClause;

        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        for(int i = 0; i < argList.size(); ++i)
        {
            if(argList.get(i) instanceof String)
            {
                statement.setString(i + 1, (String) argList.get(i));
            }
            else if(argList.get(i) instanceof Integer)
            {
                statement.setInt(i + 1, (int) argList.get(i));
            }
        }

        JsonArray movieJsons = new JsonArray();

        ResultSet rs = statement.executeQuery();
        while(rs.next())
        {
            Movie movie = new Movie(rs);
            getGenres(movie);
            getStars(movie);

            JsonObject movieJson = movie.toJson();
            movieJsons.add(movieJson);
        }

        rs.close();
        statement.close();
        connection.close();

        return movieJsons;
    }

    private String like(String param)
    {
        return param + "%";
    }

    private String getOrderByArgs(String orderBy, String titleOrder, String ratingOrder)
    {
        String args = "";
        String titleArg = "";
        String ratingArg = "";
        if(!Helper.isValid(orderBy))
        {
            orderBy = "rating";
        }
        if(!Helper.isValid(titleOrder))
        {
            titleOrder = "asc";
        }
        if(!Helper.isValid(ratingOrder))
        {
            ratingOrder = "desc";
        }
        if(titleOrder.equals("desc"))
        {
            titleArg = " m.title DESC ";
        }
        else
        {
            titleArg = " m.title ASC ";
        }
        if(ratingOrder.equals("asc"))
        {
            ratingArg = " r.rating ASC ";
        }
        else
        {
            ratingArg = " r.rating DESC ";
        }
        if(orderBy.equals("title"))
        {
            args = titleArg + "," + ratingArg;
        }
        else
        {
            args = ratingArg + "," + titleArg;
        }

        return args;
    }
    private int getOffset(String recordsPerPageString, String pageNumberString)
    {
        int recordsPerPage = 25;
        int pageNumber = 0;

        if(Helper.isValid(recordsPerPageString))
        {
            recordsPerPage = Integer.parseInt(recordsPerPageString);
        }
        if(Helper.isValid(pageNumberString))
        {
            pageNumber = Integer.parseInt(pageNumberString);
        }

        int offset = recordsPerPage * pageNumber;

        return offset;
    }
}