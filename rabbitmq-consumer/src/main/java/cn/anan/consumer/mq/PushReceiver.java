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

 *                                                      ___________                      #########
 *                                              7----->|_ 队列 Q1 _|---- 短信发送 -----> # 短信服务  #
 *                                             /                                         #########
 *                                            /
 *                        匹配规则 :Fanout    /
 *                                          /
 *     ____________            ########### /            ____________                     ###########
 *    |_ 订单服务 _|--------->  #  交换器  #------------>|_ 队列 Q2 _|---- app push ----> # push 服务 #
 *                             ############                                              ###########
 *
 *
 *
 * @author anan
 * @created by anan on 2019/3/1 17:20
 */
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.push.value}",autoDelete ="true"),
                exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.FANOUT)
//                key = "${mq.config.queue.info.routing-key}" //广播模式不需要路由键
        )
)
public class PushReceiver {

  /**
   * 接收消息方法，采用消息队列监听机制
   * @param msg
   */
  @RabbitHandler
  public void process(String msg){
    System.out.println("receiver: Push--->" + msg);
  }

}
