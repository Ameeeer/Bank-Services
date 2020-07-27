package bank.service.userservice.service;

import bank.service.userservice.model.Stonk;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * FeignClient - abstraction for calling REST, that native integrated with Ribbon and automatically convert into java objects
 *
 * @author Ruslan Potapov
 * @name - calling certain service if it registered in Eureka
 * @url - if external
 * @fallback - in case  if service or DB is unavailable
 */

@FeignClient(name = "stonks-service", url = "${feign.url:/**}", fallback = FeignFallback.class)
public interface ServiceFeignClient {


    // Request. contain way that identical in gallery-service (same signature)
    @RequestLine("GET /show")
    List<Stonk> getAllEmployeesList();

    class FeignHolder {
        public static ServiceFeignClient create() {
            return HystrixFeign.builder().encoder(new GsonEncoder()).decoder(new GsonDecoder()).target(ServiceFeignClient.class, "http://localhost:8081/", new FallbackFactory<ServiceFeignClient>() {
                @Override
                public ServiceFeignClient create(Throwable throwable) {
                    return new ServiceFeignClient() {
                        @Override
                        public List<Stonk> getAllEmployeesList() {
                            System.out.println(throwable.getMessage());
                            return null;
                        }
                    };
                }
            });
        }
    }

}
