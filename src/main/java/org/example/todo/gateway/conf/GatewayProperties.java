package org.example.todo.gateway.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@ConfigurationProperties(prefix = "todo.gateway")
@Component
@Data
public class GatewayProperties {


    /**
     * 免鉴权Urls
     */
    List<String> loginAllowPaths;


}
