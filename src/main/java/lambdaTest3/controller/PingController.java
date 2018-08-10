package lambdaTest3.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;

import lambdaTest3.StreamLambdaHandler;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@RestController
@EnableWebMvc
public class PingController {
	private static Logger log = LoggerFactory.getLogger("lambdaTest3");
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public Map<String, String> ping() {
        Map<String, String> pong = new HashMap<>();
        
        System.out.println("CONTROLLER - This is loging from System.out");
        log.info("CONTROLLER - This is logging from slf4j");
        log.warn("CONTROLLER - This is logging from slf4j");
        log.error("CONTROLLER - This is logging from slf4j");
        
        pong.put("pong", this.getSecret());
        return pong;
    }
    
    private String getSecret() {

        String secretName = "test";
        String endpoint = "secretsmanager.us-east-1.amazonaws.com";
        String region = "us-east-1";
        String secret = null;

        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        AWSSecretsManager client = clientBuilder.build();

        
        ByteBuffer binarySecretData;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;
        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        } catch(ResourceNotFoundException e) {
            System.out.println("The requested secret " + secretName + " was not found");
        } catch (InvalidRequestException e) {
            System.out.println("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("The request had invalid params: " + e.getMessage());
        }

        if(getSecretValueResult == null) {
            return "";
        }

        // Decrypted secret using the associated KMS CMK
        // Depending on whether the secret was a string or binary, one of these fields will be populated
        if(getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
        }
        else {
            binarySecretData = getSecretValueResult.getSecretBinary();
        }
        return secret;
    }
    
}
