package br.com.vandersonsampaio.mapper;

import br.com.vandersonsampaio.model.Session;
import br.com.vandersonsampaio.model.dto.response.RegisteredSessionDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SessionMapper {

    RegisteredSessionDTO toDTO(Session model);
}
