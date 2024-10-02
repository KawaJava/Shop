package kawajava.github.io.user.controller;

import jakarta.validation.Valid;
import kawajava.github.io.kafka.KafkaProducer;
import kawajava.github.io.order.model.Order;
import kawajava.github.io.order.service.OrderService;
import kawajava.github.io.user.controller.dto.OrderDetailsDto;
import kawajava.github.io.user.controller.dto.UserDetailsDto;
import kawajava.github.io.user.controller.dto.UserDto;
import kawajava.github.io.user.model.User;
import kawajava.github.io.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @PostMapping("/registry")
    public User createUser(@RequestBody @Valid UserDto userDto) {
        var newUser = userService.registerUser(userDto);

        var message = "Nowy użytkownik zarejestrowany: " + newUser.getUsername() +
                " (" + newUser.getEmail() + ")";

        kafkaProducer.sendMessage("user-registration-topic", message);
        return newUser;
    }

    @PutMapping("/users/edit")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UserDto userDto) {
        String response = userService.updateUser(userDetails, userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/me/details")
    public UserDetailsDto getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByUsername(userDetails);
        List<Order> allForUser = orderService.findAllForUser(user.getId());
        return UserDetailsDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .orders(allForUser.stream()
                        .map(order -> OrderDetailsDto.builder()
                                .placeDate(order.getPlaceDate())
                                .orderStatus(order.getOrderStatus())
                                .grossValue(order.getGrossValue())
                                .zipcode(order.getZipcode())
                                .city(order.getCity())
                                .street(order.getStreet())
                                .build())
                        .toList())
                .build();
    }

    @PostMapping("/activate/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) {
        var user = userService.findByActivationToken(token);

        if (user.getActivationTokenDate().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token wygasł");
        }
        userService.activateUser(user.getId());
        return ResponseEntity.ok("Konto zostało aktywowane!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails);
        return ResponseEntity.ok("Konto zostało trwale usunięte.");
    }
}
