<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zxx">

<head>
  <meta charset="UTF-8">
  <meta name="description" content="Anime Template">
  <meta name="keywords" content="Anime, unica, creative, html">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Anime</title>

  <!-- Google Font -->
  <link
          href="https://fonts.googleapis.com/css2?family=Oswald:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
  <link
          href="https://fonts.googleapis.com/css2?family=Mulish:wght@300;400;500;600;700;800;900&display=swap"
          rel="stylesheet">

  <!-- Css Styles -->
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.25/css/jquery.dataTables.css">
  <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
  <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
  <link rel="stylesheet" href="css/elegant-icons.css" type="text/css">
  <link rel="stylesheet" href="css/plyr.css" type="text/css">
  <link rel="stylesheet" href="css/nice-select.css" type="text/css">
  <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
  <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
  <link rel="stylesheet" type="text/css" href="css/ds/style.css"/>
  <link rel="stylesheet" href="https://kit.fontawesome.com/fe2c9f6253.css"
        crossorigin="anonymous">
  <script src="https://kit.fontawesome.com/9847adceef.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@8"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
  <style>
    #key-list-table tbody tr td:nth-child(3){
      white-space:nowrap;
      text-overflow : ellipsis;
      overflow : hidden;

    }
  </style>
</head>

<body>
<fmt:setLocale value="${sessionScope.LANG}"/>
<fmt:setBundle basename="app"/>


<c:url var="urlAvatar"
       value="${request.rervletContext.realPath}${sessionScope.user.avatar}"/>

<div id="ah_wrapper">

  <!-- Page Preloder -->
  <div id="preloder">
    <div class="loader"></div>
  </div>

  <!-- Header Section Begin -->
  <c:import url="/anime-main/header.jsp"/>

  <!-- Header End -->

  <!-- Hero Section Begin -->
  <section class="">

    <div class="container">
      <span class="addKey float-right mb-3 mr-2">
                              <button class="btn btn-outline-light"><i
                                      class="ri-add-fill"><span class="pl-1">Nhập key</span></i>
                              </button>
                              </span>
      <span class="createKey float-right mb-3 mr-2">
                              <button class="btn btn-outline-light"><i
                                      class="ri-add-fill" type="button" onclick="createKey()"><span class="pl-1">Tạo key</span></i>
                              </button>
                              </span>
      <div id="addArea">

      </div>
      <table id="key-list-table"
             class="table table-striped table-bordered mt-4" role="grid"
             aria-describedby="user-list-page-info" data-stripe-classes="[]" style="text-align: center">
        <thead>
        <tr>
          <th></th>
          <th>id</th>
          <th>Public key</th>
          <th>Ngày tạo</th>
          <th >Ngày hết hạn</th>
          <th >Ngày hủy</th>
          <th >Trạng thái</th>
          <th>Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${requestScope.keyList}">
          <tr style="background-color:rgba(0,0,0,.05)">
            <th></th>
            <td>${item.id}</td>
            <td style="
    max-width: 200px;
    word-break: break-all;" title="${item.key}" onclick="copyToClipboard(this)">${item.key}</td>
            <jsp:useBean id="formatter" class="services.DateTimeFormat"/>
            <td>${formatter.format(item.dayReceive)}</td>
            <td id="dayExpired">${formatter.format(item.dayExpired)}</td>
            <td id="dayCanceled">${formatter.format(item.dayCanceled)}</td>
            <td id="renderIsActive">
              <c:if test="${item.status==1}">
              <span class="status iq-bg-success">
															</c:if>
															<c:if test="${item.status==0}">
																<span class="status iq-bg-danger">
															</c:if>
                                                                    ${item.statusDescription}</span>
            </td>
            <td>
              <c:if test="${item.status==1}">
                <button class="btn btn-danger" type="button" id="lostBt" onclick="lostKey(this)">
                  <fmt:message>button.lost</fmt:message>
                </button>
              </c:if>
            </td>
          </tr>

        </c:forEach>
        </tbody>
      </table>

    </div>
  </section>
  <!-- Footer Section Begin -->
  <c:import url="/anime-main/footer.jsp"/>
  <!-- Footer Section End -->
</div>
<!-- Js Plugins -->

<script src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>

