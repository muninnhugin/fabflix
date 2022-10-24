let movieId = getParameterByName('id'), movieTitle;

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

function addToCart()
{
    console.log("adding item to cart");
    jQuery.ajax(
        {
            url: "api/single-movie",
            method: "POST",
            dataType: "json",
            data: {
                "movie_id": movieId,
                "movie_title": movieTitle,
                "quantity": 1
            },
            success: (itemData) => confirm("You have " + itemData["quantity"] + " of " + movieTitle + " in your cart")
        }
    )
}

function handleResult(resultData) {

    console.log("handleResult: populating movie info from resultData");

    let movieInfoElement = jQuery("#movie_info");
    movieTitle = resultData["movie_title"];

    movieInfoElement.append("<p>Title: " + movieTitle + "</p>" +
        "<p>Year: " + resultData["movie_year"] + "</p>" +
        "<p>Director: " + resultData["movie_director"] + "</p>" +
        "<p>Rating: " + resultData["movie_rating"] + "</p>");

    movieInfoElement.append(`<input type='button' id='add_to_cart_btn'class='btn btn-primary' value='Add to Shopping Cart' onclick='addToCart()'>`);

    appendGenreToJquery(movieInfoElement, resultData["movie_genres"]);
    appendStarsToJquery(movieInfoElement, resultData["movie_stars"]);
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/single-movie?id=" + movieId,
    success: (resultData) => handleResult(resultData)
});