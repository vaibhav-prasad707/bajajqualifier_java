@SpringBootApplication
public class BajajQualifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(BajajQualifierApplication.class, args);
    }

    @Bean
    CommandLineRunner run(WebhookService webhookService) {
        return args -> webhookService.generateWebhook();
    }
}
