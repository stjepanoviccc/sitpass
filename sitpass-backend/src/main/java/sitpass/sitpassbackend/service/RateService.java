package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.RateDTO;
import sitpass.sitpassbackend.model.Rate;

public interface RateService {

    Rate getModel(Long id);
    RateDTO create(RateDTO rateDTO);

}
