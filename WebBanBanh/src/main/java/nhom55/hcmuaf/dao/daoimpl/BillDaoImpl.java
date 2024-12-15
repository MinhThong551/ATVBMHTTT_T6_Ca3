package nhom55.hcmuaf.dao.daoimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nhom55.hcmuaf.beans.BillDetails;
import nhom55.hcmuaf.beans.Bills;
import nhom55.hcmuaf.beans.Products;
import nhom55.hcmuaf.dao.BillDao;
import nhom55.hcmuaf.database.JDBIConnector;

public class BillDaoImpl implements BillDao {

  @Override
  public boolean addAListProductToBills(LocalDateTime orderedDate, String productList,
                                        String status, int userId, int payment, String firstName, String lastName,
                                        String streetAddress, String city, String phoneNumber, String email, double totalPrice,
                                        double deliveryFee, String note) {

    int count = JDBIConnector.get().withHandle(h -> {
      return h.createQuery(
                      "SELECT count(*) FROM bills WHERE productList = :productList and orderedDate = :orderedDate")
              .bind("productList", productList)
              .bind("orderedDate", orderedDate)
              .mapTo(int.class)
              .one();
    });

    if (count > 0) {
      return false;
    } else {
      JDBIConnector.get().withHandle(h -> {
        return h.createUpdate(
                        "INSERT INTO bills(orderedDate, productList, status, userId, payment, firstName, lastName, streetAddress, city, phoneNumber, email,totalPrice,deliveryFee,note,creationTime) "
                                +
                                "VALUES (:orderedDate, :productList, :status, :userId, :payment, :firstName, :lastName, :streetAddress, :city, :phoneNumber, :email, :totalPrice, :deliveryFee, :note, :creationTime)")
                .bind("orderedDate", orderedDate)
                .bind("productList", productList)
                .bind("status", status)
                .bind("userId", userId)
                .bind("payment", payment)
                .bind("firstName", firstName)
                .bind("lastName", lastName)
                .bind("streetAddress", streetAddress)
                .bind("city", city)
                .bind("phoneNumber", phoneNumber)
                .bind("email", email)
                .bind("totalPrice", totalPrice)
                .bind("deliveryFee", deliveryFee)
                .bind("note", note)
                .bind("creationTime", LocalDateTime.now())
                .execute();
      });

      return true;
    }
  }

  @Override
  public int getIDAListProductFromBills(LocalDateTime orderedDate, int idUser) {
    int result = 0;
    result = JDBIConnector.get().withHandle(handle -> {
      return handle.createQuery(
                      "SELECT id from bills where orderedDate = :orderedDate and userId = :idUser ")
              .bind("orderedDate", orderedDate)
              .bind("idUser", idUser)
              .mapTo(int.class).one();
    });

    return result;
  }

  @Override
  public boolean addAProductToBillDetails(int idProduct, int idBills, int quantity,
                                          double totalPrice) {
    int count = 0;
    count = JDBIConnector.get().withHandle(h -> {
      return h.createQuery(
                      "SELECT count(*) from bill_details where idProduct = :idProduct and idBills= :idBills")
              .bind("idProduct", idProduct)
              .bind("idBills", idBills)
              .mapTo(int.class).one();
    });
    if (count > 0) {
      return false;
    } else {
      JDBIConnector.get().withHandle(h -> {
        return h.createUpdate(
                        "INSERT INTO bill_details(quantity,totalPrice,idProduct,idBills) values (:quantity,:totalPrice,:idProduct,:idBills)")
                .bind("idProduct", idProduct)
                .bind("idBills", idBills)
                .bind("quantity", quantity)
                .bind("totalPrice", totalPrice).execute();
      });

      return true;
    }
  }

  @Override
  public boolean degreeAmountWhenOderingSuccessfully(int idProduct, int quantity) {
    JDBIConnector.get().withHandle(h -> {
      return h.createUpdate("UPDATE products set weight = weight -:quantity where id = :idProduct")
              .bind("quantity", quantity)
              .bind("idProduct", idProduct)
              .execute();
    });
    return true;
  }

