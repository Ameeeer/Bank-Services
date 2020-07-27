package bank.service.userservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * The type Rest template service.
 *
 * @author Ruslan Potapov
 */
@Service
@RequiredArgsConstructor
public class RestTemplateService {
    @Value("${stonks-service.data.url:/**}")
    private final String url;
    private final RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "failed")
    public String data() {
        String response = restTemplate.getForObject(url, String.class);
        //Log.log(Log.Level.INFO, response);
        return response;
    }

    public String failed() {
        String error = "Service is not available now. Please try later";
       // Log.log(Log.Level.INFO, error);
        return error;
    }

}
