package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query("select v from Voucher v where v.code = ?1")
    Optional<Voucher> findByCodeEquals(String code);
}