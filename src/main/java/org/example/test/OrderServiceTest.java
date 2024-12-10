package org.example.test;

import org.example.dto.OrderDTO;
import org.example.dto.OrderItemDTO;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import org.example.entity.Product;
import org.example.mapper.OrderMapper;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)


public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @Test
    public void testGetAllOrders() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    public void testCreateOrder() {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(2);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setOrderDate(LocalDateTime.now());
        orderDTO.setStatus("CREATED");
        orderDTO.setItems(Collections.singletonList(itemDTO));

        Product product = new Product();
        product.setId(1L);

        OrderItem orderItem = new OrderItem();
        Order order = new Order();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderMapper.toEntity(any(OrderItemDTO.class), eq(product), isNull())).thenReturn(orderItem);
        when(orderMapper.toEntity(eq(orderDTO), anyList())).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        Long customerId = 1L;
        List<OrderItemDTO> orderItemDTOs = Collections.singletonList(itemDTO);
        OrderDTO result = orderService.createOrder(customerId, orderItemDTOs);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).findById(1L);
        verify(orderMapper, times(1)).toEntity(any(OrderItemDTO.class), eq(product), isNull());
        verify(orderMapper, times(1)).toEntity(eq(orderDTO), anyList());
        verify(orderRepository, times(1)).save(order);
    }


    @Test
    public void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetOrdersByStatus() {
        Order order = new Order();

        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(Collections.singletonList(order));

        List<Order> result = orderService.getOrdersByStatus(OrderStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
    }

    @Test
    public void testUpdateOrderStatus() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.updateOrderStatus(1L, OrderStatus.SHIPPED);

        assertNotNull(result);
        assertEquals(OrderStatus.SHIPPED, result.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testAddOrderItem() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        order.addOrderItem(item);
        assertEquals(1, order.getOrderItems().size());
    }

    @Test
    public void testRemoveOrderItem() {

        Order order = new Order();
        OrderItem item = new OrderItem();

        order.addOrderItem(item);

        assertEquals(1, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(item));
        assertEquals(order, item.getOrder());

        order.removeOrderItem(item);

        assertEquals(0, order.getOrderItems().size());
        assertFalse(order.getOrderItems().contains(item));
        assertNull(item.getOrder());
    }

    @Test
    public void testAddProduct() {

        Order order = new Order();

        assertEquals(0, order.getOrderProducts().size());

        Product dummyProduct = new Product();
        order.addProduct(dummyProduct);

        assertEquals(1, order.getOrderProducts().size());
    }

    @Test
    void getAllOrders() {
    }

    @Test
    void getOrderById() {
    }

    @Test
    void createOrder() {
    }

    @Test
    void deleteOrder() {
    }

    @Test
    void getOrdersByStatus() {
    }

    @Test
    void updateOrderStatus() {
    }

    @Test
    void updateOrderStatuses() {
    }

    @Test
    void getDailyProfit() {
    }

    @Test
    void getProfit() {
    }

    @Test
    void getOrdersPendingForMoreThanNDays() {
    }

    @Test
    void addProductToCart() {
    }

    @Test
    void removeProductFromCart() {
    }

    @Test
    void addProduct() {
    }

    @Test
    void getProductsPendingForMoreThanNDays() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void getId() {
    }

    @Test
    void getCreatedDate() {
    }

    @Test
    void getCreatedAt() {
    }

    @Test
    void getCustomerId() {
    }

    @Test
    void getOrderDate() {
    }

    @Test
    void getShippingAddress() {
    }

    @Test
    void getBillingAddress() {
    }

    @Test
    void getPaymentStatus() {
    }

    @Test
    void getStatus() {
    }

    @Test
    void getOrderItems() {
    }

    @Test
    void getOrderProducts() {
    }

    @Test
    void getTotalPrice() {
    }

    @Test
    void setId() {
    }

    @Test
    void setCreatedDate() {
    }

    @Test
    void setCreatedAt() {
    }

    @Test
    void setCustomerId() {
    }

    @Test
    void setOrderDate() {
    }

    @Test
    void setShippingAddress() {
    }

    @Test
    void setBillingAddress() {
    }

    @Test
    void setPaymentStatus() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void setOrderItems() {
    }

    @Test
    void setOrderProducts() {
    }

    @Test
    void setTotalPrice() {
    }

    @Test
    void addOrderItem() {
    }

    @Test
    void removeOrderItem() {
    }

    @Test
    void calculateDailyProfit() {
    }

    @Test
    void testEquals1() {
    }

    @Test
    void testHashCode1() {
    }

    @Test
    void testGetAllOrders1() {
    }

    @Test
    void testGetDailyProfit1() {
    }

    @Test
    void testGetDailyProfit2() {
    }

    @Test
    void testGetProfit1() {
    }

    @Test
    void testGetAllOrders2() {
    }

    @Test
    void testGetOrderById1() {
    }

    @Test
    void testCreateOrder1() {
    }

    @Test
    void testDeleteOrder1() {
    }

    @Test
    void testGetOrdersByStatus1() {
    }

    @Test
    void testUpdateOrderStatus1() {
    }

    @Test
    void testGetProfit2() {
    }

    @Test
    void testAddProductToCart() {
    }

    @Test
    void testGetDailyProfit() {
    }

    @Test
    void testCreateOrder2() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void getOrderStatus() {
    }

    @Test
    void getOrderHistory() {
    }

    @Test
    void testGetDailyProfit3() {
    }
}

