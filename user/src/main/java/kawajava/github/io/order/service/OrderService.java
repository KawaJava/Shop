package kawajava.github.io.order.service;

import kawajava.github.io.order.model.Order;
import kawajava.github.io.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAllForUser(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }
}
