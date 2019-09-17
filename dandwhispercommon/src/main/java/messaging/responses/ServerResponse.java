package messaging.responses;

import java.io.Serializable;

public abstract class ServerResponse implements Serializable {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) { this.success = success; }
}
