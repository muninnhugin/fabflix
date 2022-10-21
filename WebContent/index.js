let search_form = jQuery("#search_form");
//let genre_form = jQuery("#genre_browse_form");

function handleSearchSubmit(searchEvent)
{
    console.log("handling search submit");
    searchEvent.preventDefault();
    let searchParams = search_form.serialize();
    let redirectUrl = "movie-list.html?" + searchParams;
    console.log("redirecting to " + redirectUrl);
    window.location.assign(redirectUrl);
}

search_form.submit(handleSearchSubmit);



// requests list of genres
// jQuery.ajax({
//     dataType: "json", // Setting return data type
//     method: "GET", // Setting request method
//     url: "api/browse-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
//     success: (genreListData) => populateGenreBrowseForm(genreListData) // Setting callback function to handle data returned successfully by the StarsServlet
// });

// // TODO populate genre form with data from BrowseListServlet
// function populateGenreBrowseForm(genreListData)
// {
//     for(let i = 0; i < genreListData.length; ++i)
//     {
//         let genre_id = genreListData[i]["genre_id"];
//         let genre_name = genreListData[i]["genre_name"];
//         let genreHtml = "<label><input type='radio' name='genre_id' value='" +
//             genre_id + "'>" + genre_name + "</label><br>";
//         genre_form.append(genreHtml);
//     }
//     genre_form.append("<input type='submit' value='Browse'>");
// }

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