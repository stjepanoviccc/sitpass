package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.DisciplineDTO;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Discipline;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.repository.DisciplineRepository;
import sitpass.sitpassbackend.service.DisciplineService;

import static sitpass.sitpassbackend.dto.DisciplineDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineRepository disciplineRepository;

    @Override
    public Discipline getModel(Long id) {
        return disciplineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Discipline with id %s not found.", id)));
    }

    @Override
    public DisciplineDTO create(DisciplineDTO disciplineDTO) {
        Discipline savedDiscipline = disciplineRepository.save(disciplineDTO.convertToModel());
        return convertToDto(savedDiscipline);
    }

    @Override
    public DisciplineDTO update(Facility facility, DisciplineDTO disciplineDTO) {
        Discipline discipline = getModel(disciplineDTO.getId());
        discipline.setName(disciplineDTO.getName());
        discipline.setFacility(facility);
        discipline.setIsDeleted(discipline.getIsDeleted());
        Discipline updatedDiscipline = disciplineRepository.save(discipline);
        return convertToDto(updatedDiscipline);
    }

    @Override
    public void delete(Long id) {
        Discipline deletedDiscipline = getModel(id);
        disciplineRepository.deleteById(id);
    }
}
