package com.andjela.airport.controller;

import com.andjela.airport.entity.Gate;
import com.andjela.airport.service.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@DependsOn("dataLoader")
public class GateController {

    @Autowired
    private GateService gateService;

    @RequestMapping(value = "/gate/{flightNumber}", method = RequestMethod.GET)
    @ResponseBody
    public Gate getAndAssignGateByFlightNumber(@PathVariable("flightNumber") final String flightNumber) {
        return gateService.getAndAssignGateByFlightNumber(flightNumber);
    }

    @RequestMapping(value = "/gate/{gateNumber}", method = RequestMethod.PUT)
    @ResponseBody
    public Gate updateGateAsAvailable(@PathVariable("gateNumber") final String gateNumber) {
        return gateService.updateGateAsAvailable(gateNumber);
    }


}
