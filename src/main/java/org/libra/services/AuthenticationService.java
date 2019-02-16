package org.libra.services;

import org.libra.beans.ResponseBean;
import org.libra.beans.utilities.AuthorityUtilityBean;
import org.libra.beans.utilities.ServiceUtilityBean;
import org.libra.entities.implementation.UserAuthorityEntity;
import org.libra.entities.implementation.UserEntity;
import org.libra.exceptions.BadCredentialsException;
import org.libra.exceptions.BadRequestException;
import org.libra.exceptions.DuplicatedEntryException;
import org.libra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService
{
    private final AuthorityUtilityBean authorityUtilityBean;
    private final ServiceUtilityBean serviceUtilityBean;
    private final UserRepository userRepository;
    private final ResponseBean responseBean;
    private final HttpHeaders httpHeaders;

    private List<UserAuthorityEntity> setRegistrationObjectAuthorityValues(String userAuthorityEmail)
    {
        List<UserAuthorityEntity> userAuthorityEntityList = new ArrayList<>();
        UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
        userAuthorityEntity.setEmail(userAuthorityEmail);
        userAuthorityEntity.setAuthority("ROLE_ADMIN");
        userAuthorityEntityList.add(userAuthorityEntity);
        return userAuthorityEntityList;
    }
    private UserEntity completeRegistrationObject(UserEntity userEntity)
    {
        userEntity.setEnabled(1);
        userEntity.setAuthorities(setRegistrationObjectAuthorityValues(userEntity.getEmail()));
        return userEntity;
    }

    public ResponseBean putAuthentication(UserEntity userEntity) throws Exception
    {
        Optional<UserEntity> optionalUserEntity = userRepository.findUserEntityByEmail(userEntity.getEmail());
        if (!optionalUserEntity.isPresent())
        {
            userRepository.save(serviceUtilityBean.encodeUserEntityPassword(completeRegistrationObject(userEntity)));
            responseBean.setStatus(HttpStatus.CREATED);
            responseBean.setHeaders(httpHeaders);
            responseBean.setResponse("User with email: '" + userEntity.getEmail() + "' successfully registered");
        }
        else
        {
            throw new DuplicatedEntryException("User with email: '" + userEntity.getEmail() + "' already exists");
        }
        return responseBean;
    }
    public ResponseEntity<?> getAuthentication(String status) throws Exception
    {
        ResponseEntity<?> responseEntity;
        if ((status != null) && (status.equals("success")))
        {
            responseEntity = new ResponseEntity<>(userRepository.findUserEntityByEmail(authorityUtilityBean.getCurrentAuthenticationEmail()), httpHeaders, HttpStatus.OK);
        }
        else if ((status != null) && (status.equals("invalidAuthenticationData")))
        {
            throw new BadCredentialsException("Invalid authentication data");
        }
        else
        {
            throw new BadRequestException("This is a authentication page, please use POST method to authenticate");
        }
        return responseEntity;
    }

    @Autowired
    public AuthenticationService(AuthorityUtilityBean authorityUtilityBean, ServiceUtilityBean serviceUtilityBean,
                                 UserRepository authenticationRepository, ResponseBean responseBean, HttpHeaders httpHeaders)
    {
        this.authorityUtilityBean = authorityUtilityBean;
        this.serviceUtilityBean = serviceUtilityBean;
        this.userRepository = authenticationRepository;
        this.responseBean = responseBean;
        this.httpHeaders = httpHeaders;
    }
}