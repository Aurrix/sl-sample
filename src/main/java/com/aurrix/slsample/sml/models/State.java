package com.aurrix.slsample.sml.models;

import com.aurrix.slsample.sml.enums.StateAction;
import com.aurrix.slsample.sml.exceptions.StateValidationException;

public abstract class State {
    public boolean isInitialised;

    public StateAction action;

    public Class<?> source;

    public void onInit() throws StateValidationException {}
    public void onUpdate() throws StateValidationException {}
    public void onDestroy() {}

    public abstract String getKey();
}
