/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleResult(resultData) {

    console.log("handleResult: populating movie info from resultData");

    let movieInfoElement = jQuery("#movie_info");

    movieInfoElement.append("<p>Title: " + resultData["movie_title"] + "</p>" +
        "<p>Year: " + resultData["movie_year"] + "</p>" +
        "<p>Director: " + resultData["movie_director"] + "</p>" +
        "<p>Rating: " + resultData["movie_rating"] + "</p>");


    movieInfoElement.append("<p>Genres:</p>" + "<div class=\"list-group\">");
    let genresNum = Math.min(3, resultData["movie_genres"].length);
    if(genresNum != 0)
    {
        for(let i = 0; i < genresNum; ++i)
        {
            movieInfoElement.append("<a class=\"list-group-item\"" + ">" + resultData["movie_genres"][i]["genre_name"] + "</a>");
        }
    }
    else
    {
        movieInfoElement.append("<a>None</a>")
    }
    movieInfoElement.append("</div>");

}

let movieId = getParameterByName('id');

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/single-movie?id=" + movieId,
    success: (resultData) => handleResult(resultData)
});