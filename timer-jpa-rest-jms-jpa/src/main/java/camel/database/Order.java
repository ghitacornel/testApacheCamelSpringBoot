package camel.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_ORDER")
public class Order {

    @Id
    @GeneratedValue
    private Integer id;

    private String product;
    private int price;
    private int quantity;
    private int percentageVoucherReduction;
    private Date paymentDate;

    private int voucherTryCount = 0;

    private int paymentTryCount = 0;

    @Builder.Default
    private OrderStatus status = OrderStatus.NEW;

}
