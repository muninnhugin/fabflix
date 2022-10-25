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
    let status = result["status"];
    let message = result["message"];
    let rowHtml =   "<p>Payment Status: " + status + "</p>" +
                    "<p>Payment Message: " + message + "</p>"
    form_submit_feedback.append(rowHtml);
}

jQuery.ajax({
    url: "api/payment",
    method: "GET",
    dataType: "json",
    success: (totalData) => handleTotalData(totalData)
});

payment_form.submit(handlePaymentSubmit);