package de.manetmodel.network.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.unit.DataUnit.Type;

public class Unit {

    public enum Time {
	milliseconds, second, minute, hour
    }

    public enum Distance {
	meter, kilometer
    }

    private static final List<Tuple<Time, Integer>> times;
    static {
	List<Tuple<Time, Integer>> units = new ArrayList<Tuple<Time, Integer>>();
	units.add(new Tuple<Time, Integer>(Time.milliseconds, 1));
	units.add(new Tuple<Time, Integer>(Time.second, 1000));
	units.add(new Tuple<Time, Integer>(Time.minute, 60000));
	units.add(new Tuple<Time, Integer>(Time.hour, 3600000));
	times = Collections.unmodifiableList(units);
    }

    private static final List<Tuple<Distance, Integer>> distances;
    static {
	List<Tuple<Distance, Integer>> units = new ArrayList<Tuple<Distance, Integer>>();
	units.add(new Tuple<Distance, Integer>(Distance.meter, 1));
	units.add(new Tuple<Distance, Integer>(Distance.kilometer, 1000));
	distances = Collections.unmodifiableList(units);
    }

    public static int getTimeFactor(Unit.Time unit) {
	return times.stream().filter(t -> t.getFirst().equals(unit)).findFirst().get().getSecond();
    }

    public static int getDistanceFactor(Unit.Distance unit) {
	return distances.stream().filter(t -> t.getFirst().equals(unit)).findFirst().get().getSecond();
    }

    public static Distance lowestDistanceUnit() {
	return distances.get(0).getFirst();
    }

    public static Time lowestTimeUnit() {
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
