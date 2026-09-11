package br.com.estudos.exchangeservicemicrosservice.enviromnent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class InstanceInformationService implements ApplicationListener<WebServerInitializedEvent> {
    private int port;

    @Value("${HOSTNAME:LOCAL}")
    private String hostName;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName.substring(hostName.length() - 5);
    }
}
