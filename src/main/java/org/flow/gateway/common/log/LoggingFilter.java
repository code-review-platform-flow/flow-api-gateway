package org.flow.gateway.common.log;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	public LoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			logRequest(exchange);
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				logResponse(exchange);
			}));
		};
	}

	private void logRequest(ServerWebExchange exchange) {
		Map<String, Object> logInfo = new HashMap<>();
		logInfo.put("method", exchange.getRequest().getMethod());
		logInfo.put("uri", exchange.getRequest().getURI());
		logInfo.put("headers", exchange.getRequest().getHeaders());

		Map<String, Object> logMap = new HashMap<>();
		logMap.put("request", logInfo);

		logger.info("{}", logMap);
	}

	private void logResponse(ServerWebExchange exchange) {
		Map<String, Object> logInfo = new HashMap<>();
		logInfo.put("statusCode", exchange.getResponse().getStatusCode());

		Map<String, Object> logMap = new HashMap<>();
		logMap.put("response", logInfo);

		logger.info("{}", logMap);
	}

	public static class Config {
		// Configuration properties if needed
	}
}
