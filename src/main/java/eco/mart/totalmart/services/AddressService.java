package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Address;
import eco.mart.totalmart.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    UserService userService;


    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    public Optional<Address> save(Address address) {
        address.setUser(userService.getUserLoggedIn());
        return Optional.of(addressRepository.save(address));
    }

    public Optional<Address> update(Address address) {
        return Optional.of(addressRepository.save(address));
    }

    public Optional<Address> delete(Address address) {
        addressRepository.delete(address);
        return Optional.of(address);
    }

}
