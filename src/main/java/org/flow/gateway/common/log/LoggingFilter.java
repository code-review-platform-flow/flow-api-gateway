package org.flow.gateway.common.log;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Order(0)
public class LoggingFilter implements GlobalFilter {

	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		return DataBufferUtils.join(request.getBody())
			.flatMap(dataBuffer -> {
				byte[] bytes = new byte[dataBuffer.readableByteCount()];
				dataBuffer.read(bytes);
				String body = new String(bytes, StandardCharsets.UTF_8);
				DataBufferUtils.release(dataBuffer);

				logRequest(request, body);

				ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
					@Override
					public Flux<DataBuffer> getBody() {
						return Flux.just(exchange.getResponse().bufferFactory().wrap(bytes));
					}
				};

				ServerHttpResponse originalResponse = exchange.getResponse();
				ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
					@Override
					public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
						if (body instanceof Flux) {
							Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>)body;
							return super.writeWith(fluxBody.map(dataBuffer -> {
								byte[] content = new byte[dataBuffer.readableByteCount()];
								dataBuffer.read(content);
								logResponse(originalResponse, new String(content, StandardCharsets.UTF_8));
								return originalResponse.bufferFactory().wrap(content);
							}));
						}
						return super.writeWith(body);
					}

					@Override
					public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
						return writeWith(Flux.from(body).flatMapSequential(p -> p));
					}
				};

				return chain.filter(exchange.mutate().request(mutatedRequest).response(decoratedResponse).build());
			});
	}

	private void logRequest(ServerHttpRequest request, String body) {
		Map<String, Object> logMap = new HashMap<>();
		logMap.put("path", request.getURI().getPath());
		logMap.put("method", request.getMethod() != null ? request.getMethod().name() : "UNKNOWN");
		logMap.put("headers", request.getHeaders());
		logMap.put("body", body);

		try {
			String logOutput = objectMapper.writeValueAsString(logMap);
			logger.info("Request: {}", logOutput);
		} catch (JsonProcessingException e) {
			logger.error("Error logging request", e);
		}
	}

	private void logResponse(ServerHttpResponse response, String body) {
		Map<String, Object> logMap = new HashMap<>();
		logMap.put("statusCode", response.getStatusCode());
		logMap.put("headers", response.getHeaders());
		logMap.put("body", body);

		try {
			String logOutput = objectMapper.writeValueAsString(logMap);
			logger.info("Response: {}", logOutput);
		} catch (JsonProcessingException e) {
			logger.error("Error logging response", e);
		}
	}
}