let search_form = jQuery("#search_form");
let genre_list = jQuery("#genre_browse_list");


function handleSearchSubmit(searchEvent)
{
    console.log("handling search submit");
    searchEvent.preventDefault();
    let searchParams = search_form.serialize();
    let redirectUrl = "movie-list.html?" + searchParams;
    console.log("redirecting to " + redirectUrl);
    window.location.assign(redirectUrl);
}

// TODO populate genre form with data from BrowseListServlet
function populateGenreBrowseList(genreListData)
{
    for(let i = 0; i < genreListData.length; ++i)
    {
        let genre_id = genreListData[i]["genre_id"];
        let genre_name = genreListData[i]["genre_name"];
        let genreHtml = '<div class="genre_item"><a href="movie-list.html?genre_id=' +
            genre_id + '">' + genre_name + '</a></div>';
        genre_list.append(genreHtml);
    }
}

search_form.submit(handleSearchSubmit);

// requests list of genres
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browse-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (genreListData) => populateGenreBrowseList(genreListData) // Setting callback function to handle data returned successfully by the StarsServlet
});



// // TODO handle genre browse submit event
// function handleGenreBrowseSubmit(genreBrowseEvent)
// {
//     console.log("handling genre browse submit");
//     genreBrowseEvent.preventDefault();
//     movieTableBodyElement.empty();
//     jQuery.ajax({
//         dataType: "json", // Setting return data type
//         method: "GET", // Setting request method
//         url: "api/movie-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
//         data: genre_form.serialize(),
//         success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
//     });
// }

//genre_form.submit(handleGenreBrowseSubmit);