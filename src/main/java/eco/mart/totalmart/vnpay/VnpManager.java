package eco.mart.totalmart.vnpay;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "VNPayManager", schema = "dbo")
public class VnpManager {

    @Id
    Long orderId;

    @Column(name = "linkToPay", nullable = false)
    String linkToPay;
    @Enumerated(EnumType.STRING)
    VnpStatus status;

    Timestamp expiredTime;

}
