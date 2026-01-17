package hu.mbalazs.szamlazz.xmlhandling;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xml.output")
public class XmlOutputProperties {
    private String agentId;

    public String getAgentId() { return this.agentId;}
    public void setAgentId(String agentId) { this.agentId = agentId; }


}
