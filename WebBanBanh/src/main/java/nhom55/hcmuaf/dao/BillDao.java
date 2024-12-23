package nhom55.hcmuaf.dao;


import nhom55.hcmuaf.beans.BillDetails;
import nhom55.hcmuaf.beans.Bills;

import java.time.LocalDateTime;
import java.util.List;

public interface BillDao {

    public boolean addAListProductToBills(LocalDateTime orderedDate, String productList, String status, int user, int payment, String firstName, String lastName, String streetAddress, String city, String phoneNumber, String email, double totalPrice, double deliveryFee, String note);

    public int getIDAListProductFromBills(LocalDateTime orderedDate, int idUser);
    public boolean addAProductToBillDetails(int idProduct , int idBills, int quantity, double totalPrice );

    public boolean degreeAmountWhenOderingSuccessfully(int idProduct, int quantity);

    public List<Bills> getListBills(int idUser);
    public List<BillDetails> getListProductInABill(int idBills);
    public int countTotalRowProductInDatabase();
    public List<Bills> get10BillsForEachPage(int index, int quantityDefault);

    public int countResultSearchingBill(String txtSearch);
    public List<Bills> search(String search, int index, int sizePage);
    public Bills getABill(int id);
    public void updateStatusABill(int idBill, String status);
    public int getIdUser(int idBill);

    public boolean saveSignature(int orderId, int userId, String signature);

    public String getBillHashById(int idBill);

   public boolean updateBillVerifyStatus(int idBill, String verifyStatus);

    public List<Bills> getAllBills();

    public String getBillFeature(int idBill);
    public String getBillDetailsAsString(int idBill);
    public boolean updateBillFeatures(int idBill, String billFeatures);
    public String getEmailByBillId(int billId);

    public String getSignatureById(int idBill);

    public String getPublicKeyByUserId(int userId);

   public String getBillVerifyStatus(int billId);
}
