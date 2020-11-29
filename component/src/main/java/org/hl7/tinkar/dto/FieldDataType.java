package org.hl7.tinkar.dto;

import org.hl7.tinkar.dto.digraph.DigraphDTO;

import java.time.Instant;
import java.util.UUID;

/**
 * Note that Double objects will be converted to Float objects by the serialization mechanisms.
 *
 * The underlying intent is to keep the implementation simple by using the common types,
 * with precision dictated by domain of use, and that long and double are more granular than
 * typically required, and they waste more memory/bandwidth.
 *
 * If there is compelling use for a more precise data type (such as Instant), they can be added when a
 * agreed business need and use case are identified..
 */
public enum FieldDataType {
    STRING((byte) 0, String.class, UUID.fromString("601135f2-2bad-11eb-adc1-0242ac120002")),
    INTEGER((byte) 1, Integer.class,  UUID.fromString("60113822-2bad-11eb-adc1-0242ac120002")),
    FLOAT((byte) 2, Float.class,   UUID.fromString("6011391c-2bad-11eb-adc1-0242ac120002")),
    BOOLEAN((byte) 3, Boolean.class, UUID.fromString("601139ee-2bad-11eb-adc1-0242ac120002")),
    BYTE_ARRAY((byte) 4, byte[].class, UUID.fromString("60113aac-2bad-11eb-adc1-0242ac120002")),
    OBJECT_ARRAY((byte) 5, Object[].class, UUID.fromString("60113b74-2bad-11eb-adc1-0242ac120002")),
    IDENTIFIED_THING((byte) 6, IdentifiedThingDTO.class, UUID.fromString("60113d36-2bad-11eb-adc1-0242ac120002")),
    DIGRAPH((byte) 7, DigraphDTO.class, UUID.fromString("60113dfe-2bad-11eb-adc1-0242ac120002")),
    INSTANT((byte) 8, Instant.class, UUID.fromString("9cb1bd10-31b1-11eb-adc1-0242ac120002"));

    public final byte token;
    public final Class<? extends Object> clazz;
    public final UUID conceptUuid;

    FieldDataType(byte token, Class<? extends Object> clazz, UUID conceptUuid) {
        this.token = token;
        this.clazz = clazz;
        this.conceptUuid = conceptUuid;
    }

    public static FieldDataType fromToken(byte token) {
        switch (token) {
            case 0: return STRING;
            case 1: return INTEGER;
            case 2: return FLOAT;
            case 3: return BOOLEAN;
            case 4: return BYTE_ARRAY;
            case 5: return OBJECT_ARRAY;
            case 6: return IDENTIFIED_THING;
            case 7: return DIGRAPH;
            case 8: return INSTANT;
            default:
                throw new UnsupportedOperationException("Can't handle token: " +
                        token);
        }
    }

    public static FieldDataType getFieldDataType(Object obj) {
        for (FieldDataType  fieldDataType: FieldDataType.values()) {
            if (fieldDataType.clazz.isAssignableFrom(obj.getClass())) {
                return fieldDataType;
            }
        }
        if (obj instanceof Double) {
            return FLOAT;
        }
        throw new UnsupportedOperationException("Can't handle: " +
                obj.getClass().getSimpleName() + "\n" +  obj);
    }
}