<script src="https://cdn.datatables.net/buttons/2.3.6/js/dataTables.buttons.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.3.6/js/buttons.print.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/player.js"></script>
<script src="js/jquery.nice-select.min.js"></script>
<script src="js/mixitup.min.js"></script>
<script src="js/jquery.slicknav.js"></script>
<script src="js/owl.carousel.min.js"></script>
<script src="js/main.js"></script>
<script type="text/javascript">
</script>
<script>
  var tableKey;
  function copyToClipboard(element) {
    var $temp = $("<input>");
    $("body").append($temp);
    $temp.val($(element).html()).select();
    document.execCommand("copy");
    $temp.remove();
    Swal.fire(
            'Good job!',
            'Sao chép khóa thành công!',
            'success'
    )
  }
  $(document).ready(function () {
    tableKey=$('#key-list-table').DataTable({
      "stripeClasses": [],
      rowId: 'row_num',
      columnDefs: [ {
        targets: 0,
        data: null,
        defaultContent: '',
        title: 'STT',
        className: 'dt-center',
        orderable: false,
        searchable: false,
        render: function (data, type, row, meta) {

          return meta.row + 1;
        }
      } ],
      order: [[ 0, 'asc' ]],
      "searching": true,
      "sorting": true,
      dom: 'Bfrtip',
      buttons: [
        {
          extend: 'copy',
          className: 'btn btn-outline-primary'
        },
        {
          extend: 'csv',
          className: 'btn btn-outline-info'
        },
        {
          extend: 'excel',
          className: 'btn btn-outline-success'
        },
        {
          extend: 'pdf',
          className: 'btn btn-outline-warning'
        },
        {
          extend: 'print',
          className: 'btn btn-outline-danger'
        }
      ],
    });

  });
  let addRow=`<table class="table table-striped table-bordered" >
              <thead>
                                    <tr>
                                        <th>Public Key</th>
                                        <th>Ngày hết hạn</th>
                                        <th>Option</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                 <tr id="addKey">


                                            <td contenteditable="true" id="addPublicKey" style="
    max-width: 200px;
    word-break: break-all;"></td>
                                            <td contenteditable="true"><input name="addDayExpired" id="addDayExpired" type="date"></td>
                                            <td>
                                               <span class="table-remove">
                                               <button type="button" class="btn btn-sm iq-bg-success btn-rounded btn-sm my-0" style="background:#ecfff6 !important;color: #65f9b3!important" onclick="submitAdd(this)" id="submitAddButton">Submit</button>
                                                </span>
                                            </td>
                                        </tr>
                                        </tbody>
                                        </table>
            `
  $(".addKey").on("click",()=>{
    if ($("#addKey").length) {
      $("#addArea").html(``);
    } else {
      $("#addArea").html(addRow);
      let button = $("#submitAddButton");
      let tr = $(button).closest("tr");
      let tdArray = $(tr).find("td");
      validate(tdArray[1],button, tdArray[0]);
      enableSubmit(tdArray[1],button, tdArray[0]);

    }
  });

  function validate(dayExpired,submit, ...tds) {

    tds.forEach(td => {
      td.addEventListener("input", () => {
        enableSubmit(dayExpired,submit, ...tds);
      });
    })
    $($(dayExpired).find("#addDayExpired")).on("change",()=>{
      enableSubmit(dayExpired,submit, ...tds);
    })
  }

  function enableSubmit(dayExpired,submit, ...tds) {
    let isNotEmpty = true;
    tds.forEach(td => {
      isNotEmpty &= $(td).text().trim() !== "";
    })
    isNotEmpty&=$($(dayExpired).find("#addDayExpired")).val()!==""
    if (isNotEmpty) {
      $(submit).prop('disabled', false);
    } else {
      $(submit).prop('disabled', true);

    }
  }
  function submitAdd(button){
    let tr = $(button).closest("tr");
    let tdArray = $(tr).find("td");

    Swal.fire({
      title: 'Xác nhận?',
      text: "Bạn chắc chắn thực hiện thao tác này!",
      icon: 'info',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, add it!'
    }).then((result) => {
      if (result.isConfirmed) {
        let addPublicKey = $(tdArray[0]).text();
        let addDayExpired = $($(tdArray[1]).find("#addDayExpired")).val();
        document.getElementById("loadingAnime").style.display="block";
        $.ajax({
          url: "AddPublicKey",
          type: "post",
          data: {
            addPublicKey:addPublicKey,
            addDayExpired : addDayExpired,
          },
          success: function (data) {
            document.getElementById("loadingAnime").style.display="none";
            if (data.status === 400) {
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: data.responseText,
              });
              return;
            }

            let key = JSON.parse((JSON.parse(data)).newKey);
            let status = key.status;
            let statusString = key.status==1?`<span class="status iq-bg-success">`:`<span class="status iq-bg-danger">`;
            let bt = key.status==1?`<button class="btn btn-danger"type="button" id="lostBt" onclick="lostKey(this)">
                      <fmt:message>button.lost</fmt:message>
                    </button>`:"";
              let appendInsert=`<tr style="background-color:rgba(0,0,0,.05)"> <td></td>`
            +`<td>`+key.id+`</td>
            <td style="
    max-width: 200px;
    word-break: break-all;" title="`+key.key+`" onclick="copyToClipboard(this)">`+key.key+`</td>
            <td  >`+key.dayReceive+`</td>
            <td id="dayExpired">`+key.dayExpired+`</td>
            <td id="dayCanceled">`+key.dayCanceled+`</td>
            <td>
              <div id="renderIsActive">`+statusString+
                      key.statusDescription+`</span>
            </div>
            </td>
            <td>
             `+bt+`
            </td>
          </tr>`;
              tableKey.rows.add($(appendInsert)).draw();
              $("#addKey").remove();
              Swal.fire(
                      'Good job!',
                      'Thêm thành công!',
                      'success'
              )
          },
          error: function (xhr) {
            document.getElementById("loadingAnime").style.display="none";
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: xhr.responseText,
              });

          }
        });
      }
    })

  }
  function lostKey(button){
    Swal.fire({
      title: 'Xác nhận?',
      text: "Bạn đã lộ hoặc mất private key và chắc chắn vô hiệu hóa chữ kí hiện tại ?!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Xác nhận!'
    }).then((result) => {
      if (result.isConfirmed) {
        document.getElementById("loadingAnime").style.display="block";
        $.ajax({
          url: "LostKey",
          type: "post",
          success: function (data) {
            document.getElementById("loadingAnime").style.display="none";
            let dataParse = JSON.parse(data);
            let isSuccess = dataParse.isSuccess;
            let keyDisable = JSON.parse(dataParse.key);

            if (isSuccess) {

              $(button).closest("tr").find("#dayExpired").html(keyDisable.dayExpired);
              $(button).closest("tr").find("#dayCanceled").html(keyDisable.dayCanceled);
              $(button).closest("tr").find("#renderIsActive").html(`<span class="status iq-bg-danger">Đã hết hạn</span>`);
              $(button).remove();
              Swal.fire({
                icon: 'success',
                title: 'Thành công',
                text: 'Vô hiệu hóa thành công!',
              })
            }else{

              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: "Hiện tại không có chữ ký nào còn hoạt động để vô hiệu hóa",
              });
            }
          },
          error: function (xhr) {
            document.getElementById("loadingAnime").style.display="none";
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: xhr.responseText,
            });

          }
        });
      }
    })

  }
  function createKey(){
    Swal.fire({
      title: 'Xác nhận?',
      text: "Bạn muốn nhận chữ kí mới và chắc chắn vô hiệu hóa chữ kí hiện tại ?!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Xác nhận!'
    }).then((result) => {
      if (result.isConfirmed) {
        document.getElementById("loadingAnime").style.display="block";
        $.ajax({
          url: "CreateKey",
          type: "post",
          success: function (data) {
            document.getElementById("loadingAnime").style.display="none";
               let key = JSON.parse((JSON.parse(data)).key);
                 let statusString = key.status==1?`<span class="status iq-bg-success">`:`<span class="status iq-bg-danger">`;
                 let bt = key.status==1?`<button class="btn btn-danger" id="lostBt" type="button" onclick="lostKey(this)">
                      <fmt:message>button.lost</fmt:message>
                    </button>`:"";
                 let appendInsert=`<tr style="background-color:rgba(0,0,0,.05)"> <td></td>`
                         +`<td>`+key.id+`</td>
            <td style="
    max-width: 200px;
    word-break: break-all;" title="`+key.key+`" onclick="copyToClipboard(this)">`+key.key+`</td>
            <td >`+key.dayReceive +`</td>
            <td id="dayExpired">`+key.dayExpired+`</td>
            <td id="dayCanceled"></td>
            <td>
              <div id="renderIsActive">`+statusString+
                         key.statusDescription+`</span>
            </div>
            </td>
            <td>
             `+bt+`
            </td>
          </tr>`;
                 let firstRow = $($("#key-list-table").find("tr")[1]);
                 let btLost = $($(firstRow).find("#lostBt"));
                 if(btLost!=null){
                 $(btLost).remove();
                 }
                 $($(firstRow).find("#dayCanceled")).html(key.dayReceive);
                 $($(firstRow).find("#dayExpired")).html(key.dayReceive);
                 $($(firstRow).find("#renderIsActive")).html(`<span class="status iq-bg-danger">Đã hết hạn</span>`);
                 $("#key-list-table").prepend(appendInsert);
                 Swal.fire({
                   icon: 'success',
                   title: 'Thành công',
                   text: 'Tạo chữ ký thành công, private key đã được gửi qua email của bạn!',
                 })
          },
          error: function (xhr) {
            document.getElementById("loadingAnime").style.display="none";
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: xhr.responseText,
            });
          }
        });
      }
    })
  }
</script>
</body>
</html>