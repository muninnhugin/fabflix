let sort_form = jQuery("#sort_form");
let next_page_button = jQuery("#next_page_button");
let prev_page_button = jQuery("#prev_page_button");
let page_number = 0;

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
        genre_id: getURLParameter("genre_id"),
        order_by: jQuery("#order_by").val(),
        rating_order: jQuery("#rating_order").val(),
        title_order: jQuery("#title_order").val(),
        records_per_page: jQuery("#records_per_page").val(),
        page_number:page_number
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

    for (let i = 0; i < resultData.length; i++) {

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
    page_number = 0
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "api/movie-list",
        data: getUrlParameterFromLink(),
        success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

function requestMovieData()
{
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/movie-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
        data: getUrlParameterFromLink(),
        success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

requestMovieData();


// TODO change to on select instead of on submit
sort_form.submit(sortRequest);


// TODO main: implement next and prev button
function getNextPage()
{
    page_number += 1;
    console.log("getting next page");
    if(page_number > 0)
    {
        prev_page_button.prop("disabled", false);
    }
    console.log("getting page number " + page_number);
    requestMovieData();
}

function getPrevPage()
{
    console.log("getting previous page");
    if(page_number < 1)
    {
        prev_page_button.prop("disable", true);
    }
    else
    {
        page_number -= 1;
    }
    console.log("getting page number " + page_number);
    requestMovieData();
}


