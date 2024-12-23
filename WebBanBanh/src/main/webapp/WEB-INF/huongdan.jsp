
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hướng Dẫn Xác Thực </title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            padding-left: 20px;
            padding-right: 20px;
            background-color: #fff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        h1, h2 {
            text-align: center;
            color: #2c3e50;
        }

        h1 {
            color: #6F4F28;
        }

        h2 {
            color: #6F4F28;
        }

        ol {
            line-height: 1.8;
            margin-left: 0;
            padding-left: 0;
        }

        .step {
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 15px;
            margin: 15px 0;
            text-align: left;
        }
        .step h2 {
            text-align: left;
        }
        .step img {
            max-width: 100%;
            border-radius: 5px;
            margin-bottom: 10px;
        }

        .back-button {
            position: absolute;
            top: 20px;
            left: 20px;
            font-size: 16px;
            color: #fff;
            background-color: #2c3e50;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }

        .back-button:hover {
            background-color: #16a085;
        }
        .brown-button {
            background-color: #6F4F28;  /* Màu nâu */
            color: white;  /* Màu chữ trắng */
            padding: 10px 20px;  /* Khoảng cách nội dung */
            border: none;  /* Không viền */
            border-radius: 5px;  /* Bo góc */
            cursor: pointer;  /* Hiệu ứng chuột */
        }

        .brown-button:hover {
            background-color: #4E3629;  /* Màu nâu đậm hơn khi hover */
        }


    </style>
</head>
<body>
<!-- Button quay về trang thanh toán -->
<form action="/page/order/check-out" method="post">
    <button type="submit" class="brown-button">Quay lại trang thanh toán</button>
</form>


<div class="container">







    <!-- Các tiêu đề được căn giữa -->
    <h1>CỬA HÀNG BÁNH </h1>
    <h2>Hướng dẫn xác thực đơn hàng</h2>

    <ol>
        <li class="step">
            <h2>Bước 1: Tạo khóa và lưu khóa</h2>
            <p>Sau khi tạo đơn hàng thành công, quý khách vui lòng vào email để xác thực đơn hàng, tạo khóa và lưu khóa trên trang chủ của hệ thống.</p>
            <img src="/static/images/buoc1.jpg" alt="Đăng nhập vào hệ thống">
        </li>

        <li class="step">
            <h2>Bước 2: Mở công cụ ký chữ ký điện tử</h2>
            <p>Khách hàng điền dữ liệu cần ký và lấy khóa riêng đã gửi đến email.</p>
            <img src="/static/images/buoc2.jpg" alt="Chọn tính năng">
        </li>

        <li class="step">
            <h2>Bước 3: Nhập chữ ký và bấm xác thực</h2>
            <p>Nhập chữ ký điện tử và bấm nút xác thực đơn hàng.</p>
            <img src="/static/images/buoc3.jpg" alt="Nhập dữ liệu vào form">
        </li>

        <li class="step">
            <h2>Bước 4: Đơn hàng đã được xác thực thành công</h2>
            <p>Sau khi ký thành công đơn hàng, trang chủ sẽ hiện ra thông báo đã xác thực đơn hàng thành công.</p>
            <img src="/static/images/buoc4.jpg" alt="Kiểm tra thông tin">
        </li>
    </ol>
</div>

</body>
</html>

