package net.drapuria.framework.command.meta;

import lombok.Getter;
import net.drapuria.framework.command.parameter.ParameterData;

import java.lang.reflect.Method;

@Getter
public abstract class SubCommandMeta<E, T extends ParameterData<?>> {

    protected final T parameterData;
    protected final String[] aliases;

    protected final Object instance;
    protected final Method method;

    protected final String defaultAlias;
    protected final String parameterString;

    public SubCommandMeta(T parameterData, String[] aliases, Object instance, Method method, String parameterString) {
        this.parameterData = parameterData;
        this.aliases = aliases;
        this.instance = instance;
        this.method = method;
        this.defaultAlias = aliases[0];
        this.parameterString = parameterString;
    }

    public abstract boolean execute(E executor, String[] params);

    public abstract boolean canAccess(E executor);

}