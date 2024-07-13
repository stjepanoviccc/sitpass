package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.WorkDayDTO;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.WorkDay;
import sitpass.sitpassbackend.repository.WorkDayRepository;
import sitpass.sitpassbackend.service.WorkDayService;

import static sitpass.sitpassbackend.dto.WorkDayDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class WorkDayServiceImpl implements WorkDayService {

    private final WorkDayRepository workDayRepository;

    @Override
    public WorkDay getModel(Long id) {
        return workDayRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Work Day with id %s not found.", id);
                    return new NotFoundException(errorMessage);
                });
    }

    @Override
    public WorkDayDTO create(WorkDayDTO workDayDTO) {
        WorkDay createdWorkDay = workDayRepository.save(workDayDTO.convertToModel());
        return convertToDto(createdWorkDay);
    }

    @Override
    public WorkDayDTO update(Long id, WorkDayDTO workDayDTO) {
        WorkDay workDay = getModel(id);
        workDay.setDay(workDayDTO.getDay());
        workDay.setFrom(workDayDTO.getFrom());
        workDay.setFacility(workDayDTO.getFacility());
        workDay.setUntil(workDayDTO.getUntil());
        workDay.setValidFrom(workDayDTO.getValidFrom());
        workDay.setIsDeleted(workDayDTO.getIsDeleted());
        WorkDay updatedWorkDay = workDayRepository.save(workDay);
        return convertToDto(updatedWorkDay);
    }

    @Override
    public void delete(Long id) {
        WorkDay deletedWorkDay = getModel(id);
        workDayRepository.deleteById(id);
    }

}
