package nhom55.hcmuaf.beans_remaster;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Carts {

  Integer id;
  Integer numOfKg;
  Double totalPrice;
  Integer quantity;
  Integer user;

}
