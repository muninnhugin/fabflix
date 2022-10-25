let payment_total = jQuery("#payment_total");

function handleTotalData(totalData)
{
    payment_total.append(totalData["cart_total"]);
}

jQuery.ajax({
    url: "api/payment",
    method: "GET",
    dataType: "json",
    success: (totalData) => handleTotalData(totalData)
});