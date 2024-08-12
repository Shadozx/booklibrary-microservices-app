package com.shadoww.authservice.mapper;

import com.shadoww.api.dto.request.user.PersonRequest;
import com.shadoww.api.dto.response.PersonResponse;
import com.shadoww.authservice.model.Person;

public interface PersonMapper {

    Person mapToModel(PersonRequest request);

    PersonResponse mapToResponse(Person person);
    Person mapToModel(PersonResponse response);

}
