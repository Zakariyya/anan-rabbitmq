package cn.anan.provider.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: anan
 * @create: 2019/3/14 16:33
 * @Description:
 */
@Component
public class ProductSender {
//  @Value("${mq.config.queue.error.routing-key}")
//  private String errorRoutingKey;

  @Value("${mq.config.exchange}")
  private String exchange;

  @Autowired
  private AmqpTemplate rabbitAmqpTemplate;

  public void send(String msg) {
    /**
     * 向消息队列发送消息
     * 参数1：交换器名称
     * 参数2：路由键
     * 参数3：消息
     *
     */
    this.rabbitAmqpTemplate.convertAndSend(exchange, "product.log.info", "product.log.info:::::::"+msg);
    this.rabbitAmqpTemplate.convertAndSend(exchange, "product.log.debug", "product.log.debug:::::::"+msg);
    this.rabbitAmqpTemplate.convertAndSend(exchange, "product.log.error", "product.log.error:::::::"+msg);
    this.rabbitAmqpTemplate.convertAndSend(exchange, "product.log.warn", "product.log.warn:::::::"+msg);
  }

}
