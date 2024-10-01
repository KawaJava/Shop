package kawajava.github.io.user.controller.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kawajava.github.io.order.model.OrderStatus;
import lombok.Builder;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Builder
public class OrderDetailsDto {
    private Date placeDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private BigDecimal grossValue;
    private String zipcode;
    private String city;
    private String street;
}
