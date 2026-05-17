package sistema_bancario.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sistema_bancario.dtos.req.CustomerReqDTO;
import sistema_bancario.dtos.res.CustomerDetailsResDTO;
import sistema_bancario.entities.users.Customer;
import sistema_bancario.repositories.CustomerRepository;
import sistema_bancario.services.exceptions.ResourceNotFoundException;

@Service
public class CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findCustomerByCpf(String cpf) {
        log.info(
                "event=customer_search_request cpf={}",
                cpf
        );

        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> {
                    log.warn(
                            "event=customer_search_failed reason=customer_not_found cpf={}",
                            cpf
                    );

                    return new ResourceNotFoundException("Cliente não encontrado.");
                });

        log.info(
                "event=customer_search_success customerId={} cpf={}",
                customer.getId(),
                cpf
        );

        return customer;
    }

    public CustomerDetailsResDTO createCustomer(CustomerReqDTO dto){
        log.info(
                "event=customer_creation_request cpf={} email={}",
                dto.cpf(),
                dto.email()
        );

        boolean customerAlreadyExists = customerRepository.findByCpf(dto.cpf())
                .isPresent();

        if (customerAlreadyExists) {
            log.warn(
                    "event=customer_creation_failed reason=cpf_already_registered cpf={}",
                    dto.cpf()
            );

            throw new IllegalArgumentException("Cliente já registrado.");
        }

        Customer newCustomer = new Customer();
        newCustomer.setName(dto.name());
        newCustomer.setPhone(dto.phone());
        newCustomer.setEmail(dto.email());
        newCustomer.setPassword(dto.password());
        newCustomer.setCpf(dto.cpf());
        newCustomer.setBirthDate(dto.birthDate());

        Customer savedCustomer = customerRepository.save(newCustomer);

        log.info(
                "event=customer_creation_success customerId={} cpf={} email={}",
                savedCustomer.getId(),
                savedCustomer.getCpf(),
                savedCustomer.getEmail()
        );

        return new CustomerDetailsResDTO(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getPhone(),
                savedCustomer.getEmail(),
                savedCustomer.getCpf(),
                savedCustomer.getBirthDate()
        );
    }
}
