package nl.deltares.keycloak.mocking;

import javax.persistence.*;
import java.util.*;

public class MockTypedQuery<T> implements TypedQuery<T> {

    public List<T> typedList = new ArrayList<>();

    @Override
    public List<T> getResultList() {
        return typedList;
    }

    @Override
    public T getSingleResult() {
        return typedList.size() > 0 ? typedList.get(0) : null;
    }

    @Override
    public int executeUpdate() {
        return 0;
    }

    @Override
    public TypedQuery<T> setMaxResults(int i) {
        return null;
    }

    @Override
    public int getMaxResults() {
        return 0;
    }

    @Override
    public TypedQuery<T> setFirstResult(int i) {
        return null;
    }

    @Override
    public int getFirstResult() {
        return 0;
    }

    @Override
    public TypedQuery<T> setHint(String s, Object o) {
        return null;
    }

    @Override
    public Map<String, Object> getHints() {
        return null;
    }

    @Override
    public <T1> TypedQuery<T> setParameter(Parameter<T1> parameter, T1 t1) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(Parameter<Calendar> parameter, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(Parameter<Date> parameter, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(String s, Object o) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(String s, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(String s, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(int i, Object o) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(int i, Calendar calendar, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<T> setParameter(int i, Date date, TemporalType temporalType) {
        return null;
    }

    @Override
    public Set<Parameter<?>> getParameters() {
        return null;
    }

    @Override
    public Parameter<?> getParameter(String s) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public Parameter<?> getParameter(int i) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(int i, Class<T> aClass) {
        return null;
    }

    @Override
    public boolean isBound(Parameter<?> parameter) {
        return false;
    }

    @Override
    public <T> T getParameterValue(Parameter<T> parameter) {
        return null;
    }

    @Override
    public Object getParameterValue(String s) {
        return null;
    }

    @Override
    public Object getParameterValue(int i) {
        return null;
    }

    @Override
    public TypedQuery<T> setFlushMode(FlushModeType flushModeType) {
        return null;
    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public TypedQuery<T> setLockMode(LockModeType lockModeType) {
        return null;
    }

    @Override
    public LockModeType getLockMode() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
