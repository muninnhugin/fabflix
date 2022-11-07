let add_star_form = jQuery("#add_star_form");
let database_button = jQuery("#database_metadata_button");
let database_table = jQuery("#database_metadata_table");
let add_movie_form = jQuery("#add_movie_form");

function handleAddStar(addEvent)
{
    console.log("sending add star request");
    addEvent.preventDefault();
    jQuery.ajax(
        {
            url: "api/employee_dashboard",
            method: "POST",
            dataType: "JSON",
            data: add_star_form.serialize(),
            success: (addStarResponse) => handleAddStarResponse(addStarResponse)
        }
    );
}

function handleAddMovie(addEvent)
{
    console.log("sending add movie request");
    addEvent.preventDefault();
    jQuery.ajax({
        url: "api/employee_dashboard",
        method: "POST",
        dataType: "JSON",
        data: add_movie_form.serialize(),
        success: (addMovieResponse) => handleAddMovieResponse(addMovieResponse)
    })
}

function handleDatabaseClick()
{
    database_table.empty();
    jQuery.ajax({
        url: "api/employee_dashboard",
        method: "GET",
        dataType: "JSON",
        success: (databaseMetadata) => createDbMetadataTable(databaseMetadata)
    })
}

function handleAddStarResponse(response) {
    console.log("printing add star request status");
    let add_star_message = jQuery("#add_star_message");
    add_star_message.empty();
    let message = response["message"];
    add_star_message.append(message);

}

function handleAddMovieResponse(response)
{
    console.log("printing add movie request status");
    let add_movie_message = jQuery("#add_movie_message");
    add_movie_message.empty();
    let message = response["message"];
    add_movie_message.append(message);
}

function createDbMetadataTable(databaseMetadata)
{
    let html = "<tr>" +
        "<th>Table</th>" +
        "<th>Column</th>" +
        "<th>Data Type</th>" +
        "</tr>";

    for(let i = 0; i < databaseMetadata.length; ++i)
    {
        html += "<tr>" +
            "<td>" + databaseMetadata[i]["table_name"] + "</td>" +
            "<td>" + databaseMetadata[i]["column_name"] + "</td>" +
            "<td>" + databaseMetadata[i]["data_type"] + "</td>" +
            "</tr>>";
    }

    database_table.append(html);
}

add_star_form.submit(handleAddStar);
database_button.click(handleDatabaseClick);
add_movie_form.submit(handleAddMovie);