package com.aurrix.slsample.sml.services;

import com.aurrix.slsample.sml.models.State;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StateContextHolder {

    @Autowired
    private List<State> states;


    public void setState(WebSocketSession session, String key, State state) throws Exception {
        var states = getRequestStates(session);
        states.put(key, state);
    }

    public Map<String, State> getRequestStates(WebSocketSession session) throws Exception {
        // With Session Affinity
        Map<String, State> sessionStates = (Map<String, State>) session.getAttributes().get("states");
        if (sessionStates != null) {
            return sessionStates;
        } else {
            var initStates = this.getRawStates();
            session.getAttributes().put("states", initStates);
            return initStates;
        }
    }

    private Map<String, State> getRawStates() throws Exception {
        Map<String, State> requestStates = new HashMap<>();
        for (var state : this.states) {
            var source = (State) ((Advised) state).getTargetSource().getTarget();
            source.source = source.getClass();
            requestStates.put(state.getKey(), source);
        }
        return requestStates;
    }
}
