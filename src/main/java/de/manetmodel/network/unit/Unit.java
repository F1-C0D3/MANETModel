package de.manetmodel.network.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.manetmodel.util.Tuple;

public class Unit {

    public enum Type {
	bit, kilobit, megabit, gigabit
    }

    private static final List<Tuple<Type, Long>> types;
    static {
	List<Tuple<Type, Long>> units = new ArrayList<Tuple<Type, Long>>();
	units.add(new Tuple<Unit.Type, Long>(Type.bit, 1L));
	units.add(new Tuple<Unit.Type, Long>(Type.kilobit, 1000L));
	units.add(new Tuple<Unit.Type, Long>(Type.megabit, 1000000L));
	units.add(new Tuple<Unit.Type, Long>(Type.gigabit, 1000000000L));
	types = Collections.unmodifiableList(units);
    }

    public static long getFactor(Unit.Type type) {
	return types.stream().filter(t -> t.getFirst().equals(type)).findFirst().get().getSecond();
    }

    public static Tuple<Type, Long> last() {
	return types.get(types.size() - 1);
    }

    public static Tuple<Type, Long> nextLower(Unit.Type type) {
	Tuple<Type, Long> result = types.get(0);

	for (int index = 0; index < types.size(); index++) {

	    if (type.equals(types.get(index).getFirst()) && index != 0) {
		return types.get(index);
	    }
	}
	return result;
    }
}
