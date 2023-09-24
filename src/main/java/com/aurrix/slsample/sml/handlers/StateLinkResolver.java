package com.aurrix.slsample.sml.handlers;

import com.aurrix.slsample.sml.enums.ResponseFragmentType;
import com.aurrix.slsample.sml.exceptions.StateValidationException;
import com.aurrix.slsample.sml.models.State;
import com.aurrix.slsample.sml.models.StateRequest;
import com.aurrix.slsample.sml.models.StateResponse;
import com.aurrix.slsample.sml.services.StateContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;

@Service
public class StateLinkResolver implements WebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(StateLinkResolver.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private StateContextHolder stateContextHolder;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection established");
    }

    @Override
    public void handleMessage(
            WebSocketSession session,
            WebSocketMessage<?> message
    ) throws Exception {
        logger.info("Message received: " + message.getPayload());
        var request = mapper.readValue(message.getPayload().toString(), StateRequest.class);
        switch (request.type()) {
            case STATE_REQUEST -> this.onStateRequest(
                    session,
                    request
            );
            case STATE_UPDATED -> this.onStateUpdate(
                    session,
                    request
            );
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle transport error
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // Handle connection closed event
        stateContextHolder.getRequestStates(session).values().forEach(State::onDestroy);
        logger.info("Connection closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void onStateRequest(
            WebSocketSession session,
            StateRequest<List<String>> stateRequest
    ) throws Exception {
        var requestStateKeys = stateRequest.data();
        var requestStates = stateContextHolder.getRequestStates(session);
        for (var requestKey : requestStateKeys) {
            logger.info("State: " + requestKey);
            var state = requestStates.get(requestKey);
            try {
                state.onInit();
            } catch (StateValidationException e) {
                handleValidationException(
                        session,
                        requestKey,
                        e
                );
                return;
            }
            session.sendMessage(new TextMessage(
                    mapper.writeValueAsString(
                            new StateResponse<>(
                                    ResponseFragmentType.STATE_READY,
                                    requestKey,
                                    state
                            )
                    )
            ));
        }
    }

    private void onStateUpdate(
            WebSocketSession session,
            StateRequest<?> stateUpdate
    ) throws Exception {
        var storedState = stateContextHolder.getRequestStates(session).get(stateUpdate.key());
        State updatedState = (State) mapper.convertValue(stateUpdate.data(), storedState.source);
        logger.info("Stored state: " + storedState);
        logger.info("Updated state: " + stateUpdate.data());
        try {
            updatedState.onUpdate();
        } catch (StateValidationException e) {
            handleValidationException(
                    session,
                    stateUpdate.key(),
                    e
            );
            return;
        }
        updatedState.source = storedState.source;
        updatedState.isInitialised = storedState.isInitialised;
        stateContextHolder.setState(session, stateUpdate.key(), updatedState);
        session.sendMessage(new TextMessage(
                mapper.writeValueAsString(
                        new StateResponse<>(
                                ResponseFragmentType.STATE_READY,
                                stateUpdate.key(),
                                updatedState
                        )
                )
        ));
    }

    public void handleValidationException(
            WebSocketSession session,
            String key,
            StateValidationException exception
    ) throws IOException {
        session.sendMessage(
                new TextMessage(
                        mapper.writeValueAsString(
                                new StateResponse<>(
                                        ResponseFragmentType.STATE_ERROR,
                                        key,
                                        exception
                                )
                        )
                )
        );
    }
}
