package com.github.kleesup.kleegdx.core.serialize;

/**
 * An implementation of {@link IDataTransferable} featuring some sort of base class that constantly holds its data
 * transfer object (caching instead of recreating each {@link #toDataTransferObject()} call). Therefore, all methods
 * updating transferable data also need to update the DTO object. In this case it might be better to keep fields in
 * the DTO object and only read from and write to it.
 */
public abstract class AbstractSingleDataTransferable<T> implements IDataTransferable<T> {

    /** Internal hold DTO object. */
    protected T data;

    /**
     * Builds a new instance of this class and creates a new DTO object by calling {@link #buildDataTransferObject()}.
     */
    protected AbstractSingleDataTransferable(){
        this.data = buildDataTransferObject();
    }

    /**
     * Builds a new instance of this class and takes in a given DTO object.
     * @param data The DTO object to save locally.
     */
    protected AbstractSingleDataTransferable(T data){
        this.data = data;
    }

    /**
     * Builds a new data transfer object.
     * @return The build instance of the DTO.
     */
    protected abstract T buildDataTransferObject();

    @Override
    public T toDataTransferObject() {
        return data; //always returns the single instance.
    }
}
