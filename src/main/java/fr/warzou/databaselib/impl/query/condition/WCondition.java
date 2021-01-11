package fr.warzou.databaselib.impl.query.condition;

import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.dbl.db.query.condition.Condition;

import java.util.Arrays;
import java.util.List;

public class WCondition implements Condition {

    @SafeVarargs
    @Override
    public final <T> Condition in(Column<T> column, T... in) {
        return in(column, Arrays.asList(in));
    }

    @Override
    public <T> Condition in(Column<T> column, List<T> in) {
        return null;
    }

    @Override
    public <T> Condition between(Column<T> column, T bound1, T bound2) {
        return null;
    }

    @Override
    public <T extends String> Condition like(Column<T> column, String model) {
        return null;
    }

    @Override
    public <T extends String> Condition likeStartedBy(Column<T> column, String start) {
        return null;
    }

    @Override
    public <T extends String> Condition likeEndedBy(Column<T> column, String end) {
        return null;
    }

    @Override
    public <T extends String> Condition likeContain(Column<T> column, String contain) {
        return null;
    }

    @Override
    public <T extends String> Condition likeBoundedBy(Column<T> column, String start, String end) {
        return null;
    }

    @Override
    public <T extends String> Condition likeWithUnknownCharacter(Column<T> column, String before, String after) {
        return null;
    }

    @Override
    public Condition and(Condition condition) {
        return null;
    }

    @Override
    public Condition and(boolean condition) {
        return null;
    }

    @Override
    public Condition or(Condition condition) {
        return null;
    }

    @Override
    public Condition or(boolean condition) {
        return null;
    }

}
