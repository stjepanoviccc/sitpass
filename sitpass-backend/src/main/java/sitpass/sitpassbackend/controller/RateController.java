package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sitpass.sitpassbackend.dto.RateDTO;
import sitpass.sitpassbackend.service.RateService;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/facilities/reviews/rates")
public class RateController {

    private final RateService rateService;

    @PostMapping()
    public ResponseEntity<RateDTO> create(@Valid @RequestBody RateDTO rateDTO) {
        return ResponseEntity.status(CREATED).body(rateService.create(rateDTO));
    }

}
