package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.RateDTO;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Rate;
import sitpass.sitpassbackend.repository.RateRepository;
import sitpass.sitpassbackend.service.RateService;

import static sitpass.sitpassbackend.dto.RateDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class RateServiceImpl implements RateService {

    private final RateRepository rateRepository;

    @Override
    public Rate getModel(Long id) {
        return rateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Rate with id %s not found.", id)));
    }

    @Override
    public RateDTO create(RateDTO rateDTO) {
        if (isValidRating(rateDTO.getEquipment()) || isValidRating(rateDTO.getHygene()) || isValidRating(rateDTO.getSpace()) || isValidRating(rateDTO.getStaff())) {
            throw new IllegalArgumentException("Ratings must be between 1 and 10.");
        }
        return convertToDto(rateRepository.save(rateDTO.convertToModel()));
    }

    private boolean isValidRating(int rating) {
        return rating < 1 || rating > 10;
    }
}
