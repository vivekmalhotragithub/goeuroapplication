/**
 * 
 */
package com.goeuro.vivek.application.busroute;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Mapper class that holds the route information
 * 
 * 
 *
 */
@Component
public class BusRouteMapper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BusRouteMapper.class);

	private Path busRouteFile;

	/**
	 * a Map with Keys as Departure stations and Value as a List of Arrival
	 * stations
	 */
	private Map<Integer, List<Integer>> routeToStationsMap;

	/**
	 * Initialize a Route to Stations Map with Bus Route File.
	 * 
	 * @param busRoute
	 */
	public void readBusRouteFile(String busRoute) {
		this.busRouteFile = Paths.get(busRoute);
		routeToStationsMap = new HashMap<>();
		if (!Files.exists(this.busRouteFile)) {
			throw new IllegalArgumentException(
					"Route File does not exist... Exiting!");
		}

		try (BufferedReader reader = Files.newBufferedReader(this.busRouteFile)) {
			Integer noOfRoutes = Integer.parseInt(reader.readLine());
			int noOfLinesRead = 0;
			String routeDef = null;
			while ((routeDef = reader.readLine()) != null) {

				final String routeStations[] = routeDef.split(" ");

				List<Integer> arrivalStation = routeToStationsMap.get(Integer
						.parseInt(routeStations[0]));
				if (arrivalStation != null) {
					throw new IllegalArgumentException(
							"duplicate routes defined in the ");
				} else {
					arrivalStation = new LinkedList<>();
					for (int index = 1, length = routeStations.length; index < length; index++) {
						arrivalStation.add(Integer
								.parseInt(routeStations[index]));
					}
					// add route stations
					routeToStationsMap.put(Integer.parseInt(routeStations[0]),
							arrivalStation);
				}

				noOfLinesRead++;
			}
			if (noOfRoutes != noOfLinesRead) {
				throw new IllegalArgumentException(
						"No of Routes defined do not match the routes configured.");
			}
			LOGGER.info("Finished loading route information.");

			registerFileModifications();

		} catch (IOException | NumberFormatException ex) {
			//
			throw new IllegalArgumentException("Error reading the Route File.",
					ex);
		}

	}

	// register the file for an modifications
	private void registerFileModifications() {
		LOGGER.info("Registering File for changes");
		final Path fileName = this.busRouteFile.getFileName();
		final Path parentDir = this.busRouteFile.getParent();

		new Thread(
				() -> {

					WatchService watcher = null;
					try {
						watcher = FileSystems.getDefault().newWatchService();
						boolean isWatcherEnabled = true;
						parentDir.register(watcher,
								StandardWatchEventKinds.ENTRY_MODIFY);
						while (isWatcherEnabled) {

							WatchKey key = watcher.take();
							for (WatchEvent<?> event : key.pollEvents()) {
								if (event.kind() == StandardWatchEventKinds.OVERFLOW)
									continue;
								else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
									LOGGER.info("Changes in Route File Detected.");
									Path newFile = (Path) event.context();
									if (newFile.endsWith(fileName)) {
										LOGGER.info("Reloading the file:"
												+ parentDir.resolve(newFile)
														.toString());
										BusRouteMapper.this
												.readBusRouteFile(parentDir
														.resolve(newFile)
														.toString());
										isWatcherEnabled = false;
										break;
									}
								}
							}
							boolean valid = key.reset();
							if (!valid) {
								break;
							}
						}
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}

				}).start();

	}

	/**
	 * Check if a route exists between departure and arrival station
	 * 
	 * @param depStation
	 * @param arrStation
	 * @return true if it exists or else false
	 */
	public boolean checkRouteExist(final Integer depStation,
			final Integer arrStation) {
		for (Entry<Integer, List<Integer>> route : routeToStationsMap
				.entrySet()) {
			List<Integer> routeStations = route.getValue();
			int depStationIndex = routeStations.indexOf(depStation);
			if (depStationIndex != -1) {
				int arrivalStationIndex = routeStations.indexOf(arrStation);
				if (arrivalStationIndex != -1
						&& arrivalStationIndex >= depStationIndex) {
					LOGGER.info("Route found from station {} to {}",depStation,arrStation);
					return true;
				}
			}
		}
		LOGGER.info("No Route found from station {} to {}",depStation,arrStation);
		return false;
	}

}
