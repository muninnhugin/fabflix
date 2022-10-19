/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

// TODO make faster

function handleMovieResult(resultData) {
    console.log("handleStarResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<td>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +     // display star_name for the link text
            '</a>' +
            "</td>";
        rowHTML += "<td>" + resultData[i]["movie_year"] + "</td>";
        rowHTML += "<td>" + resultData[i]["movie_director"] + "</td>";
        rowHTML += "<td>" + resultData[i]["movie_rating"] + "</td>";
        rowHTML += "<td><ul>";
        let genreNum = Math.min(3, resultData[i]["movie_genres"].length);
        for(let j = 0; j < genreNum; ++j)
        {
            rowHTML += "<li" + "> " + resultData[i]["movie_genres"][j]["genre_name"] + " </li>";
        }
        rowHTML += "</ul></td>";
        rowHTML += "<td><ul>";
        let starNum = Math.min(3, resultData[i]["movie_stars"].length);
        for(let j = 0; j < starNum; ++j)
        {
            rowHTML += "<li><a href='single-star.html?id=" + resultData[i]["movie_stars"][j]["star_id"] + "'> " +
                resultData[i]["movie_stars"][j]["star_name"] + " </a></li>";
        }
        rowHTML += "</ul></td>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});