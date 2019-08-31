package com.jumkid.media.graphql.type;

import com.jumkid.media.util.DateTimeUtils;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.LocalDateTime;
import java.util.Date;

public class GraphQLDate extends GraphQLScalarType {

    public static final String DATE_NAME = "Date";
    public static final String DATE_DESCRIPTION = "Date type";

    public GraphQLDate() {
        super(DATE_NAME, DATE_DESCRIPTION, new Coercing<Date, String>() {
            private Date convertImpl(Object input) {
                if (input instanceof Long) {
                    return new Date((Long) input);
                } else if (input instanceof String) {
                    LocalDateTime localDateTime = DateTimeUtils.parseDate((String) input);

                    if (localDateTime != null) {
                        return DateTimeUtils.toDate(localDateTime);
                    }
                }
                return null;
            }

            @Override
            public String serialize(Object input) {
                if (input instanceof Date) {
                    return DateTimeUtils.toISOString((Date) input);
                } else {
                    Date result = convertImpl(input);
                    if (result == null) {
                        throw new CoercingSerializeException("Invalid value '" + input + "' for Date");
                    }
                    return DateTimeUtils.toISOString(result);
                }
            }

            @Override
            public Date parseValue(Object input) {
                Date result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException("Invalid value '" + input + "' for Date");
                }
                return result;
            }

            @Override
            public Date parseLiteral(Object input) {
                if (!(input instanceof StringValue)) return null;
                String value = ((StringValue) input).getValue();
                Date result = convertImpl(value);
                return result;
            }
        });
    }

}
