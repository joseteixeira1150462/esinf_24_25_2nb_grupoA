package esinf.utils;

import java.util.Objects;

/**
 * A generic class to hold a pair of values. This class provides methods to
 * retrieve and set the values of a pair. The values can be of any type.
 *
 * @param <F> the type of the first value
 * @param <S> the type of the second value
 */
public class Pair<F,S> {
    /**
     * The first element of the pair.
     * This can be of any type specified during the instantiation of the Pair.
     */
    private F first;
    /**
     * The second value in the pair. This value can be of any type, as specified by the generic type parameter {@code S}.
     */
    private S second;

    /**
     * Constructs a new Pair with the specified values.
     *
     * @param first the first value in the pair
     * @param second the second value in the pair
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    /**
     * Retrieves the first value of the pair.
     *
     * @return the first value of the pair
     */
    public F getFirst() {
        return first;
    }
    /**
     * Retrieves the second value of the pair.
     *
     * @return the second value of the pair
     */
    public S getSecond() {
        return second;
    }
    /**
     * Sets the first value of the pair.
     *
     * @param first the new value to set as the first element of the pair
     */
    public void setFirst(F first) {
        this.first = first;
    }
    /**
     * Sets the second value of the pair.
     *
     * @param second the second value to set
     */
    public void setSecond(S second) {
        this.second = second;
    }

    /**
     * Indicates whether some other object is "equal to" this one. Two pairs are considered
     * equal if both their first and second elements are equal.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    /**
     * Returns a hash code value for the Pair object.
     * This method is supported for the benefit of hash tables such
     * as those provided by HashMap.
     *
     * @return a hash code value for this Pair
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
