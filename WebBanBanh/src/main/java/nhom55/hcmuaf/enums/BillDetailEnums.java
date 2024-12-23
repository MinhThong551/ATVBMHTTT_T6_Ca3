package nhom55.hcmuaf.enums;

public enum BillDetailEnums {
  IS_VERIFY("chưa xác thực"),
  NO_VERIFY("đã xác thực"),
  HAS_CHANGED("đã bị thay đổi");
  private final String text;

  BillDetailEnums(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
