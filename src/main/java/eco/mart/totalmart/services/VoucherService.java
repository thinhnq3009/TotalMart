package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Voucher;
import eco.mart.totalmart.repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    VoucherRepository voucherRepository;

    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> findById(Long id) {
        return voucherRepository.findById(id);
    }

    public Optional<Voucher> findByCode(String code) {
        return voucherRepository.findByCodeEquals(code);
    }


    public void save(Voucher voucher) {
        voucherRepository.save(voucher);
    }
}
