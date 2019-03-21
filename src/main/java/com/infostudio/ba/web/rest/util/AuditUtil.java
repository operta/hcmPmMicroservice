package com.infostudio.ba.web.rest.util;


import com.infostudio.ba.domain.Action;
import com.infostudio.ba.security.SecurityUtils;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;

public final class AuditUtil {
    private AuditUtil() {

    }

    public static AuditApplicationEvent createAuditEvent(String principal, String type, String entityName, String entityId, Action action) {
        switch (action) {
            case POST:
                return new AuditApplicationEvent(
                    principal,
                    type,
                    "message=" + HeaderUtil.createEntityCreationAlert(entityName, entityId)
                );
            case PUT:
                return new AuditApplicationEvent(
                    principal,
                    type,
                    "message=" + HeaderUtil.createEntityUpdateAlert(entityName, entityId)
                );
            case DELETE:
                return new AuditApplicationEvent(
                    principal,
                    type,
                    "message=" + HeaderUtil.createEntityDeletionAlert(entityName, entityId)
                );
        }
        return null;
    }

    public static AuditApplicationEvent createAuditEvent(String entityName, String entityId, Action action) {
        switch (action) {
            case POST:
                return new AuditApplicationEvent(
                    SecurityUtils.getCurrentUserLogin().orElse("/"),
                    "performance",
                    "message=" + HeaderUtil.createEntityCreationAlert(entityName, entityId)
                );
            case PUT:
                return new AuditApplicationEvent(
                    SecurityUtils.getCurrentUserLogin().orElse("/"),
                    "performance",
                    "message=" + HeaderUtil.createEntityUpdateAlert(entityName, entityId)
                );
            case DELETE:
                return new AuditApplicationEvent(
                    SecurityUtils.getCurrentUserLogin().orElse("/"),
                    "performance",
                    "message=" + HeaderUtil.createEntityDeletionAlert(entityName, entityId)
                );
        }
        return null;
    }
}
