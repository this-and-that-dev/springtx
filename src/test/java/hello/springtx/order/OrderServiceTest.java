package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void complete() throws NotEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUserName("정상");

        //when
        orderService.order(order);

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(findOrder.getPayStatus()).isEqualTo(order.getPayStatus());
    }
    @Test
    void runTimeException() {
        //given
        Order order = new Order();
        order.setUserName("예외");

        //when
        Assertions.assertThatThrownBy(() -> orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        //then
        Optional<Order> findOrder = orderRepository.findById(order.getId());
        Assertions.assertThat(findOrder.isEmpty()).isTrue();
    }

    @Test
    void bizException() {
        //given
        Order order = new Order();
        order.setUserName("잔고부족");

        //when
        try {
            orderService.order(order);
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고부족을 알린다.");
        }

        //then
        Optional<Order> findOrder = orderRepository.findById(order.getId());
        Assertions.assertThat(findOrder.get().getPayStatus()).isEqualTo("대기");
    }
}