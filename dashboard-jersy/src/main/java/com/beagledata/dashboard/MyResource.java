package com.beagledata.dashboard;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.beagledata.controller.DashBoardRTController;

@Path("list")
public class MyResource {

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "test dashboard webapps";
    }
    
    @GET
    @Path("rtdata")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getRtDate() {
        return new DashBoardRTController().listarRt();
    }
}
