package org.libra.controllers;

import org.libra.assets.validators.RequestDataValidator;
import org.libra.assets.wrappers.ResponseEntityWrapper;
import org.libra.dto.UserAuthorityDto;
import org.libra.entities.implementation.UserAuthorityEntity;
import org.libra.services.UserAuthorityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/userAuthority")
public class UserAuthorityController
{
    private final UserAuthorityService userAuthorityService;
    private final ModelMapper modelMapper;

    @PutMapping
    public ResponseEntity<?> putUserAuthority(@Validated(value = {RequestDataValidator.PutUserAuthority.class}) @RequestBody UserAuthorityDto userAuthorityDtoToPut) throws Exception
    {
        return new ResponseEntityWrapper<>(userAuthorityService.putUserAuthority(modelMapper.map(userAuthorityDtoToPut, UserAuthorityEntity.class)));
    }

    @DeleteMapping(value = {"", "/{id}"})
    public ResponseEntity<?> deleteUserAuthority(@PathVariable(value = "id") Integer id) throws Exception
    {
        return new ResponseEntityWrapper<>(userAuthorityService.deleteUserAuthority(id));
    }

    @Autowired
    public UserAuthorityController(UserAuthorityService userAuthorityService, ModelMapper modelMapper)
    {
        this.userAuthorityService = userAuthorityService;
        this.modelMapper = modelMapper;
    }
}