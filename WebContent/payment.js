let payment_total = jQuery("#payment_total");
let payment_form = jQuery("#payment_form");
let form_submit_feedback = jQuery("#form_submit_feedback");

function handleTotalData(totalData)
{
    payment_total.append(totalData["cart_total"]);
}

function handlePaymentSubmit(paymentEvent)
{
    console.log("placing order");
    paymentEvent.preventDefault();
    jQuery.ajax({
        url: "api/payment",
        method: "post",
        dataType: "json",
        data: payment_form.serialize(),
        success: handlePaymentSubmitResult
    })
}

function handlePaymentSubmitResult(result)
{
    let rowHtml =   "<p>Payment Status: " + result["status"] + "</p>" +
                    "<p>Payment Message: " + result["message"] + "</p>";
    if(result["status"] === "success") {

        rowHtml +=  "<p>Sale ID: " + result["sale_id"] + "</p>" +
                    "<p>Customer ID: " + result["user_id"] + "</p>";
        for(let i = 0; i < result["cart_items"].length; ++i)
        {
            rowHtml += "<p>Movie Title: " + result["cart_items"][i]["movie_title"] + "</p>" +
                "<p>Quantity Purchased: " + result["cart_items"][i]["quantity"] + "</p>"
        }
        rowHtml += "<p>Total: " + result["cart_total"] + "</p>";
    }
    form_submit_feedback.append(rowHtml);
}

jQuery.ajax({
    url: "api/payment",
    method: "GET",
    dataType: "json",
    success: (totalData) => handleTotalData(totalData)
});

payment_form.submit(handlePaymentSubmit);