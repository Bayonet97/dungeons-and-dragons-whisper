package websocketsclient;

import java.beans.PropertyChangeSupport;
import java.util.Observable;
/**
 * messaging.ChatMessageBuilder to be sent from client to client through a Communicator.
 * All clients that are subscribed to the property of a message
 * will receive a copy of this message.
 * @author Nico Kuijpers
 */
public abstract class Communicator{
    final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
}
