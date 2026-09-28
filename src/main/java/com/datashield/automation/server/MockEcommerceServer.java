package com.datashield.automation.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class MockEcommerceServer {

    private static HttpServer server;
    private static final int PORT = 8088;

    public static synchronized void startServer() {
        if (server != null) {
            return;
        }

        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/", new StaticHandler(getLoginHtml()));
            server.createContext("/inventory.html", new StaticHandler(getInventoryHtml()));
            server.createContext("/cart.html", new StaticHandler(getCartHtml()));
            server.createContext("/checkout-step-one.html", new StaticHandler(getCheckoutHtml()));
            server.createContext("/checkout-step-two.html", new StaticHandler(getCheckoutHtml()));
            server.createContext("/checkout-complete.html", new StaticHandler(getConfirmationHtml()));
            server.setExecutor(null);
            server.start();
            System.out.println("Mock E-Commerce Server started successfully at http://localhost:" + PORT);
        } catch (IOException e) {
            System.err.println("Could not start Mock E-Commerce Server: " + e.getMessage());
        }
    }

    public static synchronized void stopServer() {
        if (server != null) {
            server.stop(0);
            server = null;
            System.out.println("Mock E-Commerce Server stopped.");
        }
    }

    private static class StaticHandler implements HttpHandler {
        private final String content;

        public StaticHandler(String content) {
            this.content = content;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static String getLoginHtml() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Swag Labs</title>\n" +
                "    <meta charset='utf-8'>\n" +
                "    <style>\n" +
                "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background-color: #242b35; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh; }\n" +
                "        .login_wrapper { background: #ffffff; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.25); width: 380px; padding: 36px; text-align: center; }\n" +
                "        .login_logo { font-size: 32px; font-weight: 800; color: #13232c; margin-bottom: 28px; letter-spacing: 1px; font-family: 'Arial Black', sans-serif; }\n" +
                "        .form_group { margin-bottom: 16px; }\n" +
                "        .input_error { width: 100%; box-sizing: border-box; padding: 13px 16px; border: 2px solid #e2e8f0; border-radius: 8px; font-size: 15px; outline: none; transition: border-color 0.2s; }\n" +
                "        .input_error:focus { border-color: #2e7d32; }\n" +
                "        .submit-button { width: 100%; padding: 13px; background: #e2231a; color: white; border: none; border-radius: 8px; font-size: 16px; font-weight: 700; cursor: pointer; transition: background 0.2s, transform 0.1s; }\n" +
                "        .submit-button:hover { background: #b81c15; }\n" +
                "        .submit-button:active { transform: scale(0.98); }\n" +
                "        .error-message-container { margin-top: 18px; }\n" +
                "        .error-message-container h3 { font-size: 13px; color: #b71c1c; background: #ffebee; border: 1px solid #ffcdd2; padding: 10px 14px; border-radius: 6px; margin: 0; display: none; line-height: 1.4; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"login_wrapper\">\n" +
                "        <div class=\"login_logo\">Swag Labs</div>\n" +
                "        <form id=\"login_form\" onsubmit=\"handleLogin(event)\">\n" +
                "            <div class=\"form_group\">\n" +
                "                <input class=\"input_error\" placeholder=\"Username\" type=\"text\" id=\"user-name\" name=\"user-name\" autocorrect=\"off\" autocapitalize=\"none\">\n" +
                "            </div>\n" +
                "            <div class=\"form_group\">\n" +
                "                <input class=\"input_error\" placeholder=\"Password\" type=\"password\" id=\"password\" name=\"password\">\n" +
                "            </div>\n" +
                "            <input type=\"submit\" class=\"submit-button btn_action\" id=\"login-button\" name=\"login-button\" value=\"Login\">\n" +
                "            <div class=\"error-message-container\">\n" +
                "                <h3 data-test=\"error\" id=\"error-msg\"></h3>\n" +
                "            </div>\n" +
                "        </form>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        function handleLogin(e) {\n" +
                "            e.preventDefault();\n" +
                "            const u = document.getElementById('user-name').value.trim();\n" +
                "            const p = document.getElementById('password').value;\n" +
                "            const err = document.getElementById('error-msg');\n" +
                "            if (u === 'standard_user' && p === 'secret_sauce') {\n" +
                "                sessionStorage.setItem('loggedIn', 'true');\n" +
                "                sessionStorage.setItem('cartCount', '0');\n" +
                "                window.location.href = '/inventory.html';\n" +
                "            } else {\n" +
                "                err.innerText = 'Epic sadface: Username and password do not match any user in this service';\n" +
                "                err.style.display = 'block';\n" +
                "            }\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    private static String getInventoryHtml() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Swag Labs - Products</title>\n" +
                "    <meta charset='utf-8'>\n" +
                "    <style>\n" +
                "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f4f5f7; margin: 0; }\n" +
                "        .header { background: #13232c; color: white; padding: 16px 36px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 8px rgba(0,0,0,0.15); }\n" +
                "        .header .app_logo { font-size: 26px; font-weight: 800; font-family: 'Arial Black', sans-serif; letter-spacing: 1px; }\n" +
                "        .shopping_cart_link { position: relative; color: white; text-decoration: none; font-size: 26px; cursor: pointer; display: flex; align-items: center; }\n" +
                "        .shopping_cart_badge { position: absolute; top: -8px; right: -12px; background: #e2231a; color: white; border-radius: 12px; font-size: 12px; padding: 2px 7px; font-weight: 700; }\n" +
                "        .sub_header { background: white; padding: 14px 36px; border-bottom: 1px solid #e0e0e0; display: flex; justify-content: space-between; align-items: center; }\n" +
                "        .title { font-size: 22px; font-weight: 700; color: #13232c; text-transform: capitalize; }\n" +
                "        .inventory_container { max-width: 1200px; margin: 30px auto; padding: 0 24px; display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 24px; }\n" +
                "        .inventory_item { background: white; border-radius: 10px; padding: 22px; box-shadow: 0 3px 10px rgba(0,0,0,0.05); display: flex; flex-direction: column; justify-content: space-between; border: 1px solid #eaeaea; transition: transform 0.2s, box-shadow 0.2s; }\n" +
                "        .inventory_item:hover { transform: translateY(-3px); box-shadow: 0 6px 18px rgba(0,0,0,0.08); }\n" +
                "        .inventory_item_img { height: 160px; background: #eef2f6; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 50px; margin-bottom: 16px; }\n" +
                "        .inventory_item_name { font-size: 19px; font-weight: 700; color: #185875; margin-bottom: 8px; cursor: pointer; }\n" +
                "        .inventory_item_desc { font-size: 14px; color: #555; line-height: 1.5; margin-bottom: 18px; flex-grow: 1; }\n" +
                "        .pricebar { display: flex; justify-content: space-between; align-items: center; margin-top: auto; padding-top: 12px; border-top: 1px solid #f0f0f0; }\n" +
                "        .inventory_item_price { font-size: 20px; font-weight: 800; color: #13232c; }\n" +
                "        .btn_inventory { padding: 9px 18px; border-radius: 6px; border: 2px solid #13232c; background: transparent; font-weight: 700; font-size: 14px; cursor: pointer; transition: all 0.2s; }\n" +
                "        .btn_inventory:hover { background: #13232c; color: white; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <div class=\"app_logo\">Swag Labs</div>\n" +
                "        <a class=\"shopping_cart_link\" onclick=\"window.location.href='/cart.html'\">\n" +
                "            🛒 <span class=\"shopping_cart_badge\" id=\"cart-badge\" style=\"display:none;\">0</span>\n" +
                "        </a>\n" +
                "    </div>\n" +
                "    <div class=\"sub_header\">\n" +
                "        <span class=\"title\">Products</span>\n" +
                "    </div>\n" +
                "    <div class=\"inventory_container\">\n" +
                "        <div class=\"inventory_item\">\n" +
                "            <div class=\"inventory_item_img\">🎒</div>\n" +
                "            <div class=\"inventory_item_name\">Sauce Labs Backpack</div>\n" +
                "            <div class=\"inventory_item_desc\">Carry all the things with the sleek, streamlined Sly Pack that melds uncompromising style and unequaled laptop and tablet protection.</div>\n" +
                "            <div class=\"pricebar\">\n" +
                "                <div class=\"inventory_item_price\">$29.99</div>\n" +
                "                <button class=\"btn_inventory\" id=\"add-to-cart-sauce-labs-backpack\" onclick=\"toggleCart(this)\">Add to cart</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"inventory_item\">\n" +
                "            <div class=\"inventory_item_img\">💡</div>\n" +
                "            <div class=\"inventory_item_name\">Sauce Labs Bike Light</div>\n" +
                "            <div class=\"inventory_item_desc\">A red light for your bike or anywhere you need light, with 3 lighting modes.</div>\n" +
                "            <div class=\"pricebar\">\n" +
                "                <div class=\"inventory_item_price\">$9.99</div>\n" +
                "                <button class=\"btn_inventory\" id=\"add-to-cart-bike-light\" onclick=\"toggleCart(this)\">Add to cart</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"inventory_item\">\n" +
                "            <div class=\"inventory_item_img\">👕</div>\n" +
                "            <div class=\"inventory_item_name\">Sauce Labs Bolt T-Shirt</div>\n" +
                "            <div class=\"inventory_item_desc\">Get your testing superhero on with the Sauce Labs bolt T-shirt.</div>\n" +
                "            <div class=\"pricebar\">\n" +
                "                <div class=\"inventory_item_price\">$15.99</div>\n" +
                "                <button class=\"btn_inventory\" id=\"add-to-cart-tshirt\" onclick=\"toggleCart(this)\">Add to cart</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        let count = parseInt(sessionStorage.getItem('cartCount') || '0');\n" +
                "        updateBadge();\n" +
                "        function toggleCart(btn) {\n" +
                "            if (btn.innerText === 'Add to cart') {\n" +
                "                btn.innerText = 'Remove';\n" +
                "                btn.style.color = '#e2231a';\n" +
                "                btn.style.borderColor = '#e2231a';\n" +
                "                count++;\n" +
                "            } else {\n" +
                "                btn.innerText = 'Add to cart';\n" +
                "                btn.style.color = '';\n" +
                "                btn.style.borderColor = '';\n" +
                "                if (count > 0) count--;\n" +
                "            }\n" +
                "            sessionStorage.setItem('cartCount', count);\n" +
                "            updateBadge();\n" +
                "        }\n" +
                "        function updateBadge() {\n" +
                "            const b = document.getElementById('cart-badge');\n" +
                "            if (count > 0) {\n" +
                "                b.innerText = count;\n" +
                "                b.style.display = 'inline-block';\n" +
                "            } else {\n" +
                "                b.style.display = 'none';\n" +
                "            }\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    private static String getCartHtml() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Swag Labs - Cart</title>\n" +
                "    <meta charset='utf-8'>\n" +
                "    <style>\n" +
                "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f4f5f7; margin: 0; }\n" +
                "        .header { background: #13232c; color: white; padding: 16px 36px; display: flex; justify-content: space-between; align-items: center; }\n" +
                "        .header .app_logo { font-size: 26px; font-weight: 800; font-family: 'Arial Black', sans-serif; letter-spacing: 1px; }\n" +
                "        .sub_header { background: white; padding: 14px 36px; border-bottom: 1px solid #e0e0e0; }\n" +
                "        .title { font-size: 22px; font-weight: 700; color: #13232c; }\n" +
                "        .cart_container { max-width: 900px; margin: 35px auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 3px 12px rgba(0,0,0,0.06); }\n" +
                "        .cart_item { border-bottom: 1px solid #eee; padding: 18px 0; display: flex; justify-content: space-between; align-items: center; }\n" +
                "        .cart_item_label { font-size: 18px; font-weight: 700; color: #185875; }\n" +
                "        .cart_item_price { font-size: 18px; font-weight: 700; color: #13232c; }\n" +
                "        .cart_footer { margin-top: 30px; display: flex; justify-content: flex-end; gap: 16px; }\n" +
                "        .btn_action { padding: 12px 28px; border-radius: 6px; font-weight: 700; cursor: pointer; text-decoration: none; font-size: 15px; border: none; }\n" +
                "        .btn_checkout { background: #2e7d32; color: white; transition: background 0.2s; }\n" +
                "        .btn_checkout:hover { background: #1b5e20; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <div class=\"app_logo\">Swag Labs</div>\n" +
                "    </div>\n" +
                "    <div class=\"sub_header\">\n" +
                "        <span class=\"title\">Your Cart</span>\n" +
                "    </div>\n" +
                "    <div class=\"cart_container\">\n" +
                "        <div class=\"cart_item\">\n" +
                "            <div>\n" +
                "                <div class=\"cart_item_label\">Sauce Labs Backpack</div>\n" +
                "                <div style=\"color: #666; font-size: 14px; margin-top: 6px;\">Qty: 1 | Sly Pack with laptop protection</div>\n" +
                "            </div>\n" +
                "            <div class=\"cart_item_price\">$29.99</div>\n" +
                "        </div>\n" +
                "        <div class=\"cart_footer\">\n" +
                "            <button id=\"checkout\" class=\"btn_action btn_checkout\" onclick=\"window.location.href='/checkout-step-one.html'\">Checkout</button>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private static String getCheckoutHtml() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Swag Labs - Checkout</title>\n" +
                "    <meta charset='utf-8'>\n" +
                "    <style>\n" +
                "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f4f5f7; margin: 0; }\n" +
                "        .header { background: #13232c; color: white; padding: 16px 36px; display: flex; justify-content: space-between; align-items: center; }\n" +
                "        .header .app_logo { font-size: 26px; font-weight: 800; font-family: 'Arial Black', sans-serif; letter-spacing: 1px; }\n" +
                "        .sub_header { background: white; padding: 14px 36px; border-bottom: 1px solid #e0e0e0; }\n" +
                "        .title { font-size: 22px; font-weight: 700; color: #13232c; }\n" +
                "        .checkout_container { max-width: 480px; margin: 35px auto; background: white; padding: 32px; border-radius: 10px; box-shadow: 0 3px 12px rgba(0,0,0,0.06); }\n" +
                "        .form_group { margin-bottom: 18px; }\n" +
                "        .input_field { width: 100%; box-sizing: border-box; padding: 12px 14px; border: 2px solid #e2e8f0; border-radius: 8px; font-size: 15px; outline: none; transition: border-color 0.2s; }\n" +
                "        .input_field:focus { border-color: #2e7d32; }\n" +
                "        .btn_action { padding: 13px 24px; border-radius: 8px; font-weight: 700; cursor: pointer; font-size: 15px; border: none; }\n" +
                "        .btn_primary { background: #2e7d32; color: white; width: 100%; transition: background 0.2s; }\n" +
                "        .btn_primary:hover { background: #1b5e20; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <div class=\"app_logo\">Swag Labs</div>\n" +
                "    </div>\n" +
                "    <div class=\"sub_header\">\n" +
                "        <span class=\"title\">Checkout: Your Information</span>\n" +
                "    </div>\n" +
                "    <div class=\"checkout_container\">\n" +
                "        <form onsubmit=\"handleContinue(event)\">\n" +
                "            <div class=\"form_group\">\n" +
                "                <input class=\"input_field\" id=\"first-name\" placeholder=\"First Name\" required />\n" +
                "            </div>\n" +
                "            <div class=\"form_group\">\n" +
                "                <input class=\"input_field\" id=\"last-name\" placeholder=\"Last Name\" required />\n" +
                "            </div>\n" +
                "            <div class=\"form_group\">\n" +
                "                <input class=\"input_field\" id=\"postal-code\" placeholder=\"Zip/Postal Code\" required />\n" +
                "            </div>\n" +
                "            <input type=\"submit\" id=\"continue\" class=\"btn_action btn_primary\" value=\"Continue\" />\n" +
                "            <div id=\"finish_section\" style=\"display:none; margin-top: 22px;\">\n" +
                "                <div style=\"padding: 12px; background: #e8f5e9; border: 1px solid #c8e6c9; border-radius: 6px; margin-bottom: 18px; font-size: 14px; color: #2e7d32; font-weight: 600;\">✓ Shipping Information verified! Total: $29.99</div>\n" +
                "                <button type=\"button\" id=\"finish\" class=\"btn_action btn_primary\" onclick=\"window.location.href='/checkout-complete.html'\">Finish</button>\n" +
                "            </div>\n" +
                "        </form>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        function handleContinue(e) {\n" +
                "            e.preventDefault();\n" +
                "            document.getElementById('continue').style.display = 'none';\n" +
                "            document.getElementById('finish_section').style.display = 'block';\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    private static String getConfirmationHtml() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Swag Labs - Order Confirmation</title>\n" +
                "    <meta charset='utf-8'>\n" +
                "    <style>\n" +
                "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f4f5f7; margin: 0; }\n" +
                "        .header { background: #13232c; color: white; padding: 16px 36px; display: flex; justify-content: space-between; align-items: center; }\n" +
                "        .header .app_logo { font-size: 26px; font-weight: 800; font-family: 'Arial Black', sans-serif; letter-spacing: 1px; }\n" +
                "        .sub_header { background: white; padding: 14px 36px; border-bottom: 1px solid #e0e0e0; }\n" +
                "        .title { font-size: 22px; font-weight: 700; color: #13232c; }\n" +
                "        .confirm_container { max-width: 600px; margin: 45px auto; background: white; padding: 45px; border-radius: 10px; text-align: center; box-shadow: 0 3px 12px rgba(0,0,0,0.06); }\n" +
                "        .pony_express { font-size: 68px; margin-bottom: 22px; }\n" +
                "        .complete-header { font-size: 26px; font-weight: 800; color: #2e7d32; margin-bottom: 14px; }\n" +
                "        .complete-text { font-size: 16px; color: #555; line-height: 1.6; margin-bottom: 28px; }\n" +
                "        .btn_back { padding: 12px 28px; background: #13232c; color: white; border: none; border-radius: 6px; font-weight: 700; cursor: pointer; transition: background 0.2s; }\n" +
                "        .btn_back:hover { background: #0a1419; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <div class=\"app_logo\">Swag Labs</div>\n" +
                "    </div>\n" +
                "    <div class=\"sub_header\">\n" +
                "        <span class=\"title\">Checkout: Complete!</span>\n" +
                "    </div>\n" +
                "    <div class=\"confirm_container\">\n" +
                "        <div class=\"pony_express\">📦</div>\n" +
                "        <h2 class=\"complete-header\">Thank you for your order!</h2>\n" +
                "        <div class=\"complete-text\">Your order has been dispatched, and will arrive just as fast as the pony can get there!</div>\n" +
                "        <button class=\"btn_back\" onclick=\"window.location.href='/inventory.html'\">Back Home</button>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
