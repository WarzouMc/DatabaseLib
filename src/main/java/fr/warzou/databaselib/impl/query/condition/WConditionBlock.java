package fr.warzou.databaselib.impl.query.condition;

import fr.warzou.databaselib.dbl.db.query.condition.Condition;
import fr.warzou.databaselib.dbl.db.query.condition.ConditionBlock;

public class WConditionBlock extends WCondition implements ConditionBlock {

    @Override
    public Condition and(ConditionBlock condition) {
        return null;
    }

    @Override
    public Condition or(ConditionBlock condition) {
        return null;
    }
}
