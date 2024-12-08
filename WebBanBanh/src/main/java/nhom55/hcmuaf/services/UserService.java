package nhom55.hcmuaf.services;

import nhom55.hcmuaf.beans.Users;
import nhom55.hcmuaf.dao.UsersDao;
import nhom55.hcmuaf.dao.daoimpl.UsersDaoImpl;
import nhom55.hcmuaf.util.MyUtils;

import java.util.Date;
import java.util.List;

public class UserService {

  private static UserService instance;
  private UsersDao userDao;

  public UserService() {
    userDao = new UsersDaoImpl();
  }

  public static UserService getInstance() {
    if (instance == null) {
      instance = new UserService();
    }
    return instance;
  }

  public Users getUserByEmail(String email) {
    return userDao.getUserByEmail(email);
  }


  /**
   * show profile
   *
   * @param
   */
  public List<Users> showInfoUser() {
    return userDao.showInfoUser();
  }

  public Users getUserById(int id) {
    return userDao.getUserById(id);
  }

  /**
   * update profile no ima
   *
   * @param
   */
  public void updateProfile(int userId, String newUserName, String newEmail, String newAddress,
                            String newPhoneNumber, Date newDateOfBirth, String newSexual, int newStatus, int newRole) {
    userDao.updateProfile(userId, newUserName, newEmail, newAddress, newPhoneNumber, newDateOfBirth,
            newSexual, newStatus, newRole);
  }

//  public String generateKeys(String userId) throws Exception {
//    // Tạo cặp khóa
//    KeyPair keyPair = KeyPairGen.generateKeyPair();
//
//    // Lấy publicKey
//    String publicKey = KeyPairGen.getPublicKey(keyPair);
//
//    // Lấy privateKey
//    String privateKey = KeyPairGen.getPrivateKey(keyPair);
//
//    // Lưu publicKey vào DB
//    userDao.updatePublicKey(userId, publicKey);
//
//    // Gửi email với privateKey (cần có cấu hình gửi email)
//    sendEmailWithPrivateKey(userId, privateKey);
//
//    return publicKey; // Trả về publicKey để hiển thị trên JSP
//  }
//
//  private void sendEmailWithPrivateKey(String userId, String privateKey) {
//    // Thực hiện gửi email cho user, sử dụng thư viện gửi email như JavaMail
//    String email = getUserEmailById(userId); // Hàm này để lấy email từ cơ sở dữ liệu
//    String subject = "Your Private Key";
//    String message = "Here is your private key: " + privateKey;
//
//    // Gửi email (code gửi email sẽ cần cấu hình SMTP)
//  }

  /**
   * update profile with img
   *
   * @param
   */
  public String updateProfileWithImage(int userId, String newUserName, String newEmail,
                                       String newAddress, String newPhoneNumber, Date newDateOfBirth, String img, String newSexual) {
    return userDao.updateProfileWithImage(userId, newUserName, newEmail, newAddress, newPhoneNumber,
            newDateOfBirth, img, newSexual);
  }

  public boolean checkPassUser(int id, String password) {
    return userDao.checkPassUser(id, password);
  }

  /**
   * update new password
   *
   * @param
   * @return password
   */
  public String changePass(int id, String newPassword) {
    String encodePass = MyUtils.encodePass(newPassword); // Mã hóa mật khẩu mới ở đây
    return userDao.updatePassWordUser(id, encodePass);
  }

  public List<Users> get5UsersForEachPage(int index, int quantityDefault) {
    return userDao.get5UsersForEachPage(index, quantityDefault);
  }

  public int countResultSearchingUser(String txtSearch) {
    return userDao.countResultSearchingUser(txtSearch);
  }

  public List<Users> search(String search, int index, int sizePage) {
    return userDao.search(search, index, sizePage);
  }

  public List<Users> searchFilter(String sortBy, String order, String search, int index,
                                  int sizePage) {
    return userDao.searchFilter(sortBy, order, search, index, sizePage);
  }

  public int countTotalUserInDatabase() {
    return userDao.countTotalRowUserInDatabase();
  }

  public List<Users> sortByFilter(int index, int role, String sortBy, String order) {
    return userDao.sortByFilter(index, role, sortBy, order);
  }

  public void deleteUser(int id) {
    userDao.deleteUser(id);
  }
  public int addNewGoogleUser(String username,String email, String img) {
    return userDao.addNewGoogleUser(username,email, img);
  }

  public String addNewUser(String username, String password, String hash, String email,
                           String phoneNumber, String address) {
    return userDao.addNewUser(username, password, hash, email,
            phoneNumber, address);
  }

  public String updateTimeStampUser(String email) {
    return userDao.updateTimeStampUser(email);
  }
}
