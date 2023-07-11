
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for Trigger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Trigger"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="trigger_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="trigger_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="trigger_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="event_manipulation" type="{http://www.jooq.org/xsd/jooq-meta-3.19.0.xsd}TriggerEventManipulation"/&gt;
 *         &lt;element name="event_object_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="event_object_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="event_object_table" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="action_order" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="action_condition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="action_statement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="action_orientation" type="{http://www.jooq.org/xsd/jooq-meta-3.19.0.xsd}TriggerActionOrientation"/&gt;
 *         &lt;element name="action_timing" type="{http://www.jooq.org/xsd/jooq-meta-3.19.0.xsd}TriggerActionTiming"/&gt;
 *         &lt;element name="action_reference_old_table" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="action_reference_new_table" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="action_reference_old_row" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="action_reference_new_row" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Trigger", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Trigger implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31900L;
    @XmlElement(name = "trigger_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String triggerCatalog;
    @XmlElement(name = "trigger_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String triggerSchema;
    @XmlElement(name = "trigger_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String triggerName;
    @XmlElement(name = "event_manipulation", required = true)
    @XmlSchemaType(name = "string")
    protected TriggerEventManipulation eventManipulation;
    @XmlElement(name = "event_object_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String eventObjectCatalog;
    @XmlElement(name = "event_object_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String eventObjectSchema;
    @XmlElement(name = "event_object_table", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String eventObjectTable;
    @XmlElement(name = "action_order")
    protected Integer actionOrder;
    @XmlElement(name = "action_condition")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String actionCondition;
    @XmlElement(name = "action_statement", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String actionStatement;
    @XmlElement(name = "action_orientation", required = true)
    @XmlSchemaType(name = "string")
    protected TriggerActionOrientation actionOrientation;
    @XmlElement(name = "action_timing", required = true)
    @XmlSchemaType(name = "string")
    protected TriggerActionTiming actionTiming;
    @XmlElement(name = "action_reference_old_table")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String actionReferenceOldTable;
    @XmlElement(name = "action_reference_new_table")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String actionReferenceNewTable;
    @XmlElement(name = "action_reference_old_row")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String actionReferenceOldRow;
    @XmlElement(name = "action_reference_new_row")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String actionReferenceNewRow;

    public String getTriggerCatalog() {
        return triggerCatalog;
    }

    public void setTriggerCatalog(String value) {
        this.triggerCatalog = value;
    }

    public String getTriggerSchema() {
        return triggerSchema;
    }

    public void setTriggerSchema(String value) {
        this.triggerSchema = value;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String value) {
        this.triggerName = value;
    }

    public TriggerEventManipulation getEventManipulation() {
        return eventManipulation;
    }

    public void setEventManipulation(TriggerEventManipulation value) {
        this.eventManipulation = value;
    }

    public String getEventObjectCatalog() {
        return eventObjectCatalog;
    }

    public void setEventObjectCatalog(String value) {
        this.eventObjectCatalog = value;
    }

    public String getEventObjectSchema() {
        return eventObjectSchema;
    }

    public void setEventObjectSchema(String value) {
        this.eventObjectSchema = value;
    }

    public String getEventObjectTable() {
        return eventObjectTable;
    }

    public void setEventObjectTable(String value) {
        this.eventObjectTable = value;
    }

    public Integer getActionOrder() {
        return actionOrder;
    }

    public void setActionOrder(Integer value) {
        this.actionOrder = value;
    }

    public String getActionCondition() {
        return actionCondition;
    }

    public void setActionCondition(String value) {
        this.actionCondition = value;
    }

    public String getActionStatement() {
        return actionStatement;
    }

    public void setActionStatement(String value) {
        this.actionStatement = value;
    }

    public TriggerActionOrientation getActionOrientation() {
        return actionOrientation;
    }

    public void setActionOrientation(TriggerActionOrientation value) {
        this.actionOrientation = value;
    }

    public TriggerActionTiming getActionTiming() {
        return actionTiming;
    }

    public void setActionTiming(TriggerActionTiming value) {
        this.actionTiming = value;
    }

    public String getActionReferenceOldTable() {
        return actionReferenceOldTable;
    }

    public void setActionReferenceOldTable(String value) {
        this.actionReferenceOldTable = value;
    }

    public String getActionReferenceNewTable() {
        return actionReferenceNewTable;
    }

    public void setActionReferenceNewTable(String value) {
        this.actionReferenceNewTable = value;
    }

    public String getActionReferenceOldRow() {
        return actionReferenceOldRow;
    }

    public void setActionReferenceOldRow(String value) {
        this.actionReferenceOldRow = value;
    }

    public String getActionReferenceNewRow() {
        return actionReferenceNewRow;
    }

    public void setActionReferenceNewRow(String value) {
        this.actionReferenceNewRow = value;
    }

    public Trigger withTriggerCatalog(String value) {
        setTriggerCatalog(value);
        return this;
    }

    public Trigger withTriggerSchema(String value) {
        setTriggerSchema(value);
        return this;
    }

    public Trigger withTriggerName(String value) {
        setTriggerName(value);
        return this;
    }

    public Trigger withEventManipulation(TriggerEventManipulation value) {
        setEventManipulation(value);
        return this;
    }

    public Trigger withEventObjectCatalog(String value) {
        setEventObjectCatalog(value);
        return this;
    }

    public Trigger withEventObjectSchema(String value) {
        setEventObjectSchema(value);
        return this;
    }

    public Trigger withEventObjectTable(String value) {
        setEventObjectTable(value);
        return this;
    }

    public Trigger withActionOrder(Integer value) {
        setActionOrder(value);
        return this;
    }

    public Trigger withActionCondition(String value) {
        setActionCondition(value);
        return this;
    }

    public Trigger withActionStatement(String value) {
        setActionStatement(value);
        return this;
    }

    public Trigger withActionOrientation(TriggerActionOrientation value) {
        setActionOrientation(value);
        return this;
    }

    public Trigger withActionTiming(TriggerActionTiming value) {
        setActionTiming(value);
        return this;
    }

    public Trigger withActionReferenceOldTable(String value) {
        setActionReferenceOldTable(value);
        return this;
    }

    public Trigger withActionReferenceNewTable(String value) {
        setActionReferenceNewTable(value);
        return this;
    }

    public Trigger withActionReferenceOldRow(String value) {
        setActionReferenceOldRow(value);
        return this;
    }

    public Trigger withActionReferenceNewRow(String value) {
        setActionReferenceNewRow(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("trigger_catalog", triggerCatalog);
        builder.append("trigger_schema", triggerSchema);
        builder.append("trigger_name", triggerName);
        builder.append("event_manipulation", eventManipulation);
        builder.append("event_object_catalog", eventObjectCatalog);
        builder.append("event_object_schema", eventObjectSchema);
        builder.append("event_object_table", eventObjectTable);
        builder.append("action_order", actionOrder);
        builder.append("action_condition", actionCondition);
        builder.append("action_statement", actionStatement);
        builder.append("action_orientation", actionOrientation);
        builder.append("action_timing", actionTiming);
        builder.append("action_reference_old_table", actionReferenceOldTable);
        builder.append("action_reference_new_table", actionReferenceNewTable);
        builder.append("action_reference_old_row", actionReferenceOldRow);
        builder.append("action_reference_new_row", actionReferenceNewRow);
    }

    @Override
    public String toString() {
        XMLBuilder builder = XMLBuilder.nonFormatting();
        appendTo(builder);
        return builder.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass()!= that.getClass()) {
            return false;
        }
        Trigger other = ((Trigger) that);
        if (triggerCatalog == null) {
            if (other.triggerCatalog!= null) {
                return false;
            }
        } else {
            if (!triggerCatalog.equals(other.triggerCatalog)) {
                return false;
            }
        }
        if (triggerSchema == null) {
            if (other.triggerSchema!= null) {
                return false;
            }
        } else {
            if (!triggerSchema.equals(other.triggerSchema)) {
                return false;
            }
        }
        if (triggerName == null) {
            if (other.triggerName!= null) {
                return false;
            }
        } else {
            if (!triggerName.equals(other.triggerName)) {
                return false;
            }
        }
        if (eventManipulation == null) {
            if (other.eventManipulation!= null) {
                return false;
            }
        } else {
            if (!eventManipulation.equals(other.eventManipulation)) {
                return false;
            }
        }
        if (eventObjectCatalog == null) {
            if (other.eventObjectCatalog!= null) {
                return false;
            }
        } else {
            if (!eventObjectCatalog.equals(other.eventObjectCatalog)) {
                return false;
            }
        }
        if (eventObjectSchema == null) {
            if (other.eventObjectSchema!= null) {
                return false;
            }
        } else {
            if (!eventObjectSchema.equals(other.eventObjectSchema)) {
                return false;
            }
        }
        if (eventObjectTable == null) {
            if (other.eventObjectTable!= null) {
                return false;
            }
        } else {
            if (!eventObjectTable.equals(other.eventObjectTable)) {
                return false;
            }
        }
        if (actionOrder == null) {
            if (other.actionOrder!= null) {
                return false;
            }
        } else {
            if (!actionOrder.equals(other.actionOrder)) {
                return false;
            }
        }
        if (actionCondition == null) {
            if (other.actionCondition!= null) {
                return false;
            }
        } else {
            if (!actionCondition.equals(other.actionCondition)) {
                return false;
            }
        }
        if (actionStatement == null) {
            if (other.actionStatement!= null) {
                return false;
            }
        } else {
            if (!actionStatement.equals(other.actionStatement)) {
                return false;
            }
        }
        if (actionOrientation == null) {
            if (other.actionOrientation!= null) {
                return false;
            }
        } else {
            if (!actionOrientation.equals(other.actionOrientation)) {
                return false;
            }
        }
        if (actionTiming == null) {
            if (other.actionTiming!= null) {
                return false;
            }
        } else {
            if (!actionTiming.equals(other.actionTiming)) {
                return false;
            }
        }
        if (actionReferenceOldTable == null) {
            if (other.actionReferenceOldTable!= null) {
                return false;
            }
        } else {
            if (!actionReferenceOldTable.equals(other.actionReferenceOldTable)) {
                return false;
            }
        }
        if (actionReferenceNewTable == null) {
            if (other.actionReferenceNewTable!= null) {
                return false;
            }
        } else {
            if (!actionReferenceNewTable.equals(other.actionReferenceNewTable)) {
                return false;
            }
        }
        if (actionReferenceOldRow == null) {
            if (other.actionReferenceOldRow!= null) {
                return false;
            }
        } else {
            if (!actionReferenceOldRow.equals(other.actionReferenceOldRow)) {
                return false;
            }
        }
        if (actionReferenceNewRow == null) {
            if (other.actionReferenceNewRow!= null) {
                return false;
            }
        } else {
            if (!actionReferenceNewRow.equals(other.actionReferenceNewRow)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((triggerCatalog == null)? 0 :triggerCatalog.hashCode()));
        result = ((prime*result)+((triggerSchema == null)? 0 :triggerSchema.hashCode()));
        result = ((prime*result)+((triggerName == null)? 0 :triggerName.hashCode()));
        result = ((prime*result)+((eventManipulation == null)? 0 :eventManipulation.hashCode()));
        result = ((prime*result)+((eventObjectCatalog == null)? 0 :eventObjectCatalog.hashCode()));
        result = ((prime*result)+((eventObjectSchema == null)? 0 :eventObjectSchema.hashCode()));
        result = ((prime*result)+((eventObjectTable == null)? 0 :eventObjectTable.hashCode()));
        result = ((prime*result)+((actionOrder == null)? 0 :actionOrder.hashCode()));
        result = ((prime*result)+((actionCondition == null)? 0 :actionCondition.hashCode()));
        result = ((prime*result)+((actionStatement == null)? 0 :actionStatement.hashCode()));
        result = ((prime*result)+((actionOrientation == null)? 0 :actionOrientation.hashCode()));
        result = ((prime*result)+((actionTiming == null)? 0 :actionTiming.hashCode()));
        result = ((prime*result)+((actionReferenceOldTable == null)? 0 :actionReferenceOldTable.hashCode()));
        result = ((prime*result)+((actionReferenceNewTable == null)? 0 :actionReferenceNewTable.hashCode()));
        result = ((prime*result)+((actionReferenceOldRow == null)? 0 :actionReferenceOldRow.hashCode()));
        result = ((prime*result)+((actionReferenceNewRow == null)? 0 :actionReferenceNewRow.hashCode()));
        return result;
    }

}
