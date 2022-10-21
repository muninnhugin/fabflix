const NUM_OF_ALPHABET_LETTERS = 26;

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

function populateTitleBrowseList()
{
    let title_browse_list = jQuery("#title_browse_list");

    // populate with numbers and special characters
    let title_html = '<div class="special_character_titles">' +
        '<a href="movie-list.html?title=*">*</a></div>';
    title_html += '<div id="number_titles" class="browse_list">';
    for(let i = 0; i < 10; ++i)
    {
        title_html += '<div class="number_title"><a href="movie-list.html?title=' +
            i + '">' + i + '</a></div>';
    }
    title_html += '</div>';
    title_browse_list.append(title_html);

    // populate with characters
    title_html = '<div id="character_titles" class="browse_list">';
    for(let i = 0; i < NUM_OF_ALPHABET_LETTERS; ++i)
    {
        let letter = String.fromCharCode(65 + i);
        title_html += '<div class="character_title"><a href="movie-list.html?title=' +
            letter + '">' + letter + '</a></div>';
    }
    title_html += '</div>';
    title_browse_list.append(title_html);
}

search_form.submit(handleSearchSubmit);

// requests list of genres
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browse-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (genreListData) => populateGenreBrowseList(genreListData) // Setting callback function to handle data returned successfully by the StarsServlet
});

populateTitleBrowseList();



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