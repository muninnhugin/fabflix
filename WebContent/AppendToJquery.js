function getGenreJquery(jqueryToAppend, genreJson)
{
    console.log("getting genre jquery from genre json");

    jqueryToAppend.add("<p>Genres:</p>" + "<div class=\"list-group\">");
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