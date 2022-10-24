let shopping_cart = jQuery("#shopping_cart");

function handleCartData(cartData)
{
    console.log("handling cart data - showing " + cartData.length + " results.");
    let cart_total = 0;
    let rowHtml = "";
    for(let i = 0; i < cartData.length; ++i)
    {
        let quantity = cartData[i]["quantity"];
        let price = cartData[i]["price"];
        let movie_total = parseInt(quantity) * parseInt(price);
        cart_total += movie_total;
        rowHtml += "<div class='cart_item'>" +
            "<p>Movie Title: " + cartData[i]["movie_title"] + "</p>" +
            "<p>Quantity: " + quantity + "</p>" +
            "<p>Price per copy: $" + price + "</p>" +
            "<p>Total: $" + movie_total + "</p>" +
            "</div>";
    }
    rowHtml += "<p>Order total: $" + cart_total + "</p>"
    shopping_cart.append(rowHtml);
}

jQuery.ajax({
    url: "api/cart",
    method: "GET",
    dataType: "json",
    success: (cartData) => handleCartData(cartData)
})