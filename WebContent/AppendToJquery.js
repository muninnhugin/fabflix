function appendGenreToJquery(jqueryToAppend, genreJson)
{
    jqueryToAppend.append("<p>Genres</p>" + "<div class=\"list-group\">");
    let genresNum = Math.min(3, genreJson.length);
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

function appendStarsToJquery(jqueryToAppend, starJson)
{
    jqueryToAppend.append("<p>Stars</p>" + "<div class=\"list-group\">");
    let starsNum = Math.min(3, starJson.length);
    if(starsNum != 0)
    {
        for(let i = 0; i < starsNum; ++i)
        {
            jqueryToAppend.append("<a class=\"list-group-item\"" + ">" + starJson[i]["star_name"] + "</a>");
        }
    }
    else
    {
        jqueryToAppend.append("<a>None</a>")
    }
    jqueryToAppend.append("</div>");

    return jqueryToAppend;
}