package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.WorkDayDTO;
import sitpass.sitpassbackend.model.WorkDay;

public interface WorkDayService {

    WorkDay getModel(Long id);
    WorkDayDTO create(WorkDayDTO workDayDTO);
    WorkDayDTO update(Long id, WorkDayDTO workDayDTO);
    void delete(Long id);

}
