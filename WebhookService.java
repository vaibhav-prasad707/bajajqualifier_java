@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void generateWebhook() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> body = Map.of(
                "name", "Vaibhav Prasad",
                "regNo", "22BCE3301",
                "email", "vaibhav.prasad2022@vitstudent.ac.in"
        );

        System.out.println("🔹 Sending POST request to generate webhook...");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null) {
            System.out.println("❌ No response from generateWebhook API!");
            return;
        }

        String webhookUrl = (String) responseBody.get("webhook");
        String accessToken = (String) responseBody.get("accessToken");

        // Print them so you can see
        System.out.println("✅ Webhook URL received: " + webhookUrl);
        System.out.println("✅ Access Token received: " + accessToken);

        // Final SQL query (solution to your problem)
        String finalQuery = "SELECT p.AMOUNT AS SALARY, " +
                "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                "d.DEPARTMENT_NAME " +
                "FROM PAYMENTS p " +
                "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                "ORDER BY p.AMOUNT DESC LIMIT 1;";

        submitSolution(webhookUrl, accessToken, finalQuery);
    }

    private void submitSolution(String webhookUrl, String accessToken, String finalQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("finalQuery", finalQuery);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // Print before sending
        System.out.println("🔹 Posting SQL Query to: " + webhookUrl);
        System.out.println("📄 Final SQL Query: " + finalQuery);

        ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, entity, String.class);

        // Print server response
        System.out.println("✅ Response from webhook: " + response.getStatusCode() + " - " + response.getBody());
    }
}
