package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}