  @Override
  public List<Bills> getListBills(int idUser) {
    return JDBIConnector.get().withHandle(h -> {
      return h.createQuery("SELECT * from bills where userId = :idUser")
              .bind("idUser", idUser)
              .mapToBean(Bills.class).stream().toList();
    });
  }

  @Override
  public List<BillDetails> getListProductInABill(int idBills) {
    return JDBIConnector.get().withHandle(handle -> {

      String sql =
              "SELECT bd.id, bd.quantity, bd.totalPrice, p.id as idProduct, p.nameOfProduct, p.description, p.price,p.imgPublicId,p.dateOfImporting,p.category "
                      +
                      "FROM bill_details bd " +
                      "JOIN products p ON bd.idProduct = p.id " +
                      "WHERE bd.idBills = ?";

      return handle.createQuery(sql)
              .bind(0, idBills)
              .map((rs, ctx) -> {
                BillDetails bd = new BillDetails();
                bd.setId(rs.getInt("id"));
                bd.setQuantity(rs.getInt("quantity"));
                bd.setTotalPrice(rs.getDouble("totalPrice"));

                Products p = new Products();
                p.setId(rs.getInt("idProduct"));
                p.setNameOfProduct(rs.getString("nameOfProduct"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setImgPublicId(rs.getString("imgPublicId"));
                p.setDateOfImporting(rs.getDate("dateOfImporting"));
                p.setCategory(rs.getString("category"));
                bd.setProducts(p);

                return bd;
              }).stream().toList();
    });
  }

  @Override
  public int countTotalRowProductInDatabase() {
    return JDBIConnector.get().withHandle(h ->
            h.createQuery("SELECT COUNT(id) FROM bills").mapTo(Integer.class).one()
    );
  }

  @Override
  public List<Bills> get10BillsForEachPage(int index, int quantityDefault) {
    List<Bills> result = new ArrayList<>();
    int start = (index - 1) * quantityDefault;

    result = JDBIConnector.get().withHandle(h ->
            h.createQuery(
                            "SELECT * FROM bills ORDER BY orderedDate DESC LIMIT :start, :quantityDefault")
                    .bind("start", start)
                    .bind("quantityDefault", quantityDefault)
                    .mapToBean(Bills.class)
                    .list()
    );

    return result;
  }

  @Override
  public int countResultSearchingBill(String txtSearch) {
    return JDBIConnector.get().withHandle(h ->
            h.select("SELECT count(*) FROM bills WHERE firstName LIKE ? OR lastName LIKE ?",
                            "%" + txtSearch + "%", "%" + txtSearch + "%")
                    .mapTo(Integer.class)
                    .one()
    );
  }

  @Override
  public List<Bills> search(String search, int index, int sizePage) {
    List<Bills> result = JDBIConnector.get().withHandle(handle -> {
      handle.begin();
      try {
        List<Bills> resultList = handle.createQuery(
                        "with testThu as (select ROW_NUMBER() over (order by " + "orderedDate"
                                + "  desc) as r,id, orderedDate, productList, status, userId , payment , firstName, lastName, streetAddress, city, phoneNumber,email,totalPrice from bills where firstName LIKE ? OR lastName LIKE ?)\n"
                                +
                                "\n" +
                                "select * FROM testThu where r between " + (index * sizePage - 9) + " and " + (
                                index * sizePage))
                .bind(0, "%" + search + "%")
                .bind(1, "%" + search + "%")
                .mapToBean(Bills.class)
                .list();
        handle.commit();
        return resultList;
      } catch (Exception e) {
        handle.rollback();
        throw e;
      }
    });
    return result;
  }

  @Override
  public Bills getABill(int id) {
    return JDBIConnector.get().withHandle(h -> {
      return h.createQuery("SELECT * from bills where id = :id")
              .bind("id", id)
              .mapToBean(Bills.class).one();
    });
  }

  @Override
  public void updateStatusABill(int idBill, String status) {
    JDBIConnector.get().withHandle(h -> {
      return h.createUpdate("update bills set status = :status where id = :idBill")
              .bind("status", status)
              .bind("idBill", idBill)
              .execute();
    });
  }

  @Override
  public int getIdUser(int idBill) {
    return JDBIConnector.get().withHandle(h -> {
      return h.createQuery("Select userId from bills where id = :idBill")
              .bind("idBill", idBill)
              .mapTo(Integer.class).one();
    });
  }

  @Override
  public boolean saveSignature(int orderId, int userId, String signature) {
    return JDBIConnector.get().withHandle(handle ->
            handle.createUpdate("UPDATE bills SET signature = :signature WHERE id = :orderId AND userId = :userId")
                    .bind("signature", signature)
                    .bind("orderId", orderId)
                    .bind("userId", userId)
                    .execute() > 0 // Trả về true nếu ít nhất một hàng được cập nhật
    );
  }

  @Override
  public String getBillHashById(int idBill) {
    return JDBIConnector.get().withHandle(handle ->
            handle.createQuery("SELECT billHash FROM bills WHERE id = :idBill")
                    .bind("idBill", idBill)
                    .mapTo(String.class)
                    .findOnly()
    );
  }


  @Override
  public boolean updateBillVerifyStatus(int idBill, String verifyStatus) {
    int rowsUpdated = JDBIConnector.get().withHandle(handle ->
            handle.createUpdate("UPDATE bills SET verify = :verifyStatus WHERE id = :idBill")
                    .bind("verifyStatus", verifyStatus)
                    .bind("idBill", idBill)
                    .execute()
    );
    return rowsUpdated > 0;  // Trả về true nếu có ít nhất 1 dòng bị ảnh hưởng
  }


  @Override
  public List<Bills> getAllBills() {
    return JDBIConnector.get().withHandle(handle ->
            handle.createQuery("SELECT * FROM bills")
                    .mapToBean(Bills.class)  // Chuyển kết quả thành danh sách đối tượng Bills
                    .list()
    );
  }


  /**
   * Lấy chuỗi đặc điểm (dữ liệu tất cả các cột) của một đơn hàng, định dạng text và phân tách bằng dấu "-".
   * @param idBill ID của đơn hàng
   * @return Chuỗi chứa các đặc điểm của đơn hàng, hoặc null nếu không tìm thấy.
   */
  @Override
  public String getBillDetailsAsString(int idBill) {
    try {
      Bills bill = getABill(idBill);
      if (bill == null) {
        return null; // Không tìm thấy đơn hàng
      }

      // Tạo một chuỗi để chứa tất cả các đặc điểm, phân tách bằng dấu "-"
      return String.format("%d-%s-%s-%s-%d-%d-%s-%s-%s-%s-%s-%s-%.2f-%s-%.2f-%s-%s",
              bill.getId(),
              bill.getOrderedDate().toString(), // Chuyển LocalDateTime thành String
              bill.getProductList(),
              bill.getStatus(),
              bill.getUserId(),
              bill.getPayment(),
              bill.getFirstName(),
              bill.getLastName(),
              bill.getStreetAddress(),
              bill.getCity(),
              bill.getPhoneNumber(),
              bill.getEmail(),
              bill.getTotalPrice(),
              bill.getCreationTime().toString(), // Chuyển LocalDateTime thành String
              bill.getDeliveryFee(),
              bill.getNote() == null ? "" : bill.getNote(),
              bill.getVerify().toString()
      );
    } catch (Exception e) {
      e.printStackTrace();
      return null; // Xử lý lỗi nếu có
    }
  }

  public static void main(String[] args) {
    BillDaoImpl billDao = new BillDaoImpl();
    String billDetails = billDao.getBillDetailsAsString(7);
    if (billDetails != null) {
      System.out.println("Bill Details: " + billDetails);
    } else {
      System.out.println("Could not retrieve bill details.");
    }
  }
}