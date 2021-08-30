package br.com.vandersonsampaio.client;

import br.com.vandersonsampaio.model.dto.UserInfoDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "UserInfo", url ="${vote_in_session.user_info.url}")
public interface UserInfoClient {

    @GetMapping(value = "/users/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserInfoDTO> getStatusCPF(@PathVariable("cpf") String cpf);
}
