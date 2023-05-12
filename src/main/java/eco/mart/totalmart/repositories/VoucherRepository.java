package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
}