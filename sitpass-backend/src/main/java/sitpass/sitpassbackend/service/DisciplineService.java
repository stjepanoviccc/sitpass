package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.DisciplineDTO;
import sitpass.sitpassbackend.model.Discipline;
import sitpass.sitpassbackend.model.Facility;

public interface DisciplineService {

    Discipline getModel(Long id);
    DisciplineDTO create(DisciplineDTO disciplineDTO);
    DisciplineDTO update(Facility facility,DisciplineDTO disciplineDTO);
    void delete(Long id);

}
