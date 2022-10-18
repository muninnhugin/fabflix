function appendGenreToJquery(jqueryToAppend, genreJson, maxStars = Infinity)
{
    jqueryToAppend.append("<p>Genres</p>" + "<div class=\"list-group\">");
    let genresNum = Math.min(maxStars, genreJson.length);
    if(genresNum != 0)
    {
        for(let i = 0; i < genresNum; ++i)
        {
            jqueryToAppend.append("<a class=\"list-group-item\"" + ">" + genreJson[i]["genre_name"] + "</a>");
        }
    }
    else
    {
        jqueryToAppend.append("<a>None</a>")
    }
    jqueryToAppend.append("</div>");

    return jqueryToAppend;
}

function appendStarsToJquery(jqueryToAppend, starJson, maxStars = Infinity)
{
    jqueryToAppend.append("<p>Stars</p>" + "<div class=\"list-group\">");
    let starsNum = Math.min(maxStars, starJson.length);
    if(starsNum != 0)
    {
        for(let i = 0; i < starsNum; ++i)
        {
            jqueryToAppend.append("<a class='list-group-item'" +
                " href='single-star.html?id=" +
                starJson[i]['star_id'] + "'>" +
                starJson[i]['star_name'] + "</a>");
        }
    }
    else
    {
        jqueryToAppend.append("<a>None</a>")
    }
    jqueryToAppend.append("</div>");

    return jqueryToAppend;
}

function appendMoviesToTable(jQueryToAppend, movieJsons, maxMovies = Infinity)
{
    for (let i = 0; i < Math.min(10, movieJsons.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" +
            "<a href='single-movie.html?id='" +
            movieJsons[i]['movie_id'] + ">" +
            movieJsons[i]["movie_title"] +
            "</a>" +
            "</th>";
        rowHTML += "<th>" + movieJsons[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + movieJsons[i]["movie_director"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        jQueryToAppend.append(rowHTML);
    }
}