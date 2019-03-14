package cn.anan.consumer.mq;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 消息接收者 1
 *
 * 这里一个项目模拟 四个接收者，
 * 消息接收者监听消息队列，发生变化时则执行对应的方法
 *
 * 业务场景：系统日志处理场景
 * 1. 微服务产生的日志，交给日志服务器处理
 * 2. 日志处理服务器有4个服务，分别是 DEBUG，INFO，WARN，ERROR (图中只模拟2个服务)
 * 3. 服务直接的通信采用 direct(发布订阅)
 *
 *                               ___________           #####################
 *                           _> |_log.info_|----msg--> ## info日志处理服务 ##
 *                          |                          #####################
 *                       __|________
 *                      (routing.key)
 *    _______    _________ |
 *   |_ 服务_|-->|_交换器_|<
 *                         \
 *                       ___\_________
 *                      (routing.key)
 *                          \     ____________            #####################
 *                           \_> |_log.error_|----msg-->  ## erro日志处理服务 ##
 *                                                        ####################
 *
 *
 *
 * @author anan
 * @created by anan on 2019/3/1 17:20
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.info.value}",autoDelete ="true"),
                exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.TOPIC),
//                key = "${mq.config.queue.info.routing-key}"
                key = "*.log.info"
        )
)
public class InfoReceiver {

  /**
   * 接收消息方法，采用消息队列监听机制
   * @param msg
   */
  @RabbitHandler
  public void process(String msg){
    System.out.println("receiver: info-->" + msg);
  }

}
