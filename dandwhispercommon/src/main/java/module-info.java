module dandwhispercommon {
    opens domain;
    exports domain;
    opens messaging;
    exports messaging;
    opens messaging.requests;
    exports messaging.requests;
    opens messaging.communication;
    exports messaging.communication;
    opens messaging.responses;
    exports messaging.responses;
}