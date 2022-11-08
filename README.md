# cs122b-fall-team-4

Team 4

Demo video URL: 

Deployment as usual. Like star example or login example.

Files with PreparedStatement:
    [src/EmployeeLoginServlet.java](https://github.com/uci-jherold2-teaching/cs122b-fall-team-4/blob/main/src/EmployeeLoginServlet.java)
    [src/LoginServlet.java](https://github.com/uci-jherold2-teaching/cs122b-fall-team-4/blob/main/src/LoginServlet.java)
    [src/MovieListServlet.java](https://github.com/uci-jherold2-teaching/cs122b-fall-team-4/blob/main/src/MovieListServlet.java)
    [src/PaymentServlet.java](https://github.com/uci-jherold2-teaching/cs122b-fall-team-4/blob/main/src/PaymentServlet.java)
    [src/SingleMovieServlet.java](https://github.com/uci-jherold2-teaching/cs122b-fall-team-4/blob/main/src/SingleMovieServlet.java)
    [src/SingleStarServlet.java](https://github.com/uci-jherold2-teaching/cs122b-fall-team-4/blob/main/src/SingleStarServlet.java)
    
Files with CallableStatement, inheriting from PreparedStatement:
    [src/EmployeeDashboardServlet.java](https://github.com/uci-jherold2-teaching/cs122b-fall-team-4/blob/main/src/EmployeeDashboardServlet.java)
        
Inconsistent XML data:
    For actors63.xml, any missing or non-integer dob listed is considered 'null' when inserting to database.
    For mains243.xml, any missing or non-integer movie year is considered '0' when inserting to database.
    For casts124.xml, stars listed as 'sa' are not considered actual stars, and thus not linked to a movie.
    
Parsing optimization strategy:
    To free up memory using the SAX Parsing method, each star/movie entry are inserted to the database individually, not as an array list of stars/movies.

All contribution made by Ha Bach (bachh1).
