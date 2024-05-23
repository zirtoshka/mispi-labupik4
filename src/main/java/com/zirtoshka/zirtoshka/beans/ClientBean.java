package com.zirtoshka.zirtoshka.beans;

import com.zirtoshka.zirtoshka.beans.interfaces.AreaMXBean;
import com.zirtoshka.zirtoshka.beans.interfaces.SquareMXBean;
import com.zirtoshka.zirtoshka.db.HitResult;

import com.zirtoshka.zirtoshka.utils.Validation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.Map;


@Getter
@Setter
@ToString
@Named("client")
@ApplicationScoped
public class ClientBean implements Serializable {
    private final String sessionId;
    private final LinkedList<HitResult> currentHits;
    private MBeanServer mbs;
    AreaMXBean area;
    SquareMXBean square;

    @ManagedProperty(value = "#{coordinates}")
    private Coordinates coordinates = new Coordinates();
    @ManagedProperty(value = "#{service}")
    private Service service = new Service();


    public ClientBean() throws NotCompliantMBeanException {
        this.sessionId = FacesContext.getCurrentInstance().getExternalContext().getSessionId(true);
        this.currentHits = service.getUserHits(sessionId);
        this.mbs = ManagementFactory.getPlatformMBeanServer();
        this.area = new Area();
        this.square=new Square();
        ObjectName areaName = null;
        ObjectName squareName = null;

        try {
            areaName = new ObjectName("ClientBean:name=area");
            mbs.registerMBean(area, areaName);
            squareName = new ObjectName("ClientBean:name=square");
            mbs.registerMBean(square, squareName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateXWithValue(Double value) {
        coordinates.setX(value);
    }


    public void makeUserRequest() {
        makeRequest(this.coordinates);
    }



    public void makeRemoteRequest() {
        try {
            if (!Validation.validate(coordinates)) {
                System.out.println("Not valid");
                throw new NumberFormatException();
            }
            makeRequest(coordinates);
        } catch (NullPointerException | NumberFormatException exception) {
            System.out.println("error in params");

        }
    }

    public void makeCanvasRequest() {
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            String xValue = params.get("x");
            String yValue = params.get("y");
            String rValue = params.get("r");

            if (xValue != null && yValue != null && rValue != null) {
                double x = Double.parseDouble(xValue.trim());
                double y = Double.parseDouble(yValue.trim());
                double r = Double.parseDouble(rValue.trim());
                double currX=coordinates.getX();
                double currY=coordinates.getY();

                coordinates.setX(x);
                coordinates.setY(y);
                coordinates.setR(r);
                makeRequest(coordinates);
                coordinates.setX(currX);
                coordinates.setY(currY);
            } else {
                System.out.println(xValue);
                System.out.println(yValue);
                System.out.println(rValue);
            }
        } catch ( IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public void makeRequest(Coordinates coordinates) {
        System.out.println("make rq");
        HitResult res = service.processRequest(this.sessionId, coordinates);
        area.increaseCounterAll();
        if (res != null) {
            area.inArea(res);
            square.getSquare(res.getR());
            this.currentHits.addFirst(res);
            if (res.isResult()) {
                area.increaseCounterInTheArea();
            }
        }
    }


    public void clearHits() {
        currentHits.clear();
        service.clearUserHits(this.sessionId);
    }
}
