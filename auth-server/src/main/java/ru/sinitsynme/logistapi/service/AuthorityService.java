package ru.sinitsynme.logistapi.service;

import exception.ExceptionSeverity;
import exception.service.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Authority;
import ru.sinitsynme.logistapi.repository.AuthorityRepository;

import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.AUTHORITY_NOT_FOUND_CODE;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.AUTHORITY_NOT_FOUND_TEMPLATE;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority getByName(String name) {
        return authorityRepository.findById(name)
                .orElseThrow(() -> new NotFoundException(
                        String.format(AUTHORITY_NOT_FOUND_TEMPLATE, name),
                        null,
                        HttpStatus.NOT_FOUND,
                        AUTHORITY_NOT_FOUND_CODE,
                        ExceptionSeverity.WARN
                ));
    }

}
