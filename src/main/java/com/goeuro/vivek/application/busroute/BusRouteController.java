/**
 * 
 */
package com.goeuro.vivek.application.busroute;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to map the REST service /api/direct
 *
 */
@RestController
@RequestMapping(path = "/api")
public class BusRouteController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BusRouteController.class);

	private BusRouteMapper busRouteMapper;

	@Autowired
	public BusRouteController(BusRouteMapper busRouteMapper) {
		this.busRouteMapper = busRouteMapper;
	}

	@RequestMapping(path = "/direct", method = RequestMethod.GET)
	public BusRouteInfo routeChecker(
			@RequestParam(value = "dep_sid") String departureId,
			@RequestParam(value = "arr_sid") String arrivalId) {
		LOGGER.info("Looking whether a route exists from station {} to {}",
				departureId, arrivalId);
		BusRouteInfo info = new BusRouteInfo();
		info.setArr_sid(Integer.parseInt(arrivalId));
		info.setDep_sid(Integer.parseInt(departureId));
		info.setDirect_bus_route(busRouteMapper.checkRouteExist(
				Integer.parseInt(departureId), Integer.parseInt(arrivalId)));

		return info;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ IllegalArgumentException.class,
			NumberFormatException.class })
	public @ResponseBody ErrorInfo handleBadRequests(HttpServletRequest req,
			Exception ex) throws IOException {
		LOGGER.info("Error handling request", ex);
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
}
