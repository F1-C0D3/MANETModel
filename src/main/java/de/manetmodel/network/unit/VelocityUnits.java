package de.manetmodel.network.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.unit.DataUnit.Type;

public class VelocityUnits {

    public enum TimeUnit {
	second, minute, hour
    }

    public enum DistanceUnit {
	meter, kilometer
    }

  
    private static final List<Tuple<TimeUnit, Integer>> times;
    static {
	List<Tuple<TimeUnit, Integer>> units = new ArrayList<Tuple<TimeUnit, Integer>>();
	units.add(new Tuple<TimeUnit, Integer>(TimeUnit.second, 1));
	units.add(new Tuple<TimeUnit, Integer>(TimeUnit.minute, 60));
	units.add(new Tuple<TimeUnit, Integer>(TimeUnit.hour, 3600));
	times = Collections.unmodifiableList(units);
    }

    private static final List<Tuple<DistanceUnit, Integer>> distances;
    static {
	List<Tuple<DistanceUnit, Integer>> units = new ArrayList<Tuple<DistanceUnit, Integer>>();
	units.add(new Tuple<DistanceUnit, Integer>(DistanceUnit.meter, 1));
	units.add(new Tuple<DistanceUnit, Integer>(DistanceUnit.kilometer, 1000));
	distances = Collections.unmodifiableList(units);
    }

    public static int getTimeFactor(VelocityUnits.TimeUnit unit) {
	return times.stream().filter(t -> t.getFirst().equals(unit)).findFirst().get().getSecond();
    }
    public static int getDistanceFactor(VelocityUnits.DistanceUnit unit) {
	return distances.stream().filter(t -> t.getFirst().equals(unit)).findFirst().get().getSecond();
    }

    public static DistanceUnit lowestDistanceUnit() {
	return distances.get(0).getFirst();
    }
    
    public static TimeUnit lowestTimeUnit() {
	return times.get(0).getFirst();
    }


//    public static Tuple<Type, Long> nextLower(DataUnit.Type type) {
//	Tuple<Type, Long> result = types.get(0);
//
//	for (int index = 0; index < types.size(); index++) {
//
//	    if (type.equals(types.get(index).getFirst()) && index != 0) {
//		return types.get(index);
//	    }
//	}
//	return result;
//    }

}
