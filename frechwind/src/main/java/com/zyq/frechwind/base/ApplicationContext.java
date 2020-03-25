package com.zyq.frechwind.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ApplicationContext {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
    private static ThreadLocal CONTEXT_THREAD_LOCAL = new ThreadLocal();
    private Operator operator;
    private AppSession appSession;
    private ClientType clientType;
    private ClientInfo clientInfo;

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        ApplicationContext object = (ApplicationContext) CONTEXT_THREAD_LOCAL.get();
        if (object == null) {
            //logger.info("create new ApplicationContext");
            object = new ApplicationContext();
            CONTEXT_THREAD_LOCAL.set(object);
        }
        return object;
    }

    public static void clearApplicationContext() {
        CONTEXT_THREAD_LOCAL.remove();
        //logger.info("clearApplicationContext");
    }



    public Operator getOperator() {
        if(appSession == null){
            return null;
        }
        Operator operator = (Operator) appSession.getAttribute("sessionMember");
        if (operator == null) {
            operator = (Operator) appSession.getAttribute("sessionUser");
        }
        return operator;
    }

    public String getUploadRoot() {
        File classRoot = new File(this.getClass().getClassLoader().getResource("").getPath());
        while (classRoot.getParentFile() != null) {
            classRoot = classRoot.getParentFile();
        }
        String root = classRoot.getAbsolutePath();
        if (root.endsWith("/") || root.endsWith("\\")) {
            root = root.substring(0, root.length() - 1);
        }
        root = root + "/" + "upload";
        return root;
    }

    public String getBackupRoot() {
        File classRoot = new File(this.getClass().getClassLoader().getResource("").getPath());
        while (classRoot.getParentFile() != null) {
            classRoot = classRoot.getParentFile();
        }
        return classRoot.getAbsolutePath() + "backupFiles";
    }

    public AppSession getAppSession() {
        return appSession;
    }

    public void setAppSession(AppSession appSession) {
        this.appSession = appSession;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }


    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}