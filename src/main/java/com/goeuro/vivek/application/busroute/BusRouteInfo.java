/**
 * 
 */
package com.goeuro.vivek.application.busroute;

/**
 * Entity that represents the JSON response of /api/direct
 *
 */
public class BusRouteInfo {

	private Integer dep_sid;

	private Integer arr_sid;

	private Boolean direct_bus_route;

	/**
	 * @return the dep_sid
	 */
	public Integer getDep_sid() {
		return dep_sid;
	}

	/**
	 * @param dep_sid
	 *            the dep_sid to set
	 */
	public void setDep_sid(Integer dep_sid) {
		this.dep_sid = dep_sid;
	}

	/**
	 * @return the arr_sid
	 */
	public Integer getArr_sid() {
		return arr_sid;
	}

	/**
	 * @param arr_sid
	 *            the arr_sid to set
	 */
	public void setArr_sid(Integer arr_sid) {
		this.arr_sid = arr_sid;
	}

	/**
	 * @return the direct_bus_route
	 */
	public Boolean getDirect_bus_route() {
		return direct_bus_route;
	}

	/**
	 * @param direct_bus_route
	 *            the direct_bus_route to set
	 */
	public void setDirect_bus_route(Boolean direct_bus_route) {
		this.direct_bus_route = direct_bus_route;
	}

}
