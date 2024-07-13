package sitpass.sitpassbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.UserAndImageDTO;
import sitpass.sitpassbackend.dto.UserDTO;
import sitpass.sitpassbackend.service.UserService;

import static sitpass.sitpassbackend.dto.UserDTO.convertToDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PutMapping()
    public ResponseEntity<UserDTO> update(@RequestBody UserAndImageDTO userAndImageDTO) {
        return ResponseEntity.ok(userService.update(userAndImageDTO.getUserDTO(), userAndImageDTO.getImageUrl()));
    }

    @PatchMapping("/{id}/{password}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @PathVariable String password) {
        return ResponseEntity.ok(userService.update(id, password));
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(convertToDTO(userService.getModel(email)));
    }

    @GetMapping("/{id}/{typedCurrentPassword}")
    public ResponseEntity<Boolean> validateCurrentPassword(@PathVariable Long id, @PathVariable String typedCurrentPassword) {
        return ResponseEntity.ok(userService.validateCurrentPassword(id, typedCurrentPassword));
    }

}
