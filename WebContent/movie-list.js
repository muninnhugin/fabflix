let search_form = jQuery("#search_form");
let movieTableBodyElement = jQuery("#movie_table_body");
let genre_form = jQuery("#genre_browse_form");

// TODO handle genre browse submit event
function handleGenreBrowseSubmit(genreBrowseEvent)
{
    console.log("handling genre browse submit");
    genreBrowseEvent.preventDefault();
    movieTableBodyElement.empty();
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/movie-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
        data: genre_form.serialize(),
        success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleMovieResult(resultData) {
    console.log("handleMovieResult: populating movie table from resultData");

    for (let i = 0; i < Math.min(20, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<td>" +
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +
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

function handleSearchSubmit(searchEvent)
{
    console.log("handling search submit");
    searchEvent.preventDefault();
    movieTableBodyElement.empty();
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/movie-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
        data: search_form.serialize(),
        success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}
// TODO populate genre form with data from BrowseListServlet
function populateGenreBrowseForm(genreListData)
{
    for(let i = 0; i < genreListData.length; ++i)
    {
        let genre_id = genreListData[i]["genre_id"];
        let genre_name = genreListData[i]["genre_name"];
        let genreHtml = "<label><input type='radio' name='genre_id' value='" +
                        genre_id + "'>" + genre_name + "</label><br>";
        genre_form.append(genreHtml);
    }
    genre_form.append("<input type='submit' value='Browse'>");
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browse-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (genreListData) => populateGenreBrowseForm(genreListData) // Setting callback function to handle data returned successfully by the StarsServlet
});

search_form.submit(handleSearchSubmit);
genre_form.submit(handleGenreBrowseSubmit);