let add_star_form = jQuery("#add_star_form");

function handleAddStarResponse(response) {
    console.log("printing add star request status");
    let add_star_message = jQuery("#add_star_message");
    add_star_message.empty();
    let message = "Status: " + response["status"] + "<br>" +
        "Message: " + response["message"]
    add_star_message.append(message);

}

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
    // console.log(add_star_form.serialize());
}

add_star_form.submit(handleAddStar);