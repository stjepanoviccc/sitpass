package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.ManagesDTO;
import sitpass.sitpassbackend.dto.ManagesRequestDTO;
import sitpass.sitpassbackend.model.Manages;

import java.util.List;

public interface ManagesService {

    Manages getModel(Long id);
    Boolean isManager(String email, Long facilityId, String by);
    List<ManagesDTO> findAll();
    List<ManagesDTO> findAllByUser(String email);
    ManagesDTO create(ManagesRequestDTO managesRequestDTO);
    void delete(Long id);
}
