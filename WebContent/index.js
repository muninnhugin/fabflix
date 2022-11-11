const NUM_OF_ALPHABET_LETTERS = 26;

let search_form = jQuery("#search_form");
let genre_list = jQuery("#genre_browse_list");
let title_search_bar = jQuery("#title_search_bar");

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

function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful")

    // parse the string into JSON
    let jsonData = JSON.parse(data);
    console.log(jsonData)

    // TODO: if you want to cache the result into a global variable you can do it here

    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}

function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")
    console.log("sending AJAX request to backend Java Servlet")

    // TODO: if you want to check past query results first, you can do it here

    jQuery.ajax({
        "method": "GET",
        "url": "movie_title_autocomplete?query=" + escape(query),
        "success": function(data) {
            handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

function handleSelectSuggestion(suggestion) {
    let redirectUrl = "single-movie.html?id=" + suggestion["data"];
    console.log("redirecting to " + redirectUrl);
    window.location.assign(redirectUrl);
}

title_search_bar.autocomplete({
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    minChars: 3,
    lookupLimit: 10
});


// requests list of genres
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/browse-list", // Setting request url, which is mapped by MovieListServlet in MovieListServlet.java
    success: (genreListData) => populateGenreBrowseList(genreListData) // Setting callback function to handle data returned successfully by the StarsServlet
});

search_form.submit(handleSearchSubmit);
populateTitleBrowseList();