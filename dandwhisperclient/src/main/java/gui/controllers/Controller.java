package gui.controllers;

import clienthandler.DanDWhisperClientHandler;
import messaging.responses.OpenChatSessionResponse;

import java.beans.PropertyChangeSupport;

public abstract class Controller {
    DanDWhisperClientHandler clientHandler;
    SceneSwitcher sceneSwitcher = new SceneSwitcher();
}
