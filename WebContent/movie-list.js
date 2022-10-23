let order_by_form = jQuery("#order_by_form");

function getURLParameter(name) {
    let urlParams = new URLSearchParams(location.search);
    return urlParams.get(name);
}

function getUrlParameterFromLink()
{
    return {    title: getURLParameter("title"),
        year: getURLParameter("year"),
        director: getURLParameter("director"),
        star_name: getURLParameter("star_name"),
        genre_id: getURLParameter("genre_id")
    };
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleMovieResult(resultData) {
    console.log("handleMovieResult: populating movie table from resultData");

    let movieTableBodyElement = jQuery("#movie_table_body");
    movieTableBodyElement.empty();

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
        rowHTML += "<td><ul id='genre_list'>";
        let genreNum = Math.min(3, resultData[i]["movie_genres"].length);
        for(let j = 0; j < genreNum; ++j)
        {
            let genre_id = resultData[i]["movie_genres"][j]["genre_id"];
            let genre_name = resultData[i]["movie_genres"][j]["genre_name"];
            rowHTML += "<li><a href='movie-list.html?genre_id=" + genre_id + "'>" + genre_name + " </a></li>";
        }
        rowHTML += "</ul></td>";
        rowHTML += "<td><ul id='star_list'>";
        let starNum = Math.min(3, resultData[i]["movie_stars"].length);
        for(let j = 0; j < starNum; ++j)
        {
            rowHTML += "<li><a href='single-star.html?id=" + resultData[i]["movie_stars"][j]["star_id"] + "'> " +
                resultData[i]["movie_stars"][j]["star_name"] + "</a></li>";
        }
        rowHTML += "</ul></td>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

function sortRequest(sortEvent)
{
    console.log("handling sort request");
    sortEvent.preventDefault();
    let order_by = jQuery("#order_by").val();
    let rating_order = jQuery("#rating_order").val();
    let title_order = jQuery("#title_order").val();
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/movie-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
        data: { title: getURLParameter("title"),
            year: getURLParameter("year"),
            director: getURLParameter("director"),
            star_name: getURLParameter("star_name"),
            genre_id: getURLParameter("genre_id"),
            order_by: order_by,
            rating_order: rating_order,
            title_order: title_order
        },
        success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    data: getUrlParameterFromLink(),
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

order_by_form.submit(sortRequest);

