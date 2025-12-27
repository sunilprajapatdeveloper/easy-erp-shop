package nextpos.app.nextpos.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayClientConfig {

    @Bean
    public RazorpayClient razorpayClient(RazorpayConfig razorpayConfig) throws RazorpayException {
        return new RazorpayClient(razorpayConfig.getKey(), razorpayConfig.getSecret());
    }
}