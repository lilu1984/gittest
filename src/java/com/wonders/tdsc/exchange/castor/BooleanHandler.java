package com.wonders.tdsc.exchange.castor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.GeneralizedFieldHandler;

public class BooleanHandler  extends GeneralizedFieldHandler {
    /** 日志 */
    protected static Log logger = LogFactory.getLog(BooleanHandler.class);


    /**
     * Creates a new MyDateHandler instance
     */
    public BooleanHandler() {
        super();
    }

    /**
     * This method is used to convert the value when the getValue method is called. The getValue method will obtain the actual field value from given
     * 'parent' object. This convert method is then invoked with the field's value. The value returned from this method will be the actual value
     * returned by getValue method.
     * 
     * @param value
     *            the object value to convert after performing a get operation
     * @return the converted value.
     */
    public Object convertUponGet(Object value) {
        logger.debug("[convertUponGet][value]:" + value);

        if (value == null || "".equals(value))
            return "";
        if("1".equals(value))
        	return "是";
        return "否";
    }

    /**
     * This method is used to convert the value when the setValue method is called. The setValue method will call this method to obtain the converted
     * value. The converted value will then be used as the value to set for the field.
     * 
     * @param value
     *            the object value to convert before performing a set operation
     * @return the converted value.
     */
    public Object convertUponSet(Object value) {
        logger.debug("[convertUponSet][value]:" + value);

        if (value == null || "".equals(value))
            return "";

        if("1".equals(value))
        	return "是";
        return "否";
    }

    /**
     * Returns the class type for the field that this GeneralizedFieldHandler converts to and from. This should be the type that is used in the object
     * model.
     * 
     * @return the class type of of the field
     */
    public Class getFieldType() {
        return String.class;
    }

    /**
     * Creates a new instance of the object described by this field.
     * 
     * @param parent
     *            The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException
     *             This field is a simple type and cannot be instantiated
     */
    public Object newInstance(Object parent) throws IllegalStateException {
        // -- Since it's marked as a string...just return null,
        // -- it's not needed.
        return null;
    }

}
