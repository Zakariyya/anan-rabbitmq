package cn.anan.provider.mq;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author anan
 * @created by anan on 2019/3/4 16:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SenderTest {

  @Autowired
  private UserSender userSender;
  @Autowired
  private OrderSender orderSender;
  @Autowired
  private ProductSender productSender;

  @Test
  public void send() {
    this.userSender.send("userSender..........");
    this.orderSender.send("orderSender..........");
    this.productSender.send("productSender..........");
  }
}