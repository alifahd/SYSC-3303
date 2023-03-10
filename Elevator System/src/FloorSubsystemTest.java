import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;

/*
 * Class to test the Floor Subsystem class. Checks lamp r and adding requests.
 */
public class FloorSubsystemTest {
	private FloorSubsystem floor = new FloorSubsystem("requests.txt");

	/**
	 * Test that request has been added to the FloorSubsystem list and then called again
	 * and check if requests were doubled.
	 */
	@Test
	public void testAddFloorRequest() {
		assertEquals(8, floor.getRequests().size());
		floor.addFloorRequest("requests.txt");
		assertEquals(16, floor.getRequests().size());
	}

	/**
	 * Tests that all lamps and sensors should start in off state and then turn them on
	 */
	@Test
	public void testLampsSensors() {
		String elevator = "Elevator2";
		int elevatorIndex = 1;
		int floorNum = 4;
		Map<Integer, ArrayList<Boolean>> sensors = floor.getArrivalSensors();
		ArrayList<Boolean> lamps = sensors.get(floorNum);
		boolean sensorOn = lamps.get(elevatorIndex);
		
		assertEquals(false, sensorOn);
	
		floor.setLampsSensors("4", elevator, true);
		sensors = floor.getArrivalSensors();
		lamps = sensors.get(floorNum);
		sensorOn = lamps.get(elevatorIndex);
		
		assertEquals(true, sensorOn);//check its on
		
		floor.setLampsSensors("4", elevator, false);//turn it off
		sensors = floor.getArrivalSensors();
		lamps = sensors.get(floorNum);
		sensorOn = lamps.get(elevatorIndex);
		
		assertEquals(false, sensorOn);//check for on again
	}
}