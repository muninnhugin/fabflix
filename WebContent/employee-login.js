let employee_login_form = jQuery("#employee_login_form");

function handleEmployeeLogin(loginEvent)
{
    console.log("logging in employee.");
    loginEvent.preventDefault();
    jQuery.ajax(
        {
            url: "api/employee_login",
            method: "POST",
            dataType: "json",
            data: employee_login_form.serialize(),
            success: (loginResponse) => handleLogin(loginResponse)
        }
    );
}

function handleLogin(loginResponse)
{
    console.log("login response: ");
    console.log(loginResponse);

    if (loginResponse["status"] === "success") {
        window.location.replace("employee-dashboard.html");
    } else {
        console.log("show error message");
        console.log(loginResponse["message"]);
        jQuery("#login_error_message").text(loginResponse["message"]);
    }
}

employee_login_form.submit(handleEmployeeLogin);