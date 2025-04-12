package com.backend.Backend.comps;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ConfirmationListener implements MessageListener {

    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> waiting_tokens = new ConcurrentHashMap<>();

    public boolean waitForConfirmation(String token, long timeout_ms) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        waiting_tokens.put(token, future);

        try {
            return future.get(timeout_ms, TimeUnit.MILLISECONDS);
        }
        catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        catch (TimeoutException e) {
            return false;
        }
        finally {
            waiting_tokens.remove(token);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String token = message.toString();
        if (waiting_tokens.containsKey(token)) {
            waiting_tokens.get(token).complete(true);
        }
    }
}
