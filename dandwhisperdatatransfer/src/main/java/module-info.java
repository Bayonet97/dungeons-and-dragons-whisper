module dandwhisperdatatransfer {
   requires dandwhispercommon;
   requires gson;
   requires slf4j.api;
   requires java.ws.rs;
   requires java.sql;
   requires org.eclipse.jetty.servlet;
   requires org.eclipse.jetty.server;
   requires jersey.container.servlet.core;
   opens rest;
   exports rest;
}