package org.openremote.manager.dashboard;

import org.openremote.container.message.MessageBrokerService;
import org.openremote.container.timer.TimerService;
import org.openremote.manager.security.ManagerIdentityService;
import org.openremote.manager.web.ManagerWebResource;
import org.openremote.model.dashboard.Dashboard;
import org.openremote.model.dashboard.DashboardResource;
import org.openremote.model.http.RequestParams;
import org.openremote.model.util.ValueUtil;

import javax.ws.rs.WebApplicationException;

import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

public class DashboardResourceImpl extends ManagerWebResource implements DashboardResource {

    protected final DashboardStorageService dashboardStorageService;
    protected final MessageBrokerService messageBrokerService;

    public DashboardResourceImpl(TimerService timerService, ManagerIdentityService identityService, DashboardStorageService dashboardStorageService, MessageBrokerService messageBrokerService) {
        super(timerService, identityService);
        this.dashboardStorageService = dashboardStorageService;
        this.messageBrokerService = messageBrokerService;
    }


    @Override
    public Dashboard[] getAllRealmDashboards(RequestParams requestParams, String realm) {

        System.out.println("Getting all User Dashboards...");
        try {
            if(!isRealmActiveAndAccessible(realm)) {
                throw new WebApplicationException(FORBIDDEN);
            }
            return this.dashboardStorageService.findAllOfRealm(realm);

        } catch (IllegalStateException ex) {
            throw new WebApplicationException(ex, BAD_REQUEST);
        }
    }

    @Override
    public Dashboard get(RequestParams requestParams, String dashboardId) {
        return this.dashboardStorageService.get(dashboardId);
    }

    @Override
    public Dashboard create(RequestParams requestParams, Dashboard dashboard) {
        try {
            System.out.println("Creating a new Dashboard..");

            // Check if access to realm
            if(!isRealmActiveAndAccessible(dashboard.getRealm())) {
                throw new WebApplicationException(FORBIDDEN);
            }
            dashboard.setOwnerId(getUserId());

            Dashboard created = this.dashboardStorageService.createNew(ValueUtil.clone(dashboard));
            return created;

        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            throw new WebApplicationException(ex, INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void update(RequestParams requestParams, Dashboard dashboard) {
        try {
            System.out.println("Updating a Dashboard..");

            // Check if access to realm
            if(!isRealmActiveAndAccessible(dashboard.getRealm())) {
                throw new WebApplicationException(FORBIDDEN);
            }
            Dashboard updated = this.dashboardStorageService.save(ValueUtil.clone(dashboard));
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            throw new WebApplicationException(ex, INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void delete(RequestParams requestParams, List<String> fields) {
        try {
            this.dashboardStorageService.delete(fields);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            throw new WebApplicationException(ex, INTERNAL_SERVER_ERROR);
        }
    }
}