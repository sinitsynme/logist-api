package ru.sinitsynme.logistapi.service;

import dto.PageRequestDto;
import exception.ExceptionSeverity;
import exception.service.BadRequestException;
import exception.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.sinitsynme.logistapi.entity.Authority;
import ru.sinitsynme.logistapi.entity.User;
import ru.sinitsynme.logistapi.repository.AuthorityRepository;
import ru.sinitsynme.logistapi.rest.dto.authority.AuthorityRequestDto;
import ru.sinitsynme.logistapi.rest.dto.authority.AuthorityUpdateRequestDto;

import java.util.List;
import java.util.Locale;

import static ru.sinitsynme.logistapi.exception.ServiceExceptionCodes.*;
import static ru.sinitsynme.logistapi.exception.ServiceExceptionMessageTemplates.*;

@Service
public class AuthorityService {

    private static final String ROLE_PREFIX = "ROLE_";
    private final AuthorityRepository authorityRepository;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);

    @Autowired
    public AuthorityService(
            AuthorityRepository authorityRepository,
            @Lazy UserService userService) {
        this.authorityRepository = authorityRepository;
        this.userService = userService;
    }


    public List<Authority> getAll() {
        return authorityRepository.findAll(Sort.by("name"));
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

    public Authority save(AuthorityRequestDto authorityRequestDto) {
        String authorityName = authorityRequestDto.getName();

        if (!authorityName.startsWith(ROLE_PREFIX)) {
            throw new BadRequestException(
                    AUTHORITY_MUST_HAVE_ROLE_PREFIX_MESSAGE,
                    null,
                    HttpStatus.BAD_REQUEST,
                    AUTHORITY_MUST_HAVE_ROLE_PREFIX_CODE,
                    ExceptionSeverity.WARN
            );
        }

        if (authorityRepository.existsById(authorityName)) {
            throw new BadRequestException(
                    String.format(AUTHORITY_EXISTS_TEMPLATE, authorityName),
                    null,
                    HttpStatus.BAD_REQUEST,
                    AUTHORITY_EXISTS_CODE,
                    ExceptionSeverity.WARN
            );
        }

        Authority authority = new Authority();
        authority.setName(authorityName.toUpperCase(Locale.ENGLISH));
        authority.setDescription(authorityRequestDto.getDescription());

        authorityRepository.save(authority);

        log.info("Authority {} was created", authority);
        return authority;
    }

    public Authority update(String authorityName, AuthorityUpdateRequestDto requestDto) {
        Authority authority = getByName(authorityName);

        authority.setDescription(requestDto.getDescription());

        authorityRepository.save(authority);

        log.info("Authority {} was updated", authority);
        return authority;
    }

    public void delete(String authorityName) {
        Authority authority = getByName(authorityName);

        if (authority.isBasic()) {
            throw new BadRequestException(
                    String.format(AUTHORITY_IS_BASIC_TEMPLATE, authorityName),
                    null,
                    HttpStatus.BAD_REQUEST,
                    AUTHORITY_IS_BASIC_CODE,
                    ExceptionSeverity.WARN
            );
        }

        Pageable pageable = new PageRequestDto(0, 1, new String[]{}).toPageable();

        Page<User> usersWithThisAuthority = userService.getUsersByAuthority(authority, pageable);

        if(!usersWithThisAuthority.getContent().isEmpty()) {
            throw new BadRequestException(
                    String.format(
                            USERS_HAVE_AUTHORITY_THAT_IS_BEING_REMOVED_TEMPLATE,
                            authorityName),
                    null,
                    HttpStatus.BAD_REQUEST,
                    USERS_HAVE_AUTHORITY_THAT_IS_BEING_REMOVED_CODE,
                    ExceptionSeverity.WARN
            );
        }

        authorityRepository.deleteById(authorityName);

        log.info("Authority with name {} was deleted successfully", authorityName);
    }
}
