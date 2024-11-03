package org.uvhnael.ecomapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.uvhnael.ecomapi.Dto.AddressResponse;
import org.uvhnael.ecomapi.Model.Address;
import org.uvhnael.ecomapi.Repository.AddressRepository;
import org.uvhnael.ecomapi.exception.address.AddressNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> getByCustomerId(int customerId) {
        List<Address> addresses = addressRepository.getByCustomerId(customerId);
        if (addresses.isEmpty()) {
            throw new AddressNotFoundException("Address not found");
        }
        return addresses;
    }

    public Address getByCustomerIDAndIsDefault(int customerId) {
        Address address = addressRepository.getByCustomerIdAndIsDefault(customerId);
        if (address == null) {
            throw new AddressNotFoundException("Address not found");
        }
        return address;
    }

    public boolean createAddress(Address address) {
        checkNull(address);
        List<Address> addresses = addressRepository.getByCustomerId(address.getCustomerId());
        if (addresses.isEmpty()) {
            address.setDefault(true);
        }
        return addressRepository.createAddress(address.getCustomerId(), address.isDefault(), address.getCustomerName(), address.getAddressLine1(), address.getAddressLine2(), address.getPhoneNumber(), address.getCity(), address.getDistrict(), address.getWard());
    }

    public boolean updateAddress(Address address) {
        checkNull(address);
        if (addressRepository.getById(address.getId()) == null) {
            throw new AddressNotFoundException("Address ID: " + address.getId() + " not found");
        }
        return addressRepository.updateAddress(address.getId(), address.isDefault(), address.getCustomerName(), address.getAddressLine1(), address.getAddressLine2(), address.getPhoneNumber(), address.getCity(), address.getDistrict(), address.getWard());
    }

    public boolean deleteAddress(int id) {
        if (addressRepository.getById(id) != null) {
            throw new AddressNotFoundException("Address ID: " + id + " not found");
        }
        return addressRepository.deleteAddress(id);
    }

    public boolean setDefaultAddress(int id, int customerId) {
        Address address = addressRepository.getById(id);
        if (address == null) {
            throw new AddressNotFoundException("Address ID: " + id + " not found");
        }
        addressRepository.setDefaultAddress(id, customerId);
        return true;
    }

    public List<AddressResponse> getCity() {
        return addressRepository.getCity();
    }

    public List<AddressResponse> getDistrict(int city) {
        return addressRepository.getDistrict(city);
    }

    public List<AddressResponse> getWard(int district) {
        return addressRepository.getWard(district);
    }

    public void checkNull(Address address) {
        if (address.getCustomerName() == null) {
            throw new IllegalArgumentException("Customer name cannot be null");
        }
        if (address.getAddressLine1() == null) {
            throw new IllegalArgumentException("Address line 1 cannot be null");
        }
        if (address.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        if (address.getCity() == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        if (address.getDistrict() == null) {
            throw new IllegalArgumentException("District cannot be null");
        }
        if (address.getWard() == null) {
            throw new IllegalArgumentException("Ward cannot be null");
        }
    }
}
