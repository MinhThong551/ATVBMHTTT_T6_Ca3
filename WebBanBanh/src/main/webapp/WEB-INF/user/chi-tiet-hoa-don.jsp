<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 26/03/2024
  Time: 5:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <fmt:setLocale value="vi_VN"/>
    <%@ page isELIgnored="false" %>
    <title>Chi tiết hóa đơn</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Lora:400,400i,700,700i&display=swap"
          rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Amatic+SC:400,700&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/open-iconic-bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/web-css/animate.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/owl.carousel.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/owl.theme.default.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/magnific-popup.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/web-css/aos.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/ionicons.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/bootstrap-datepicker.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/jquery.timepicker.css">


    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/web-css/flaticon.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/web-css/icomoon.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/web-css/style.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/user-css/user-profile.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/user-css/chi-tiet-hoa-don.css">
    <style>
        .bill-info-container {
            display: flex;
            flex-direction: column;
            gap: 10px;
            margin-top: 20px;
        }

        .bill-info {
            display: flex;
            align-items: center; /* Căn giữa theo chiều dọc */
            gap: 10px;
        }

        .bill-info p, .bill-info label {
            margin: 0;
            white-space: nowrap;
        }

        .bill-info #billHash{
            font-family: monospace; /* or another monospaced font */
            font-size: 14px;
            color: #555;
            word-wrap: break-word;
            max-width: 80%;
            display: inline-block;
            vertical-align: middle;

        }
        .bill-info .copy-button {
            background: #f0f0f0;
            border: 1px solid #ccc;
            border-radius: 5px;
            cursor: pointer;
            display: inline-flex;
            padding: 5px;

            align-items: center;
        }
        .bill-info .copy-button svg{
            width: 1em; /* Set the width */
            height: 1em;
            display: block;
            fill: #555;
        }
        .bill-info input[type="text"] {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 300px;
            box-sizing: border-box;

        }
        .verify-button {
            margin-top: 20px;
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .verify-button button {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            background-color: #4caf50;
            color: white;
            cursor: pointer;
        }
        .verify-button button:hover{
            background-color: #45a049;
        }
        .verify-button p a{
            text-decoration: none;
        }
    </style>

</head>
<body class="goto-here">
<nav class="navbar-container navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light"
     id="ftco-navbar">
    <div class="container navbar-container">
        <div class="navbar-brand">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/page/home">Cửa Hàng
                Trái Cây</a>
        </div>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ftco-nav"
                aria-controls="ftco-nav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="oi oi-menu"></span> Menu
        </button>

        <div class="navbar-account">
            <div class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="dropdown05" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">Thông tin người dùng</a>
                <div class="dropdown-menu account-menu" aria-labelledby="dropdown04">
                    <a class="account dropdown-item"
                       href="${pageContext.request.contextPath}/page/user/user-profile">
                        <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512">
                            <!--! Font Awesome Free 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. -->
                            <path
                                    d="M224 256A128 128 0 1 0 224 0a128 128 0 1 0 0 256zm-45.7 48C79.8 304 0 383.8 0 482.3C0 498.7 13.3 512 29.7 512H418.3c16.4 0 29.7-13.3 29.7-29.7C448 383.8 368.2 304 269.7 304H178.3z"/>
                        </svg>
                        Người Dùng
                    </a>
                    <a class="account dropdown-item"
                       href="${pageContext.request.contextPath}/page/logout">
                        <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512">
                            <!--! Font Awesome Free 6.4.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2023 Fonticons, Inc. -->
                            <path
                                    d="M377.9 105.9L500.7 228.7c7.2 7.2 11.3 17.1 11.3 27.3s-4.1 20.1-11.3 27.3L377.9 406.1c-6.4 6.4-15 9.9-24 9.9c-18.7 0-33.9-15.2-33.9-33.9l0-62.1-128 0c-17.7 0-32-14.3-32-32l0-64c0-17.7 14.3-32 32-32l128 0 0-62.1c0-18.7 15.2-33.9 33.9-33.9c9 0 17.6 3.6 24 9.9zM160 96L96 96c-17.7 0-32 14.3-32 32l0 256c0 17.7 14.3 32 32 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32l-64 0c-53 0-96-43-96-96L0 128C0 75 43 32 96 32l64 0c17.7 0 32 14.3 32 32s-14.3 32-32 32z"/>
                        </svg>
                        Đăng Xuất
                    </a>
                </div>
            </div>
        </div>
    </div>
</nav>
<!-- END nav -->
<div class="main-user-content" style="background-color: #e7e6e6; width: 100%; height: 100%">
    <div class="container">
        <div class="main-container">
            <div class="ds-hoa-don-link">
                <a href="${pageContext.request.contextPath}/page/bill/list-bill">
                    <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512">
                        <path
                                d="M459.5 440.6c9.5 7.9 22.8 9.7 34.1 4.4s18.4-16.6 18.4-29V96c0-12.4-7.2-23.7-18.4-29s-24.5-3.6-34.1 4.4L288 214.3V256v41.7L459.5 440.6zM256 352V256 128 96c0-12.4-7.2-23.7-18.4-29s-24.5-3.6-34.1 4.4l-192 160C4.2 237.5 0 246.5 0 256s4.2 18.5 11.5 24.6l192 160c9.5 7.9 22.8 9.7 34.1 4.4s18.4-16.6 18.4-29V352z"/>
                    </svg>
                    Về danh sách hóa đơn
                </a>
            </div>
            <div class="header-tag">
                <h1>Chi tiết hóa đơn</h1>
            </div>
            <div class="ds-sp">
                <table class="table-ds-hoa-don">
                    <tr>
                        <th style="width: 50px; font-weight: normal"></th>
                        <th style="width: 200px;">Danh sách sản phẩm</th>
                        <th style="width: 350px; text-align: center">Tên & mô tả</th>
                        <th style="width: 150px;">Giá</th>
                        <th style="width: 120px;">Số lượng</th>
                        <th style="width: 150px;">Tổng tiền</th>
                    </tr>
                    <c:set var="totalPrice" value="0"/>
                    <c:forEach items="${list}" var="product">
                        <tr>
                            <td class="product-single">
                                <a href="#">
                                    <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512">
                                        <path
                                                d="M40 48C26.7 48 16 58.7 16 72v48c0 13.3 10.7 24 24 24H88c13.3 0 24-10.7 24-24V72c0-13.3-10.7-24-24-24H40zM192 64c-17.7 0-32 14.3-32 32s14.3 32 32 32H480c17.7 0 32-14.3 32-32s-14.3-32-32-32H192zm0 160c-17.7 0-32 14.3-32 32s14.3 32 32 32H480c17.7 0 32-14.3 32-32s-14.3-32-32-32H192zm0 160c-17.7 0-32 14.3-32 32s14.3 32 32 32H480c17.7 0 32-14.3 32-32s-14.3-32-32-32H192zM16 232v48c0 13.3 10.7 24 24 24H88c13.3 0 24-10.7 24-24V232c0-13.3-10.7-24-24-24H40c-13.3 0-24 10.7-24 24zM40 368c-13.3 0-24 10.7-24 24v48c0 13.3 10.7 24 24 24H88c13.3 0 24-10.7 24-24V392c0-13.3-10.7-24-24-24H40z"/>
                                    </svg>
                                </a>
                            </td>
                            <td class="img-default" data-assets="${product.getProducts().getImgPublicId()}">
                                <img class="img-product"
                                     src="${pageContext.request.contextPath}/static/images/loading-cat.gif">
                            </td>
                            <td>
                                <div class="product-name">
                                    <h6>${product.getProducts().getNameOfProduct()}</h6>
                                </div>
                            </td>
                            <td>${product.getProducts().getPrice()}</td>
                            <td>
                                <div class="quantity">
                                    <span>${product.getQuantity()}</span>
                                </div>
                            </td>
                            <td>${product.getTotalPrice()}</td>
                            <c:set var="totalPrice" value="${totalPrice + product.getTotalPrice()}"/>
                        </tr>
                    </c:forEach>
                </table>
                <div style="position:relative; left:800px">
          <span style="color:#82ae46; font-size:20px">Tổng cộng: <fmt:formatNumber pattern="#,##0 ₫"
                                                                                   value="${totalPrice}"/></span>
                </div>

            </div>
            <form id="signatureForm" method="post" action="${pageContext.request.contextPath}/page/bill/detail">
                <div class="bill-info-container">
                    <div class="bill-info">
                        <p>Chuỗi đặc điểm:</p>
                        <span id="billFeatures">${billFeatures}</span>
                        <input type="hidden" name="billFeatures" value="${billFeatures}">
                    </div>
                    <div class="bill-info">
                        <p>Mã Hash:</p>
                        <span id="billHash">${billHash}</span>
                        <span class="copy-button" onclick="copyToClipboard('#billHash')">
                     <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512">
                           <path d="M208 0H332.1c12.7 0 24.9 5.1 33.9 14.1l67.9 67.9c9 9 14.1 21.2 14.1 33.9V336c0 26.5-21.5 48-48 48H208c-26.5 0-48-21.5-48-48V48c0-26.5 21.5-48 48-48zM48 128h80v64H64V448H256v-64h64v80c0 8.8-7.2 16-16 16H48c-8.8 0-16-7.2-16-16V144c0-8.8 7.2-16 16-16zm272 64H144V480H288c17.7 0 32-14.3 32-32V192z"/>
                      </svg>
                  </span>
                    </div>

                    <div class="bill-info">
                        <label for="signature">Chữ ký điện tử:</label>
                        <input type="text" id="signature" name="signature">
                    </div>
                </div>


                <div class="verify-button">
                    <button type="button" onclick="verifySignature()">Xác thực chữ ký</button>
                    <button type="button" onclick="cancelVerification()">Hủy</button>
                    <p><a href="#">Về trang chủ</a></p>
                </div>
            </form>
        </div>
    </div>
</div>


<!-- loader -->
<div id="ftco-loader" class="show fullscreen">
    <svg class="circular" width="48px" height="48px">
        <circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4"
                stroke="#eeeeee"/>
        <circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4"
                stroke-miterlimit="10"
                stroke="#F96D00"/>
    </svg>
</div>


<script src="${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery-migrate-3.0.1.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.easing.1.3.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.waypoints.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.stellar.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.magnific-popup.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/aos.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.animateNumber.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap-datepicker.js"></script>
<script src="${pageContext.request.contextPath}/static/js/scrollax.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/cloudinary-core/2.11.2/cloudinary-core-shrinkwrap.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBVWaKrjvy3MaE7SQ74_uJiULgl1JY0H2s&sensor=false"></script>
<script src="${pageContext.request.contextPath}/static/js/google-map.js"></script>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
<script src="${pageContext.request.contextPath}/static/js/web-js/chi-tiet-hoa-don.js"></script>

<script>
    function copyToClipboard(element) {
        var $temp = $("<input>");
        $("body").append($temp);
        $temp.val($(element).text()).select();
        document.execCommand("copy");
        $temp.remove();
        alert("Đã sao chép mã hash!");
    }

    function verifySignature() {
        document.getElementById('signatureForm').submit();
        //  call verify api to verify signature

    }

    function cancelVerification() {
        // TODO: Implement cancel action, e.g., redirect to another page
        window.location.href = "${pageContext.request.contextPath}/page/bill/list-bill";
    }
</script>

</body>
</html>