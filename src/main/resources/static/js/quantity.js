    function updateQuantity(cartProductId, action) {
        var quantityInput = document.getElementById('quantity-' + cartProductId);
        var currentQuantity = parseInt(quantityInput.value);
        
        if (action === 'increase') {
            quantityInput.value = currentQuantity + 1;
        } else if (action === 'decrease') {
            var newQuantity = Math.max(currentQuantity - 1, 1); // Đảm bảo số lượng không nhỏ hơn 1
            quantityInput.value = newQuantity;
        }
        
        // Cập nhật giá tổng
        var priceElement = document.getElementById('price-' + cartProductId);
        var price = parseFloat(priceElement.textContent.replace(/[^0-9.-]+/g,"")); // Lấy giá trị số từ chuỗi
        var totalPrice = price * quantityInput.value;
        priceElement.textContent = formatCurrency(totalPrice); // Định dạng số tiền thành định dạng tiền tệ
    }

    // Định dạng số tiền thành định dạng tiền tệ
    function formatCurrency(amount) {
        return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    }

